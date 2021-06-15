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
import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;

import com.helger.commons.equals.EqualsHelper;
import com.helger.commons.hashcode.HashCodeGenerator;
import com.helger.commons.string.ToStringGenerator;
import com.helger.peppolid.IDocumentTypeIdentifier;
import com.helger.peppolid.IParticipantIdentifier;
import com.helger.peppolid.IProcessIdentifier;
import com.helger.peppolid.factory.IIdentifierFactory;

import eu.toop.connector.api.TCConfig;
import eu.toop.connector.api.rest.TCIncomingMetadata;

/**
 * Container for all relevant AS4 transport metadata that may be interesting to
 * the recipient.
 *
 * @author Philip Helger
 */
@Immutable
public class MEIncomingTransportMetadata implements IMEIncomingTransportMetadata
{
  private final IParticipantIdentifier m_aSenderID;
  private final IParticipantIdentifier m_aReceiverID;
  private final IDocumentTypeIdentifier m_aDocTypeID;
  private final IProcessIdentifier m_aProcessID;

  public MEIncomingTransportMetadata (@Nullable final IParticipantIdentifier aSenderID,
                                      @Nullable final IParticipantIdentifier aReceiverID,
                                      @Nullable final IDocumentTypeIdentifier aDocTypeID,
                                      @Nullable final IProcessIdentifier aProcessID)
  {
    m_aSenderID = aSenderID;
    m_aReceiverID = aReceiverID;
    m_aDocTypeID = aDocTypeID;
    m_aProcessID = aProcessID;
  }

  @Nullable
  public IParticipantIdentifier getSenderID ()
  {
    return m_aSenderID;
  }

  @Nullable
  public IParticipantIdentifier getReceiverID ()
  {
    return m_aReceiverID;
  }

  @Nullable
  public IDocumentTypeIdentifier getDocumentTypeID ()
  {
    return m_aDocTypeID;
  }

  @Nullable
  public IProcessIdentifier getProcessID ()
  {
    return m_aProcessID;
  }

  @Override
  public boolean equals (final Object o)
  {
    if (o == this)
      return true;
    if (o == null || !getClass ().equals (o.getClass ()))
      return false;

    final MEIncomingTransportMetadata rhs = (MEIncomingTransportMetadata) o;
    return EqualsHelper.equalsCustom (m_aSenderID, rhs.m_aSenderID, IParticipantIdentifier::hasSameContent) &&
           EqualsHelper.equalsCustom (m_aReceiverID, rhs.m_aReceiverID, IParticipantIdentifier::hasSameContent) &&
           EqualsHelper.equalsCustom (m_aDocTypeID, rhs.m_aDocTypeID, IDocumentTypeIdentifier::hasSameContent) &&
           EqualsHelper.equalsCustom (m_aProcessID, rhs.m_aProcessID, IProcessIdentifier::hasSameContent);
  }

  @Override
  public int hashCode ()
  {
    return new HashCodeGenerator (this).append (m_aSenderID)
                                       .append (m_aReceiverID)
                                       .append (m_aDocTypeID)
                                       .append (m_aProcessID)
                                       .getHashCode ();
  }

  @Override
  public String toString ()
  {
    return new ToStringGenerator (this).append ("SenderID", m_aSenderID)
                                       .append ("ReceiverID", m_aReceiverID)
                                       .append ("DocumentTypeID", m_aDocTypeID)
                                       .append ("ProcessID", m_aProcessID)
                                       .getToString ();
  }

  @Nonnull
  public static MEIncomingTransportMetadata create (@Nonnull final TCIncomingMetadata aIM)
  {
    final IIdentifierFactory aIF = TCConfig.getIdentifierFactory ();
    final IParticipantIdentifier aSenderID = aIF.createParticipantIdentifier (aIM.getSenderID ().getScheme (),
                                                                              aIM.getSenderID ().getValue ());
    final IParticipantIdentifier aReceiverID = aIF.createParticipantIdentifier (aIM.getReceiverID ().getScheme (),
                                                                                aIM.getReceiverID ().getValue ());
    final IDocumentTypeIdentifier aDocTypeID = aIF.createDocumentTypeIdentifier (aIM.getDocTypeID ().getScheme (),
                                                                                 aIM.getDocTypeID ().getValue ());
    final IProcessIdentifier aProcessID = aIF.createProcessIdentifier (aIM.getProcessID ().getScheme (), aIM.getProcessID ().getValue ());
    return new MEIncomingTransportMetadata (aSenderID, aReceiverID, aDocTypeID, aProcessID);
  }
}
