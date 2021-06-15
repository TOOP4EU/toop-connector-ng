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
package eu.toop.connector.api.dd;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.helger.commons.collection.CollectionHelper;
import com.helger.peppolid.IDocumentTypeIdentifier;
import com.helger.peppolid.IParticipantIdentifier;
import com.helger.peppolid.IProcessIdentifier;
import com.helger.peppolid.simple.process.SimpleProcessIdentifier;
import com.helger.xsds.bdxr.smp1.EndpointType;
import com.helger.xsds.bdxr.smp1.ProcessType;
import com.helger.xsds.bdxr.smp1.ServiceInformationType;
import com.helger.xsds.bdxr.smp1.ServiceMetadataType;

/**
 * Helper interface to be used by the REST API.
 *
 * @author Philip Helger
 */
public interface IDDServiceMetadataProvider
{
  /**
   * Find the service metadata
   *
   * @param aParticipantID
   *        Participant ID to query. May not be <code>null</code>.
   * @param aDocTypeID
   *        Document type ID. May not be <code>null</code>.
   * @param aProcessID
   *        Process ID. May not be <code>null</code>.
   * @param sTransportProfile
   *        Transport profile ID. May not be <code>null</code>.
   * @return <code>null</code> if not found.
   */
  @Nullable
  ServiceMetadataType getServiceMetadata (@Nonnull IParticipantIdentifier aParticipantID,
                                          @Nonnull IDocumentTypeIdentifier aDocTypeID,
                                          @Nonnull IProcessIdentifier aProcessID,
                                          @Nonnull String sTransportProfile);

  /**
   * Find the dynamic discovery endpoint from the respective parameters.
   *
   * @param aParticipantID
   *        Participant ID to query. May not be <code>null</code>.
   * @param aDocTypeID
   *        Document type ID. May not be <code>null</code>.
   * @param aProcessID
   *        Process ID. May not be <code>null</code>.
   * @param sTransportProfile
   *        Transport profile to be used. May not be <code>null</code>.
   * @return <code>null</code> if no such endpoint was found
   * @see #getServiceMetadata(IParticipantIdentifier, IDocumentTypeIdentifier,
   *      IProcessIdentifier, String)
   * @since 2.0.0-rc1
   */
  @Nullable
  default EndpointType getEndpoint (@Nonnull final IParticipantIdentifier aParticipantID,
                                    @Nonnull final IDocumentTypeIdentifier aDocTypeID,
                                    @Nonnull final IProcessIdentifier aProcessID,
                                    @Nonnull final String sTransportProfile)
  {
    final ServiceMetadataType aSM = getServiceMetadata (aParticipantID, aDocTypeID, aProcessID, sTransportProfile);
    return getEndpoint (aSM, aProcessID, sTransportProfile);
  }

  /**
   * Find the dynamic discovery endpoint from the respective parameters.
   *
   * @param aSM
   *        The service metadata to be searched. May be <code>null</code>.
   * @param aProcessID
   *        Process ID. May not be <code>null</code>.
   * @param sTransportProfile
   *        Transport profile to be used. May not be <code>null</code>.
   * @return <code>null</code> if no such endpoint was found
   * @see #getServiceMetadata(IParticipantIdentifier, IDocumentTypeIdentifier,
   *      IProcessIdentifier, String)
   * @since 2.0.0-rc4
   */
  @Nullable
  static EndpointType getEndpoint (@Nullable final ServiceMetadataType aSM,
                                   @Nonnull final IProcessIdentifier aProcessID,
                                   @Nonnull final String sTransportProfile)
  {
    if (aSM != null)
    {
      final ServiceInformationType aSI = aSM.getServiceInformation ();
      if (aSI != null)
      {
        final ProcessType aProcess = CollectionHelper.findFirst (aSI.getProcessList ().getProcess (),
                                                                 x -> aProcessID.hasSameContent (SimpleProcessIdentifier.wrap (x.getProcessIdentifier ())));
        if (aProcess != null)
        {
          return CollectionHelper.findFirst (aProcess.getServiceEndpointList ().getEndpoint (),
                                             x -> sTransportProfile.equals (x.getTransportProfile ()));
        }
      }
    }
    return null;
  }
}
