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
package eu.toop.connector.api.user;

import java.io.IOException;

import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.helger.commons.io.resource.ClassPathResource;
import com.helger.commons.io.stream.StreamHelper;
import com.helger.commons.mime.CMimeType;
import com.helger.httpclient.HttpClientManager;
import com.helger.httpclient.response.ResponseHandlerJson;
import com.helger.json.IJson;
import com.helger.json.serialize.JsonWriter;
import com.helger.json.serialize.JsonWriterSettings;

import eu.toop.connector.api.TCIdentifierFactory;
import eu.toop.connector.api.me.EMEProtocol;
import eu.toop.connector.api.rest.TCOutgoingMessage;
import eu.toop.connector.api.rest.TCOutgoingMetadata;
import eu.toop.connector.api.rest.TCPayload;
import eu.toop.connector.api.rest.TCRestJAXB;

public class MainValidateLookupAndSendEdmRequest
{
  private static final Logger LOGGER = LoggerFactory.getLogger (MainValidateLookupAndSendEdmRequest.class);

  public static void main (final String [] args) throws IOException
  {
    final TCOutgoingMessage aOM = new TCOutgoingMessage ();
    {
      final TCOutgoingMetadata aMetadata = new TCOutgoingMetadata ();
      aMetadata.setSenderID (TCRestJAXB.createTCID (TCIdentifierFactory.PARTICIPANT_SCHEME, "9914:tc-ng-test-sender"));
      aMetadata.setReceiverID (TCRestJAXB.createTCID (TCIdentifierFactory.PARTICIPANT_SCHEME, "9915:tooptest"));
      aMetadata.setDocTypeID (TCRestJAXB.createTCID (TCIdentifierFactory.DOCTYPE_SCHEME,
                                                     "urn:eu:toop:ns:dataexchange-1p40::Response##urn:eu.toop.response.registeredorganization::1.40"));
      aMetadata.setProcessID (TCRestJAXB.createTCID (TCIdentifierFactory.PROCESS_SCHEME, "urn:eu.toop.process.datarequestresponse"));
      aMetadata.setTransportProtocol (EMEProtocol.AS4.getTransportProfileID ());
      aOM.setMetadata (aMetadata);
    }
    {
      final TCPayload aPayload = new TCPayload ();
      aPayload.setValue (StreamHelper.getAllBytes (new ClassPathResource ("edm/Concept Request_LP.xml")));
      aPayload.setMimeType (CMimeType.APPLICATION_XML.getAsString ());
      aPayload.setContentID ("mock-request@toop");
      aOM.addPayload (aPayload);
    }

    LOGGER.info (TCRestJAXB.outgoingMessage ().getAsString (aOM));

    try (HttpClientManager aHCM = new HttpClientManager ())
    {
      final HttpPost aPost = new HttpPost ("http://localhost:8090/api/user/submit/request");
      aPost.setEntity (new ByteArrayEntity (TCRestJAXB.outgoingMessage ().getAsBytes (aOM)));
      final IJson aJson = aHCM.execute (aPost, new ResponseHandlerJson ());
      LOGGER.info (new JsonWriter (new JsonWriterSettings ().setIndentEnabled (true)).writeAsString (aJson));
    }
  }
}
