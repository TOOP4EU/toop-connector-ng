/**
 * This work is protected under copyrights held by the members of the
 * TOOP Project Consortium as indicated at
 * http://wiki.ds.unipi.gr/display/TOOP/Contributors
 * (c) 2018-2021. All rights reserved.
 *
 * This work is licensed under the EUPL 1.2.
 *
 *  = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = =
 *
 * Licensed under the EUPL, Version 1.2 or – as soon they will be approved
 * by the European Commission - subsequent versions of the EUPL
 * (the "Licence");
 * You may not use this work except in compliance with the Licence.
 * You may obtain a copy of the Licence at:
 *
 *         https://joinup.ec.europa.eu/software/page/eupl
 */
package eu.toop.connector.api.me.incoming;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.NotThreadSafe;

import com.helger.commons.ValueEnforcer;
import com.helger.commons.annotation.Nonempty;
import com.helger.commons.hashcode.HashCodeGenerator;
import com.helger.commons.string.ToStringGenerator;

import eu.toop.edm.EDMErrorResponse;

/**
 * Incoming EDM error response. Uses {@link EDMErrorResponse} and
 * {@link IMEIncomingTransportMetadata} for the metadata.
 *
 * @author Philip Helger
 */
@NotThreadSafe
public class IncomingEDMErrorResponse implements IIncomingEDMResponse
{
  private final EDMErrorResponse m_aErrorResponse;
  private final String m_sTopLevelContentID;
  private final IMEIncomingTransportMetadata m_aMetadata;

  public IncomingEDMErrorResponse (@Nonnull final EDMErrorResponse aErrorResponse,
                                   @Nonnull @Nonempty final String sTopLevelContentID,
                                   @Nonnull final IMEIncomingTransportMetadata aMetadata)
  {
    ValueEnforcer.notNull (aErrorResponse, "ErrorResponse");
    ValueEnforcer.notEmpty (sTopLevelContentID, "TopLevelContentID");
    ValueEnforcer.notNull (aMetadata, "Metadata");
    m_aErrorResponse = aErrorResponse;
    m_sTopLevelContentID = sTopLevelContentID;
    m_aMetadata = aMetadata;
  }

  /**
   * @return The EDM error response that contains the main payload. Never
   *         <code>null</code>.
   */
  @Nonnull
  public EDMErrorResponse getErrorResponse ()
  {
    return m_aErrorResponse;
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

    final IncomingEDMErrorResponse rhs = (IncomingEDMErrorResponse) o;
    return m_aErrorResponse.equals (rhs.m_aErrorResponse) && m_aMetadata.equals (rhs.m_aMetadata);
  }

  @Override
  public int hashCode ()
  {
    return new HashCodeGenerator (this).append (m_aErrorResponse).append (m_aMetadata).getHashCode ();
  }

  @Override
  public String toString ()
  {
    return new ToStringGenerator (this).append ("ErrorResponse", m_aErrorResponse)
                                       .append ("TopLevelContentID", m_sTopLevelContentID)
                                       .append ("Metadata", m_aMetadata)
                                       .getToString ();
  }
}
