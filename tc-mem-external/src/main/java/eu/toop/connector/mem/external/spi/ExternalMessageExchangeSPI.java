/**
 * This work is protected under copyrights held by the members of the
 * TOOP Project Consortium as indicated at
 * http://wiki.ds.unipi.gr/display/TOOP/Contributors
 * (c) 2018-2021. All rights reserved.
 *
 * This work is licensed under the EUPL 1.2.
 *
 *  = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = =
 *
 * Licensed under the EUPL, Version 1.2 or – as soon they will be approved
 * by the European Commission - subsequent versions of the EUPL
 * (the "Licence");
 * You may not use this work except in compliance with the Licence.
 * You may obtain a copy of the Licence at:
 *
 *         https://joinup.ec.europa.eu/software/page/eupl
 */
package eu.toop.connector.mem.external.spi;

import javax.annotation.Nonnull;
import javax.servlet.ServletContext;

import com.helger.commons.ValueEnforcer;
import com.helger.commons.annotation.IsSPIImplementation;
import com.helger.commons.annotation.Nonempty;
import com.helger.commons.collection.impl.CommonsArrayList;
import com.helger.commons.collection.impl.ICommonsList;
import com.helger.commons.error.level.EErrorLevel;

import eu.toop.connector.api.me.IMessageExchangeSPI;
import eu.toop.connector.api.me.incoming.IMEIncomingHandler;
import eu.toop.connector.api.me.incoming.IncomingEDMErrorResponse;
import eu.toop.connector.api.me.incoming.IncomingEDMRequest;
import eu.toop.connector.api.me.incoming.IncomingEDMResponse;
import eu.toop.connector.api.me.incoming.MEIncomingTransportMetadata;
import eu.toop.connector.api.me.model.MEMessage;
import eu.toop.connector.api.me.model.MEPayload;
import eu.toop.connector.api.me.outgoing.IMERoutingInformation;
import eu.toop.connector.api.me.outgoing.MEOutgoingException;
import eu.toop.connector.mem.external.MEMDelegate;
import eu.toop.connector.mem.external.MEMDumper;
import eu.toop.edm.EDMErrorResponse;
import eu.toop.edm.EDMRequest;
import eu.toop.edm.EDMResponse;
import eu.toop.edm.IEDMTopLevelObject;
import eu.toop.edm.xml.EDMPayloadDeterminator;
import eu.toop.kafkaclient.ToopKafkaClient;

/**
 * Implementation of {@link IMessageExchangeSPI} using the "TOOP AS4 Gateway
 * back-end interface".
 *
 * @author Philip Helger
 */
@IsSPIImplementation
public class ExternalMessageExchangeSPI implements IMessageExchangeSPI {
  private IMEIncomingHandler m_aIncomingHandler;

  public ExternalMessageExchangeSPI() {
  }

  @Nonnull
  @Nonempty
  public String getID() {
    return "external";
  }

  public void registerIncomingHandler(@Nonnull final ServletContext aServletContext,
      @Nonnull final IMEIncomingHandler aIncomingHandler) {
    ValueEnforcer.notNull(aServletContext, "ServletContext");
    ValueEnforcer.notNull(aIncomingHandler, "IncomingHandler");
    if (m_aIncomingHandler != null)
      throw new IllegalStateException("Another incoming handler was already registered!");
    m_aIncomingHandler = aIncomingHandler;

    final MEMDelegate aDelegate = MEMDelegate.getInstance();

    aDelegate.registerNotificationHandler(aRelayResult -> {
      // more to come
      ToopKafkaClient.send(EErrorLevel.INFO,
                           () -> "Notification[" + aRelayResult.getErrorCode() + "]: " + aRelayResult.getDescription());
    });

    aDelegate.registerSubmissionResultHandler(aRelayResult -> {
      // more to come
      ToopKafkaClient.send(EErrorLevel.INFO,
                           () -> "SubmissionResult[" + aRelayResult.getErrorCode() +
                                 "]: " +
                                 aRelayResult.getDescription());
    });

    // Register the AS4 handler needed
    aDelegate.registerMessageHandler(aMEMessage -> {
      final MEPayload aHead = aMEMessage.payloads().getFirst();
      final IEDMTopLevelObject aTopLevel = EDMPayloadDeterminator.parseAndFind(aHead.getData().getInputStream());
      final String sTopLevelContentID = aHead.getContentID();

      final MEIncomingTransportMetadata aMetadata = new MEIncomingTransportMetadata(aMEMessage.getSenderID(), aMEMessage.getReceiverID(), aMEMessage.getDoctypeID(), aMEMessage.getProcessID());

      if (aTopLevel instanceof EDMRequest) {
        // Request
        m_aIncomingHandler.handleIncomingRequest(new IncomingEDMRequest((EDMRequest) aTopLevel,
                                                                        sTopLevelContentID,
                                                                        aMetadata));
      } else if (aTopLevel instanceof EDMResponse) {
        // Response
        final ICommonsList<MEPayload> aAttachments = new CommonsArrayList<>();
        for (final MEPayload aItem : aMEMessage.payloads())
          if (aItem != aHead)
            aAttachments.add(aItem);
        m_aIncomingHandler.handleIncomingResponse(new IncomingEDMResponse((EDMResponse) aTopLevel,
                                                                          sTopLevelContentID,
                                                                          aAttachments,
                                                                          aMetadata));
      } else if (aTopLevel instanceof EDMErrorResponse) {
        // Error response
        m_aIncomingHandler.handleIncomingErrorResponse(new IncomingEDMErrorResponse((EDMErrorResponse) aTopLevel,
                                                                                    sTopLevelContentID,
                                                                                    aMetadata));
      } else {
        // Unknown
        ToopKafkaClient.send(EErrorLevel.ERROR, () -> "Unsuspported Message: " + aTopLevel);
      }
    });
  }

  public void sendOutgoing(@Nonnull final IMERoutingInformation aRoutingInfo, @Nonnull final MEMessage aMessage)
      throws MEOutgoingException {
    MEMDumper.dumpOutgoingMessage(aRoutingInfo, aMessage);
    MEMDelegate.getInstance().sendMessage(aRoutingInfo, aMessage);
  }

  public void shutdown(@Nonnull final ServletContext aServletContext) {
    // empty
  }
}
