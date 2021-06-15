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
package eu.toop.connector.app.dsd;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import com.helger.commons.collection.impl.ICommonsSet;

import eu.toop.connector.api.dsd.DSDDatasetResponse;
import eu.toop.connector.api.error.LoggingTCErrorHandler;

/**
 * Test class of class {@link DSDDatasetResponseProviderRemote}.
 * 
 * @author Philip Helger
 */
public final class DSDDatasetResponseProviderRemoteTest
{
  @Test
  public void testSimple ()
  {
    final ICommonsSet <DSDDatasetResponse> aResp = new DSDDatasetResponseProviderRemote ().getAllDatasetResponsesByCountry ("test",
                                                                                                                   "REGISTERED_ORGANIZATION_TYPE",
                                                                                                                   null,
                                                                                                                   LoggingTCErrorHandler.INSTANCE);
    assertNotNull (aResp);
  }
}
