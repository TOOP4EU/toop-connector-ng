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
package eu.toop.connector.api.me.outgoing;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import eu.toop.connector.api.me.MEException;
import eu.toop.edm.error.IToopErrorCode;

/**
 * Exception when sending messages via ME
 *
 * @author Philip Helger
 */
public class MEOutgoingException extends MEException
{
  private final IToopErrorCode m_aErrorCode;

  protected MEOutgoingException (@Nullable final String sMsg, @Nullable final Throwable aCause, @Nullable final IToopErrorCode aErrorCode)
  {
    super (sMsg, aCause);
    m_aErrorCode = aErrorCode;
  }

  public MEOutgoingException (@Nullable final String sMsg)
  {
    this (sMsg, null, null);
  }

  public MEOutgoingException (@Nullable final String sMsg, @Nullable final Throwable aCause)
  {
    this (sMsg, aCause, null);
  }

  public MEOutgoingException (@Nonnull final IToopErrorCode aErrorCode, @Nullable final Throwable aCause)
  {
    this ("TOOP Error " + aErrorCode.getID (), aCause, aErrorCode);
  }

  public MEOutgoingException (@Nonnull final IToopErrorCode aErrorCode, @Nullable final String sMsg)
  {
    this ("TOOP Error " + aErrorCode.getID () + " - " + sMsg, null, aErrorCode);
  }

  @Nullable
  public final IToopErrorCode getToopErrorCode ()
  {
    return m_aErrorCode;
  }
}
