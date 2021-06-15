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
package eu.toop.connector.api.rest;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.nio.charset.StandardCharsets;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;

import com.helger.commons.io.resource.FileSystemResource;
import com.helger.commons.mime.CMimeType;
import com.helger.commons.mock.CommonsTestHelper;

import eu.toop.connector.api.TCIdentifierFactory;
import eu.toop.connector.api.me.EMEProtocol;

/**
 * Test class for class {@link TCRestJAXB}.
 *
 * @author Philip Helger
 */
public final class TCRestJAXBTest
{
  private static final Logger LOGGER = LoggerFactory.getLogger (TCRestJAXBTest.class);

  @Test
  public void testBasic ()
  {
    assertTrue (TCRestJAXB.XSD_RES.exists ());
  }

  @Test
  public void testOutgoing ()
  {
    final TCOutgoingMessage m = new TCOutgoingMessage ();
    final TCOutgoingMetadata md = new TCOutgoingMetadata ();
    md.setSenderID (TCRestJAXB.createTCID (TCIdentifierFactory.PARTICIPANT_SCHEME, "9999:sender"));
    md.setReceiverID (TCRestJAXB.createTCID (TCIdentifierFactory.PARTICIPANT_SCHEME, "9999:receiver"));
    md.setDocTypeID (TCRestJAXB.createTCID (TCIdentifierFactory.DOCTYPE_SCHEME,
                                            "urn:eu:toop:ns:dataexchange-1p40::Request##urn:eu.toop.request.registeredorganization::1.40"));
    md.setProcessID (TCRestJAXB.createTCID (TCIdentifierFactory.DOCTYPE_SCHEME, "urn:eu.toop.process.datarequestresponse"));
    md.setTransportProtocol (EMEProtocol.AS4.getTransportProfileID ());
    md.setEndpointURL ("https://target.example.org/as4");
    md.setReceiverCertificate ("Receiver's certificate".getBytes (StandardCharsets.ISO_8859_1));
    m.setMetadata (md);

    final TCPayload p = new TCPayload ();
    p.setContentID ("cid1");
    p.setMimeType (CMimeType.APPLICATION_XML.getAsString ());
    p.setValue ("Hello World".getBytes (StandardCharsets.ISO_8859_1));
    m.addPayload (p);

    CommonsTestHelper.testDefaultImplementationWithEqualContentObject (m, m.clone ());

    if (false)
      LOGGER.info (TCRestJAXB.outgoingMessage ().getAsString (m));

    // Write
    final Document aDoc = TCRestJAXB.outgoingMessage ().getAsDocument (m);
    assertNotNull (aDoc);

    // Read
    final TCOutgoingMessage m2 = TCRestJAXB.outgoingMessage ().read (aDoc);
    assertNotNull (m2);

    CommonsTestHelper.testDefaultImplementationWithEqualContentObject (m, m2);

    // Read
    final TCOutgoingMessage m3 = TCRestJAXB.outgoingMessage ()
                                           .read (new FileSystemResource (new File ("src/test/resources/xml/jonas1.xml")));
    assertNotNull (m3);
  }

  @Test
  public void testIncoming ()
  {
    final TCIncomingMessage m = new TCIncomingMessage ();
    final TCIncomingMetadata md = new TCIncomingMetadata ();
    md.setSenderID (TCRestJAXB.createTCID (TCIdentifierFactory.PARTICIPANT_SCHEME, "9999:sender"));
    md.setReceiverID (TCRestJAXB.createTCID (TCIdentifierFactory.PARTICIPANT_SCHEME, "9999:receiver"));
    md.setDocTypeID (TCRestJAXB.createTCID (TCIdentifierFactory.DOCTYPE_SCHEME,
                                            "urn:eu:toop:ns:dataexchange-1p40::Request##urn:eu.toop.request.registeredorganization::1.40"));
    md.setProcessID (TCRestJAXB.createTCID (TCIdentifierFactory.DOCTYPE_SCHEME, "urn:eu.toop.process.datarequestresponse"));
    md.setPayloadType (TCPayloadType.REQUEST);
    m.setMetadata (md);

    final TCPayload p = new TCPayload ();
    p.setContentID ("cid1");
    p.setMimeType (CMimeType.APPLICATION_XML.getAsString ());
    p.setValue ("Hello World".getBytes (StandardCharsets.ISO_8859_1));
    m.addPayload (p);

    CommonsTestHelper.testDefaultImplementationWithEqualContentObject (m, m.clone ());

    if (false)
      LOGGER.info (TCRestJAXB.incomingMessage ().getAsString (m));

    // Write
    final Document aDoc = TCRestJAXB.incomingMessage ().getAsDocument (m);
    assertNotNull (aDoc);

    // Read
    final TCIncomingMessage m2 = TCRestJAXB.incomingMessage ().read (aDoc);
    assertNotNull (m2);

    CommonsTestHelper.testDefaultImplementationWithEqualContentObject (m, m2);
  }
}
