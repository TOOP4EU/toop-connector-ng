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
package eu.toop.connector.api.dsd;

import javax.annotation.Nonnull;

import com.helger.commons.annotation.Nonempty;
import com.helger.commons.collection.impl.ICommonsSet;

import eu.toop.connector.api.error.ITCErrorHandler;

/**
 * Interface to resolve Country Code and Dataset Type ID to a set of matching
 * DSD Dataset Responses.<br>
 * Am implementation of this class e.g. queries the DSD for the respective
 * participants.
 *
 * @author jerouris
 * @author Philip Helger
 */
public interface IDSDDatasetResponseProvider
{
  /**
   * Get all DSD Responses that match the provided country code and document
   * type ID.
   *
   * @param sLogPrefix
   *        The logging prefix to be used. May not be <code>null</code>.
   * @param sDatasetType
   *        Dataset Type to query. May neither be <code>null</code> nor empty.
   * @param sCountryCode
   *        Country code to use. Must be a 2-digit string. May neither be
   *        <code>null</code> nor empty.
   * @param aErrorHdl
   *        The error handler to be used. May not be <code>null</code>.
   * @return A non-<code>null</code> but maybe empty set of datasets.
   * @since 2.1.0
   */
  @Nonnull
  ICommonsSet <DSDDatasetResponse> getAllDatasetResponsesByCountry (@Nonnull String sLogPrefix,
                                                                    @Nonnull @Nonempty String sDatasetType,
                                                                    @Nonnull @Nonempty String sCountryCode,
                                                                    @Nonnull ITCErrorHandler aErrorHdl);

  /**
   * Get all DSD Responses that match the provided DP type and document type ID.
   *
   * @param sLogPrefix
   *        The logging prefix to be used. May not be <code>null</code>.
   * @param sDatasetType
   *        Dataset Type to query. May neither be <code>null</code> nor empty.
   * @param sDPType
   *        DP type to use. This is mapped to the Business Card of the
   *        participant. May neither be <code>null</code> nor empty.
   * @param aErrorHdl
   *        The error handler to be used. May not be <code>null</code>.
   * @return A non-<code>null</code> but maybe empty set of datasets.
   * @since 2.1.0
   */
  @Nonnull
  ICommonsSet <DSDDatasetResponse> getAllDatasetResponsesByDPType (@Nonnull String sLogPrefix,
                                                                   @Nonnull @Nonempty String sDatasetType,
                                                                   @Nonnull @Nonempty String sDPType,
                                                                   @Nonnull ITCErrorHandler aErrorHdl);
}
