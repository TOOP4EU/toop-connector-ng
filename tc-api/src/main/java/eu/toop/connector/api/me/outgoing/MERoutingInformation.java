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

import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.annotation.Nonnull;

import com.helger.commons.ValueEnforcer;
import com.helger.commons.annotation.Nonempty;
import com.helger.commons.string.ToStringGenerator;
import com.helger.peppolid.IDocumentTypeIdentifier;
import com.helger.peppolid.IParticipantIdentifier;
import com.helger.peppolid.IProcessIdentifier;
import com.helger.security.certificate.CertificateHelper;

import eu.toop.connector.api.rest.TCOutgoingMetadata;

/**
 * Default implementation of {@link IMERoutingInformation}.
 *
 * @author Philip Helger
 */
public class MERoutingInformation extends MERoutingInformationInput implements IMERoutingInformation
{
  private final String m_sEndpointURL;
  private final X509Certificate m_aCert;

  public MERoutingInformation (@Nonnull final MERoutingInformationInput aOther,
                               @Nonnull @Nonempty final String sEndpointURL,
                               @Nonnull final X509Certificate aCert)
  {
    this (aOther.getSenderID (),
          aOther.getReceiverID (),
          aOther.getDocumentTypeID (),
          aOther.getProcessID (),
          aOther.getTransportProtocol (),
          sEndpointURL,
          aCert);
  }

  public MERoutingInformation (@Nonnull final IParticipantIdentifier aSenderID,
                               @Nonnull final IParticipantIdentifier aReceiverID,
                               @Nonnull final IDocumentTypeIdentifier aDocTypeID,
                               @Nonnull final IProcessIdentifier aProcessID,
                               @Nonnull @Nonempty final String sTransportProtocol,
                               @Nonnull @Nonempty final String sEndpointURL,
                               @Nonnull final X509Certificate aCert)
  {
    super (aSenderID, aReceiverID, aDocTypeID, aProcessID, sTransportProtocol);
    ValueEnforcer.notEmpty (sEndpointURL, "EndpointURL");
    ValueEnforcer.notNull (aCert, "Cert");

    m_sEndpointURL = sEndpointURL;
    m_aCert = aCert;
  }

  @Nonnull
  @Nonempty
  public String getEndpointURL ()
  {
    return m_sEndpointURL;
  }

  @Nonnull
  public X509Certificate getCertificate ()
  {
    return m_aCert;
  }

  @Override
  public String toString ()
  {
    return ToStringGenerator.getDerived (super.toString ()).append ("EndpointURL", m_sEndpointURL).append ("Cert", m_aCert).getToString ();
  }

  @Nonnull
  public static MERoutingInformation createFrom (@Nonnull final TCOutgoingMetadata aMetadata) throws CertificateException
  {
    ValueEnforcer.notNull (aMetadata, "Metadata");
    return new MERoutingInformation (MERoutingInformationInput.createForInput (aMetadata),
                                     aMetadata.getEndpointURL (),
                                     CertificateHelper.convertByteArrayToCertficateDirect (aMetadata.getReceiverCertificate ()));
  }
}
