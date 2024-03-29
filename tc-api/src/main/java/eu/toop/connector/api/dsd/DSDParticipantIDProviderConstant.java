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
package eu.toop.connector.api.dsd;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.helger.commons.ValueEnforcer;
import com.helger.commons.annotation.ReturnsMutableCopy;
import com.helger.commons.collection.impl.CommonsLinkedHashSet;
import com.helger.commons.collection.impl.ICommonsOrderedSet;
import com.helger.commons.string.ToStringGenerator;
import com.helger.peppolid.IDocumentTypeIdentifier;
import com.helger.peppolid.IParticipantIdentifier;

import eu.toop.connector.api.error.ITCErrorHandler;

/**
 * This class implements the {@link IDSDParticipantIDProvider} interface using a
 * constant set of participant identifiers. This implementation is meant for
 * testing purposes only. Don't use in production.
 *
 * @author Philip Helger
 */
public class DSDParticipantIDProviderConstant implements IDSDParticipantIDProvider
{
  private final ICommonsOrderedSet <IParticipantIdentifier> m_aSet = new CommonsLinkedHashSet <> ();

  /**
   * Constructor to return an empty participant identifier set.
   */
  public DSDParticipantIDProviderConstant ()
  {}

  /**
   * Constructor with a single participant ID.
   *
   * @param aPI
   *        The participant ID to return. May not be <code>null</code>.
   */
  public DSDParticipantIDProviderConstant (@Nonnull final IParticipantIdentifier aPI)
  {
    ValueEnforcer.notNull (aPI, "ParticipantID");
    m_aSet.add (aPI);
  }

  /**
   * Constructor with a collection of participant IDs
   *
   * @param aPIs
   *        The participant IDs to be returned. May not be <code>null</code> and
   *        may not contain <code>null</code> values.
   */
  public DSDParticipantIDProviderConstant (@Nonnull final Iterable <? extends IParticipantIdentifier> aPIs)
  {
    ValueEnforcer.notNullNoNullValue (aPIs, "ParticipantIDs");
    m_aSet.addAll (aPIs);
  }

  /**
   * Constructor with an array of participant IDs
   *
   * @param aPIs
   *        The participant IDs to be returned. May not be <code>null</code> and
   *        may not contain <code>null</code> values.
   */
  public DSDParticipantIDProviderConstant (@Nonnull final IParticipantIdentifier... aPIs)
  {
    ValueEnforcer.notNullNoNullValue (aPIs, "ParticipantIDs");
    m_aSet.addAll (aPIs);
  }

  @Nonnull
  @ReturnsMutableCopy
  public ICommonsOrderedSet <IParticipantIdentifier> getAllParticipantIDs (@Nonnull final String sLogPrefix,
                                                                           @Nonnull final String sDatasetType,
                                                                           @Nullable final String sCountryCode,
                                                                           @Nonnull final IDocumentTypeIdentifier aDocumentTypeID,
                                                                           @Nonnull final ITCErrorHandler aErrorHandler)
  {
    return m_aSet.getClone ();
  }

  @Override
  public String toString ()
  {
    return new ToStringGenerator (this).append ("ParticipantIDs", m_aSet).getToString ();
  }
}
