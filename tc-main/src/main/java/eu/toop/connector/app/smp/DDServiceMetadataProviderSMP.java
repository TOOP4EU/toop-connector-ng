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
package eu.toop.connector.app.smp;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.helger.commons.ValueEnforcer;
import com.helger.peppolid.IDocumentTypeIdentifier;
import com.helger.peppolid.IParticipantIdentifier;
import com.helger.peppolid.IProcessIdentifier;
import com.helger.smpclient.bdxr1.IBDXRServiceMetadataProvider;
import com.helger.smpclient.exception.SMPClientException;
import com.helger.smpclient.url.SMPDNSResolutionException;
import com.helger.xsds.bdxr.smp1.ServiceMetadataType;
import com.helger.xsds.bdxr.smp1.SignedServiceMetadataType;

import eu.toop.connector.api.dd.IDDServiceMetadataProvider;

/**
 * An implementation of {@link IDDServiceMetadataProvider} going to the SMP for
 * querying.
 *
 * @author Philip Helger
 */
public class DDServiceMetadataProviderSMP extends AbstractDDClient implements IDDServiceMetadataProvider
{
  public DDServiceMetadataProviderSMP ()
  {}

  @Nullable
  public ServiceMetadataType getServiceMetadata (@Nonnull final IParticipantIdentifier aParticipantID,
                                                 @Nonnull final IDocumentTypeIdentifier aDocTypeID,
                                                 @Nonnull final IProcessIdentifier aProcessID,
                                                 @Nonnull final String sTransportProfile)
  {
    ValueEnforcer.notNull (aParticipantID, "ParticipantID");
    ValueEnforcer.notNull (aDocTypeID, "DocTypeID");

    try
    {
      final IBDXRServiceMetadataProvider aBDXR1Client = getServiceMetadataProvider (aParticipantID,
                                                                                    aDocTypeID,
                                                                                    aProcessID,
                                                                                    sTransportProfile);

      final SignedServiceMetadataType aSSM = aBDXR1Client.getServiceMetadataOrNull (aParticipantID, aDocTypeID);
      if (aSSM == null)
        return null;
      return aSSM.getServiceMetadata ();
    }
    catch (final SMPDNSResolutionException | SMPClientException ex)
    {
      throw new IllegalStateException (ex);
    }
  }
}
