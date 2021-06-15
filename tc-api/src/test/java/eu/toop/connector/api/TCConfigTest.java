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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

/**
 * Test class for class {@link TCConfig}.
 *
 * @author Philip Helger
 */
public final class TCConfigTest
{
  @Test
  public void testBasic ()
  {
    assertEquals ("http://directory.central.toop/pd", TCConfig.DSD.getDSDBaseUrl ());
    assertFalse (TCConfig.R2D2.isR2D2UseDNS ());
    assertNotNull (TCConfig.getIdentifierFactory ());
  }
}
