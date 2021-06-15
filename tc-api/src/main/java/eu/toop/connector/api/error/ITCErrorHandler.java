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
package eu.toop.connector.api.error;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.helger.commons.error.level.EErrorLevel;

import eu.toop.edm.error.IToopErrorCode;

/**
 * Custom TOOP Connector error handler callback
 *
 * @author Philip Helger
 */
public interface ITCErrorHandler
{
  /**
   * The main error handler method to be implemented
   *
   * @param eErrorLevel
   *        Error level. Never <code>null</code>.
   * @param sMsg
   *        Error text. Never <code>null</code>.
   * @param t
   *        Optional exception. May be <code>null</code>.
   * @param eCode
   *        The TOOP specific error code. Never <code>null</code>.
   */
  void onMessage (@Nonnull EErrorLevel eErrorLevel, @Nonnull String sMsg, @Nullable Throwable t, @Nonnull IToopErrorCode eCode);

  default void onWarning (@Nonnull final String sMsg, @Nonnull final IToopErrorCode eCode)
  {
    onMessage (EErrorLevel.WARN, sMsg, null, eCode);
  }

  default void onWarning (@Nonnull final String sMsg, @Nullable final Throwable t, @Nonnull final IToopErrorCode eCode)
  {
    onMessage (EErrorLevel.WARN, sMsg, t, eCode);
  }

  default void onError (@Nonnull final String sMsg, @Nonnull final IToopErrorCode eCode)
  {
    onMessage (EErrorLevel.ERROR, sMsg, null, eCode);
  }

  default void onError (@Nonnull final String sMsg, @Nullable final Throwable t, @Nonnull final IToopErrorCode eCode)
  {
    onMessage (EErrorLevel.ERROR, sMsg, t, eCode);
  }
}
