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
