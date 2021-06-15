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
package eu.toop.connector.api.dsd;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.helger.commons.collection.impl.ICommonsSet;
import com.helger.peppolid.IDocumentTypeIdentifier;
import com.helger.peppolid.IParticipantIdentifier;

import eu.toop.connector.api.error.ITCErrorHandler;

/**
 * Interface to resolve Country Code and Document Type ID to a set of matching
 * participant identifiers.<br>
 * Am implementation of this class e.g. queries the TOOP Directory for the
 * respective participants.
 *
 * @author Philip Helger
 */
public interface IDSDParticipantIDProvider
{
  /**
   * Get all participant IDs that match the provided country code and document
   * type ID.
   *
   * @param sLogPrefix
   *        The logging prefix to be used. May not be <code>null</code>.
   * @param sDatasetType
   *        Dataset Type to query. May not be <code>null</code>.
   * @param sCountryCode
   *        Country code to use. Must be a 2-digit string. May be
   *        <code>null</code>.
   * @param aDocumentTypeID
   *        Document type ID to query. May not be <code>null</code>.
   * @param aErrorHandler
   *        The error handler to be used. May not be <code>null</code>.
   * @return A non-<code>null</code> but maybe empty set of Participant IDs.
   */
  @Nonnull
  ICommonsSet <IParticipantIdentifier> getAllParticipantIDs (@Nonnull String sLogPrefix,
                                                             @Nonnull String sDatasetType,
                                                             @Nullable String sCountryCode,
                                                             @Nonnull IDocumentTypeIdentifier aDocumentTypeID,
                                                             @Nonnull ITCErrorHandler aErrorHandler);
}
