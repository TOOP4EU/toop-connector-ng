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
package eu.toop.connector.api;

import static org.junit.Assert.assertNotEquals;

import org.junit.Test;

import com.helger.peppolid.IParticipantIdentifier;

/**
 * Test class for class {@link TCIdentifierFactory}.
 *
 * @author Philip Helger
 */
public final class TCIdentifierFactoryTest
{
  @Test
  public void testBasic ()
  {
    final TCIdentifierFactory aIF = TCIdentifierFactory.INSTANCE_TC;

    final IParticipantIdentifier aPI1 = aIF.createParticipantIdentifier (null, "iso6523-actorid-upis::9999:elonia");
    final IParticipantIdentifier aPI2 = aIF.createParticipantIdentifier ("iso6523-actorid-upis", "9999:elonia");
    assertNotEquals (aPI1.getURIEncoded (), aPI2.getURIEncoded ());
  }
}
