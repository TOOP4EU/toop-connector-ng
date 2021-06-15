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
package eu.toop.connector.api.validation;

import java.util.Locale;

import javax.annotation.Nonnull;

import com.helger.phive.api.executorset.VESID;
import com.helger.phive.api.result.ValidationResultList;

/**
 * TC Validation Service
 *
 * @author Philip Helger
 */
public interface IVSValidator
{
  /**
   * Perform validation
   *
   * @param aVESID
   *        VESID to use.
   * @param aPayload
   *        Payload to validate.
   * @param aDisplayLocale
   *        Display locale for the error message.
   * @return A non-<code>null</code> result list.
   */
  @Nonnull
  ValidationResultList validate (@Nonnull VESID aVESID, @Nonnull byte [] aPayload, @Nonnull Locale aDisplayLocale);
}
