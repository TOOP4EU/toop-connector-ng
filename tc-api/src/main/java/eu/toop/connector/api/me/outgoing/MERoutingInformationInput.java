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

import com.helger.commons.ValueEnforcer;
import com.helger.commons.annotation.Nonempty;
import com.helger.commons.string.ToStringGenerator;
import com.helger.peppolid.IDocumentTypeIdentifier;
import com.helger.peppolid.IParticipantIdentifier;
import com.helger.peppolid.IProcessIdentifier;
import com.helger.peppolid.factory.IIdentifierFactory;

import eu.toop.connector.api.TCConfig;
import eu.toop.connector.api.rest.TCOutgoingMetadata;

/**
 * Base class for {@link MERoutingInformation}
 *
 * @author Philip Helger
 */
public class MERoutingInformationInput
{
  private final IParticipantIdentifier m_aSenderID;
  private final IParticipantIdentifier m_aReceiverID;
  private final IDocumentTypeIdentifier m_aDocTypeID;
  private final IProcessIdentifier m_aProcessID;
  private final String m_sTransportProtocol;

  public MERoutingInformationInput (@Nonnull final IParticipantIdentifier aSenderID,
                                    @Nonnull final IParticipantIdentifier aReceiverID,
                                    @Nonnull final IDocumentTypeIdentifier aDocTypeID,
                                    @Nonnull final IProcessIdentifier aProcessID,
                                    @Nonnull @Nonempty final String sTransportProtocol)
  {
    ValueEnforcer.notNull (aSenderID, "SenderID");
    ValueEnforcer.notNull (aReceiverID, "ReceiverID");
    ValueEnforcer.notNull (aDocTypeID, "DocTypeID");
    ValueEnforcer.notNull (aProcessID, "ProcessID");
    ValueEnforcer.notEmpty (sTransportProtocol, "TransportProtocol");

    m_aSenderID = aSenderID;
    m_aReceiverID = aReceiverID;
    m_aDocTypeID = aDocTypeID;
    m_aProcessID = aProcessID;
    m_sTransportProtocol = sTransportProtocol;
  }

  @Nonnull
  public final IParticipantIdentifier getSenderID ()
  {
    return m_aSenderID;
  }

  @Nonnull
  public final IParticipantIdentifier getReceiverID ()
  {
    return m_aReceiverID;
  }

  @Nonnull
  public final IDocumentTypeIdentifier getDocumentTypeID ()
  {
    return m_aDocTypeID;
  }

  @Nonnull
  public final IProcessIdentifier getProcessID ()
  {
    return m_aProcessID;
  }

  @Nonnull
  @Nonempty
  public final String getTransportProtocol ()
  {
    return m_sTransportProtocol;
  }

  @Override
  public String toString ()
  {
    return new ToStringGenerator (this).append ("SenderID", m_aSenderID)
                                       .append ("ReceiverID", m_aReceiverID)
                                       .append ("DocTypeID", m_aDocTypeID)
                                       .append ("ProcID", m_aProcessID)
                                       .append ("TransportProtocol", m_sTransportProtocol)
                                       .getToString ();
  }

  @Nonnull
  public static MERoutingInformationInput createForInput (@Nonnull final TCOutgoingMetadata aMetadata)
  {
    ValueEnforcer.notNull (aMetadata, "Metadata");
    final IIdentifierFactory aIF = TCConfig.getIdentifierFactory ();
    return new MERoutingInformationInput (aIF.createParticipantIdentifier (aMetadata.getSenderID ().getScheme (),
                                                                           aMetadata.getSenderID ().getValue ()),
                                          aIF.createParticipantIdentifier (aMetadata.getReceiverID ().getScheme (),
                                                                           aMetadata.getReceiverID ().getValue ()),
                                          aIF.createDocumentTypeIdentifier (aMetadata.getDocTypeID ().getScheme (),
                                                                            aMetadata.getDocTypeID ().getValue ()),
                                          aIF.createProcessIdentifier (aMetadata.getProcessID ().getScheme (),
                                                                       aMetadata.getProcessID ().getValue ()),
                                          aMetadata.getTransportProtocol ());
  }
}
