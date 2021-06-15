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

import java.util.function.Predicate;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.helger.commons.error.IError;
import com.helger.commons.error.SingleError;
import com.helger.commons.error.level.EErrorLevel;
import com.helger.commons.error.list.ErrorList;

import eu.toop.edm.error.IToopErrorCode;

/**
 * Collecting implementation of {@link ITCErrorHandler}
 *
 * @author Philip Helger
 * @since 2.0.0-rc1
 */
public class WrappedTCErrorHandler implements ITCErrorHandler
{
  private final ErrorList m_aErrorList;
  private final Predicate <? super IError> m_aFilter;

  /**
   * Constructor collecting all errors.
   *
   * @param aErrorList
   *        The error list to be filled. May not be <code>null</code>.
   */
  public WrappedTCErrorHandler (@Nonnull final ErrorList aErrorList)
  {
    this (aErrorList, null);
  }

  /**
   * Constructor collecting all errors matching the provided filter.
   *
   * @param aErrorList
   *        The error list to be filled. May not be <code>null</code>.
   * @param aFilter
   *        The filter to be used. May be <code>null</code> to collect all
   *        errors.
   */
  public WrappedTCErrorHandler (@Nonnull final ErrorList aErrorList, @Nullable final Predicate <? super IError> aFilter)
  {
    m_aErrorList = aErrorList;
    m_aFilter = aFilter;
  }

  public void onMessage (@Nonnull final EErrorLevel eErrorLevel,
                         @Nonnull final String sMsg,
                         @Nullable final Throwable t,
                         @Nonnull final IToopErrorCode eCode)
  {
    final IError aError = SingleError.builder ()
                                     .errorLevel (eErrorLevel)
                                     .errorText (sMsg)
                                     .errorID (eCode.getID ())
                                     .linkedException (t)
                                     .build ();
    if (m_aFilter == null || m_aFilter.test (aError))
      m_aErrorList.add (aError);
  }
}
