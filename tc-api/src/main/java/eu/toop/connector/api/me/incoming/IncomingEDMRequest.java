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

import javax.annotation.Nonnull;
import javax.annotation.concurrent.NotThreadSafe;

import com.helger.commons.ValueEnforcer;
import com.helger.commons.annotation.Nonempty;
import com.helger.commons.hashcode.HashCodeGenerator;
import com.helger.commons.string.ToStringGenerator;

import eu.toop.edm.EDMRequest;

/**
 * Incoming EDM request. Uses {@link EDMRequest} and
 * {@link IMEIncomingTransportMetadata} for the metadata.
 *
 * @author Philip Helger
 */
@NotThreadSafe
public class IncomingEDMRequest implements IIncomingEDMRequest
{
  private final EDMRequest m_aRequest;
  private final String m_sTopLevelContentID;
  private final IMEIncomingTransportMetadata m_aMetadata;

  public IncomingEDMRequest (@Nonnull final EDMRequest aRequest,
                             @Nonnull @Nonempty final String sTopLevelContentID,
                             @Nonnull final IMEIncomingTransportMetadata aMetadata)
  {
    ValueEnforcer.notNull (aRequest, "Request");
    ValueEnforcer.notEmpty (sTopLevelContentID, "TopLevelContentID");
    ValueEnforcer.notNull (aMetadata, "Metadata");
    m_aRequest = aRequest;
    m_sTopLevelContentID = sTopLevelContentID;
    m_aMetadata = aMetadata;
  }

  /**
   * @return The EDM request that contains the main payload. Never
   *         <code>null</code>.
   */
  @Nonnull
  public EDMRequest getRequest ()
  {
    return m_aRequest;
  }

  @Nonnull
  @Nonempty
  public String getTopLevelContentID ()
  {
    return m_sTopLevelContentID;
  }

  @Nonnull
  public IMEIncomingTransportMetadata getMetadata ()
  {
    return m_aMetadata;
  }

  @Override
  public boolean equals (final Object o)
  {
    if (o == this)
      return true;
    if (o == null || !getClass ().equals (o.getClass ()))
      return false;

    final IncomingEDMRequest rhs = (IncomingEDMRequest) o;
    return m_aRequest.equals (rhs.m_aRequest) && m_aMetadata.equals (rhs.m_aMetadata);
  }

  @Override
  public int hashCode ()
  {
    return new HashCodeGenerator (this).append (m_aRequest).append (m_aMetadata).getHashCode ();
  }

  @Override
  public String toString ()
  {
    return new ToStringGenerator (this).append ("Request", m_aRequest)
                                       .append ("TopLevelContentID", m_sTopLevelContentID)
                                       .append ("Metadata", m_aMetadata)
                                       .getToString ();
  }
}
