/**
 * Copyright 2021 - TOOP Project
 *
 * This file and its contents are licensed under the EUPL, Version 1.2
 * or – as soon they will be approved by the European Commission – subsequent
 * versions of the EUPL (the "Licence");
 *
 * You may not use this work except in compliance with the Licence.
 * You may obtain a copy of the Licence at:
 *
 *       https://joinup.ec.europa.eu/collection/eupl/eupl-text-eupl-12
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the Licence is distributed on an "AS IS" basis,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *
 * See the Licence for the specific language governing permissions and limitations under the Licence.
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
