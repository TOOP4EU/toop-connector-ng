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
package eu.toop.connector.api.dd;

import javax.annotation.Nonnull;

import com.helger.commons.collection.impl.ICommonsSortedMap;
import com.helger.peppolid.IParticipantIdentifier;

import eu.toop.connector.api.error.ITCErrorHandler;

/**
 * Helper interface to be used by the REST API.
 *
 * @author Philip Helger
 */
public interface IDDServiceGroupHrefProvider
{
  /**
   * @param aParticipantID
   *        Participant ID to query.
   * @param aErrorHandler
   *        The error handler to be used. May not be <code>null</code>.
   * @return A non-<code>null</code> sorted map of all hrefs. The key MUST be
   *         URL decoded whereas the value is the "original href" as found in
   *         the response.
   */
  @Nonnull
  ICommonsSortedMap <String, String> getAllServiceGroupHrefs (@Nonnull IParticipantIdentifier aParticipantID,
                                                              @Nonnull ITCErrorHandler aErrorHandler);
}
