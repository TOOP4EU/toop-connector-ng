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

import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.NotThreadSafe;

import com.helger.commons.ValueEnforcer;
import com.helger.commons.annotation.Nonempty;
import com.helger.commons.annotation.ReturnsMutableCopy;
import com.helger.commons.annotation.ReturnsMutableObject;
import com.helger.commons.collection.impl.CommonsLinkedHashMap;
import com.helger.commons.collection.impl.ICommonsOrderedMap;
import com.helger.commons.hashcode.HashCodeGenerator;
import com.helger.commons.string.ToStringGenerator;

import eu.toop.connector.api.me.model.MEPayload;
import eu.toop.edm.EDMResponse;

/**
 * Incoming EDM response. Uses {@link EDMResponse}, optional attachments and
 * {@link IMEIncomingTransportMetadata} for the metadata.
 *
 * @author Philip Helger
 */
@NotThreadSafe
public class IncomingEDMResponse implements IIncomingEDMResponse
{
  private final EDMResponse m_aResponse;
  private final String m_sTopLevelContentID;
  private final ICommonsOrderedMap <String, MEPayload> m_aAttachments = new CommonsLinkedHashMap <> ();
  private final IMEIncomingTransportMetadata m_aMetadata;

  public IncomingEDMResponse (@Nonnull final EDMResponse aResponse,
                              @Nonnull @Nonempty final String sTopLevelContentID,
                              @Nullable final List <MEPayload> aAttachments,
                              @Nonnull final IMEIncomingTransportMetadata aMetadata)
  {
    ValueEnforcer.notNull (aResponse, "Response");
    ValueEnforcer.notEmpty (sTopLevelContentID, "TopLevelContentID");
    ValueEnforcer.notNull (aMetadata, "Metadata");

    m_aResponse = aResponse;
    m_sTopLevelContentID = sTopLevelContentID;
    if (aAttachments != null)
      for (final MEPayload aItem : aAttachments)
        m_aAttachments.put (aItem.getContentID (), aItem);
    m_aMetadata = aMetadata;
  }

  /**
   * @return The EDM response that contains the main payload. Never
   *         <code>null</code>.
   */
  @Nonnull
  public EDMResponse getResponse ()
  {
    return m_aResponse;
  }

  @Nonnull
  @Nonempty
  public String getTopLevelContentID ()
  {
    return m_sTopLevelContentID;
  }

  /**
   * @return The mutable map of all attachments that are part of the response.
   *         Never <code>null</code> but maybe empty. Handle with care.
   */
  @Nonnull
  @ReturnsMutableObject
  public ICommonsOrderedMap <String, MEPayload> attachments ()
  {
    return m_aAttachments;
  }

  /**
   * @return A copy of all attachments that are part of the response. Never
   *         <code>null</code> but maybe empty.
   */
  @Nonnull
  @ReturnsMutableCopy
  public ICommonsOrderedMap <String, MEPayload> getAllAttachments ()
  {
    return m_aAttachments.getClone ();
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

    final IncomingEDMResponse rhs = (IncomingEDMResponse) o;
    return m_aResponse.equals (rhs.m_aResponse) &&
           m_aAttachments.equals (rhs.m_aAttachments) &&
           m_aMetadata.equals (rhs.m_aMetadata);
  }

  @Override
  public int hashCode ()
  {
    return new HashCodeGenerator (this).append (m_aResponse)
                                       .append (m_aAttachments)
                                       .append (m_aMetadata)
                                       .getHashCode ();
  }

  @Override
  public String toString ()
  {
    return new ToStringGenerator (this).append ("Response", m_aResponse)
                                       .append ("TopLevelContentID", m_sTopLevelContentID)
                                       .append ("Attachments", m_aAttachments)
                                       .append ("Metadata", m_aMetadata)
                                       .getToString ();
  }
}
