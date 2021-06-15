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

import java.security.cert.CertificateEncodingException;
import java.security.cert.X509Certificate;

import javax.annotation.Nonnull;

import com.helger.commons.ValueEnforcer;
import com.helger.commons.url.URLHelper;
import com.helger.peppolid.IDocumentTypeIdentifier;
import com.helger.peppolid.IParticipantIdentifier;
import com.helger.peppolid.IProcessIdentifier;
import com.helger.peppolid.factory.BDXR1IdentifierFactory;
import com.helger.smpclient.bdxr1.BDXRClientReadOnly;
import com.helger.smpclient.bdxr1.IBDXRServiceGroupProvider;
import com.helger.smpclient.bdxr1.IBDXRServiceMetadataProvider;
import com.helger.smpclient.url.BDXLURLProvider;
import com.helger.smpclient.url.SMPDNSResolutionException;
import com.helger.xsds.bdxr.smp1.EndpointType;
import com.helger.xsds.bdxr.smp1.ProcessListType;
import com.helger.xsds.bdxr.smp1.ProcessType;
import com.helger.xsds.bdxr.smp1.ServiceEndpointList;
import com.helger.xsds.bdxr.smp1.ServiceInformationType;
import com.helger.xsds.bdxr.smp1.ServiceMetadataType;
import com.helger.xsds.bdxr.smp1.SignedServiceMetadataType;

import eu.toop.connector.api.TCConfig;

public abstract class AbstractDDClient
{
  protected AbstractDDClient ()
  {}

  @Nonnull
  private static BDXRClientReadOnly _getSMPClient (@Nonnull final IParticipantIdentifier aRecipientID) throws SMPDNSResolutionException
  {
    if (TCConfig.R2D2.isR2D2UseDNS ())
    {
      ValueEnforcer.notNull (aRecipientID, "RecipientID");

      // Use dynamic lookup via DNS - can throw exception
      return new BDXRClientReadOnly (BDXLURLProvider.INSTANCE, aRecipientID, TCConfig.R2D2.getR2D2SML ());
    }

    // Use a constant SMP URL
    return new BDXRClientReadOnly (TCConfig.R2D2.getR2D2SMPUrl ());
  }

  @Nonnull
  public static IBDXRServiceGroupProvider getServiceGroupProvider (@Nonnull final IParticipantIdentifier aRecipientID) throws SMPDNSResolutionException
  {
    return _getSMPClient (aRecipientID);
  }

  @Nonnull
  public static IBDXRServiceMetadataProvider getServiceMetadataProvider (@Nonnull final IParticipantIdentifier aRecipientID,
                                                                         @Nonnull final IDocumentTypeIdentifier aDocTypeID,
                                                                         @Nonnull final IProcessIdentifier aProcessID,
                                                                         @Nonnull final String sTransportProfile) throws SMPDNSResolutionException
  {
    if (!TCConfig.R2D2.isR2D2UseDNS ())
    {
      final String sStaticEndpoint = TCConfig.R2D2.getR2D2StaticEndpointURL ();
      final X509Certificate aStaticCert = TCConfig.R2D2.getR2D2StaticCertificate ();
      if (URLHelper.getAsURL (sStaticEndpoint) != null && aStaticCert != null)
      {
        // Create a static service metadata
        return (aServiceGroupID, aDocumentTypeID) -> {
          final SignedServiceMetadataType ret = new SignedServiceMetadataType ();
          final ServiceMetadataType aSM = new ServiceMetadataType ();
          final ServiceInformationType aSI = new ServiceInformationType ();
          aSI.setParticipantIdentifier (BDXR1IdentifierFactory.INSTANCE.createParticipantIdentifier (aServiceGroupID.getScheme (),
                                                                                                     aServiceGroupID.getValue ()));
          aSI.setDocumentIdentifier (BDXR1IdentifierFactory.INSTANCE.createDocumentTypeIdentifier (aDocTypeID.getScheme (),
                                                                                                   aDocTypeID.getValue ()));
          {
            final ProcessListType aPL = new ProcessListType ();
            final ProcessType aProcess = new ProcessType ();
            aProcess.setProcessIdentifier (BDXR1IdentifierFactory.INSTANCE.createProcessIdentifier (aProcessID.getScheme (),
                                                                                                    aProcessID.getValue ()));
            final ServiceEndpointList aSEL = new ServiceEndpointList ();
            final EndpointType aEndpoint = new EndpointType ();
            aEndpoint.setEndpointURI (sStaticEndpoint);
            aEndpoint.setRequireBusinessLevelSignature (Boolean.FALSE);
            try
            {
              aEndpoint.setCertificate (aStaticCert.getEncoded ());
            }
            catch (final CertificateEncodingException ex)
            {
              throw new IllegalArgumentException ("Failed to encode certificate " + aStaticCert);
            }
            aEndpoint.setServiceDescription ("Mocked service");
            aEndpoint.setTechnicalContactUrl ("Mocked service - no support");
            aEndpoint.setTransportProfile (sTransportProfile);
            aSEL.addEndpoint (aEndpoint);
            aProcess.setServiceEndpointList (aSEL);
            aPL.addProcess (aProcess);
            aSI.setProcessList (aPL);
          }
          aSM.setServiceInformation (aSI);
          ret.setServiceMetadata (aSM);
          return ret;
        };
      }
    }
    return _getSMPClient (aRecipientID);
  }
}
