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
package eu.toop.connector.api.me.incoming;

import org.junit.Test;

import com.helger.commons.mock.CommonsTestHelper;
import com.helger.peppolid.factory.IIdentifierFactory;

import eu.toop.connector.api.TCConfig;

/**
 * Test class for class {@link MEIncomingTransportMetadata}.
 *
 * @author Philip Helger
 */
public final class MEIncomingTransportMetadataTest
{
  @Test
  public void testEqualsHashcode ()
  {
    final IIdentifierFactory aIF = TCConfig.getIdentifierFactory ();

    MEIncomingTransportMetadata m = new MEIncomingTransportMetadata (null, null, null, null);
    CommonsTestHelper.testDefaultImplementationWithEqualContentObject (m, new MEIncomingTransportMetadata (null, null, null, null));

    m = new MEIncomingTransportMetadata (aIF.createParticipantIdentifierWithDefaultScheme ("bla"), null, null, null);
    CommonsTestHelper.testDefaultImplementationWithEqualContentObject (m,
                                                                       new MEIncomingTransportMetadata (aIF.createParticipantIdentifierWithDefaultScheme ("bla"),
                                                                                                        null,
                                                                                                        null,
                                                                                                        null));

    m = new MEIncomingTransportMetadata (null, aIF.createParticipantIdentifierWithDefaultScheme ("bla"), null, null);
    CommonsTestHelper.testDefaultImplementationWithEqualContentObject (m,
                                                                       new MEIncomingTransportMetadata (null,
                                                                                                        aIF.createParticipantIdentifierWithDefaultScheme ("bla"),
                                                                                                        null,
                                                                                                        null));

    m = new MEIncomingTransportMetadata (null, null, aIF.createDocumentTypeIdentifierWithDefaultScheme ("foo"), null);
    CommonsTestHelper.testDefaultImplementationWithEqualContentObject (m,
                                                                       new MEIncomingTransportMetadata (null,
                                                                                                        null,
                                                                                                        aIF.createDocumentTypeIdentifierWithDefaultScheme ("foo"),
                                                                                                        null));

    m = new MEIncomingTransportMetadata (null, null, null, aIF.createProcessIdentifierWithDefaultScheme ("proc"));
    CommonsTestHelper.testDefaultImplementationWithEqualContentObject (m,
                                                                       new MEIncomingTransportMetadata (null,
                                                                                                        null,
                                                                                                        null,
                                                                                                        aIF.createProcessIdentifierWithDefaultScheme ("proc")));

    m = new MEIncomingTransportMetadata (aIF.createParticipantIdentifierWithDefaultScheme ("bla"),
                                         aIF.createParticipantIdentifierWithDefaultScheme ("bla2"),
                                         aIF.createDocumentTypeIdentifierWithDefaultScheme ("foo"),
                                         aIF.createProcessIdentifierWithDefaultScheme ("proc"));
    CommonsTestHelper.testDefaultImplementationWithEqualContentObject (m,
                                                                       new MEIncomingTransportMetadata (aIF.createParticipantIdentifierWithDefaultScheme ("bla"),
                                                                                                        aIF.createParticipantIdentifierWithDefaultScheme ("bla2"),
                                                                                                        aIF.createDocumentTypeIdentifierWithDefaultScheme ("foo"),
                                                                                                        aIF.createProcessIdentifierWithDefaultScheme ("proc")));
  }
}
