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

import java.security.cert.X509Certificate;

import javax.annotation.Nonnull;

import com.helger.commons.annotation.Nonempty;
import com.helger.peppolid.IDocumentTypeIdentifier;
import com.helger.peppolid.IParticipantIdentifier;
import com.helger.peppolid.IProcessIdentifier;

/**
 * Message Exchange Routing Information.
 *
 * @author Philip Helger
 */
public interface IMERoutingInformation
{
  /**
   * @return Sender participant ID. Never <code>null</code>.
   */
  @Nonnull
  IParticipantIdentifier getSenderID ();

  /**
   * @return Receiver participant ID. Never <code>null</code>.
   */
  @Nonnull
  IParticipantIdentifier getReceiverID ();

  /**
   * @return Document type ID. Never <code>null</code>.
   */
  @Nonnull
  IDocumentTypeIdentifier getDocumentTypeID ();

  /**
   * @return Process ID. Never <code>null</code>.
   */
  @Nonnull
  IProcessIdentifier getProcessID ();

  /**
   * @return The transport profile ID from the constructor. Neither
   *         <code>null</code> nor empty.
   */
  @Nonnull
  @Nonempty
  String getTransportProtocol ();

  /**
   * @return The endpoint URL from the SMP lookup. Neither <code>null</code> nor
   *         empty.
   */
  @Nonnull
  @Nonempty
  String getEndpointURL ();

  /**
   * @return The encoded certificate from the SMP look up. May not be
   *         <code>null</code>.
   */
  @Nonnull
  X509Certificate getCertificate ();
}
