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
