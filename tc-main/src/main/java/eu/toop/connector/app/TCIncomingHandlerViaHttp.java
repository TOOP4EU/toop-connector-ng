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
package eu.toop.connector.app;

import javax.annotation.Nonnull;

import com.helger.commons.ValueEnforcer;
import com.helger.commons.error.level.EErrorLevel;

import eu.toop.connector.api.me.incoming.IMEIncomingHandler;
import eu.toop.connector.api.me.incoming.IncomingEDMErrorResponse;
import eu.toop.connector.api.me.incoming.IncomingEDMRequest;
import eu.toop.connector.api.me.incoming.IncomingEDMResponse;
import eu.toop.connector.api.me.incoming.MEIncomingException;
import eu.toop.connector.app.incoming.DC_DP_TriggerViaHttp;
import eu.toop.kafkaclient.ToopKafkaClient;

/**
 * Implementation of {@link IMEIncomingHandler} using
 * {@link DC_DP_TriggerViaHttp} to forward the message. By default this class is
 * invoked if an incoming AS4 message is received.
 *
 * @author Philip Helger
 * @since 2.0.0-rc4
 */
public class TCIncomingHandlerViaHttp implements IMEIncomingHandler
{
  private final String m_sLogPrefix;

  /**
   * @param sLogPrefix
   *        The log prefix to use. May not be <code>null</code> but maybe empty.
   */
  public TCIncomingHandlerViaHttp (@Nonnull final String sLogPrefix)
  {
    m_sLogPrefix = ValueEnforcer.notNull (sLogPrefix, "LogPrefix");
  }

  public void handleIncomingRequest (@Nonnull final IncomingEDMRequest aRequest) throws MEIncomingException
  {
    ToopKafkaClient.send (EErrorLevel.INFO, () -> m_sLogPrefix + "TC got DP incoming MEM request (2/4)");
    DC_DP_TriggerViaHttp.forwardMessage (aRequest);
  }

  public void handleIncomingResponse (@Nonnull final IncomingEDMResponse aResponse) throws MEIncomingException
  {
    ToopKafkaClient.send (EErrorLevel.INFO,
                          () -> m_sLogPrefix +
                                "TC got DC incoming MEM response (4/4) with " +
                                aResponse.attachments ().size () +
                                " attachments");
    DC_DP_TriggerViaHttp.forwardMessage (aResponse);
  }

  public void handleIncomingErrorResponse (@Nonnull final IncomingEDMErrorResponse aErrorResponse) throws MEIncomingException
  {
    ToopKafkaClient.send (EErrorLevel.INFO, () -> m_sLogPrefix + "TC got DC incoming MEM response (4/4) with ERRORs");
    DC_DP_TriggerViaHttp.forwardMessage (aErrorResponse);
  }
}
