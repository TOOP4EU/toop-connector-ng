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
package eu.toop.connector.mem.external.test;

import java.nio.charset.StandardCharsets;
import java.security.KeyStore;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

import javax.annotation.Nonnull;

import com.helger.commons.mime.CMimeType;
import com.helger.peppol.smp.ESMPTransportProfile;

import eu.toop.commons.codelist.EPredefinedDocumentTypeIdentifier;
import eu.toop.commons.codelist.EPredefinedProcessIdentifier;
import eu.toop.connector.api.TCConfig;
import eu.toop.connector.api.me.model.MEMessage;
import eu.toop.connector.api.me.outgoing.IMERoutingInformation;
import eu.toop.connector.api.me.outgoing.MERoutingInformation;
import eu.toop.connector.mem.external.EActingSide;

/**
 * @author yerlibilgin
 */
public class SampleDataProvider {

  private static KeyStore domibusKeystore;

  public static X509Certificate readDomibusCert(final String alias) {
    try {
      if (domibusKeystore == null) {
        // multithread initialiation danger... yes no big deal.
        domibusKeystore = KeyStore.getInstance("JKS");
        domibusKeystore.load(SampleDataProvider.class.getResourceAsStream("/dev-gw-jks/domibus-toop-keys.jks"),
            "test123".toCharArray());
      }

      return (X509Certificate) domibusKeystore.getCertificate(alias);

    } catch (final Exception e) {
      throw new IllegalStateException(e);
    }
  }

  @Nonnull
  public static X509Certificate readCert(final EActingSide actingSide) {
    try {
      // If I am DC, use dp certificate or vice versa
      final String certName = actingSide == EActingSide.DC ? "/freedonia.crt" : "/elonia.crt";
      return (X509Certificate) CertificateFactory.getInstance("X509")
          .generateCertificate(SampleDataProvider.class.getResourceAsStream(certName));
    } catch (final CertificateException e) {
      throw new IllegalStateException(e);
    }
  }

  public static IMERoutingInformation createGatewayRoutingMetadata(final EActingSide actingSide,
                                                                    final String receivingGWURL) {
    final X509Certificate aCert = readCert(actingSide);
    return createGatewayRoutingMetadata(receivingGWURL, aCert);
  }

  @SuppressWarnings("deprecation")
  public static IMERoutingInformation createGatewayRoutingMetadata(final String targetURL,
                                                                    final X509Certificate targetCert) {
    final IMERoutingInformation metadata = new MERoutingInformation(TCConfig.getIdentifierFactory().createParticipantIdentifier("iso6523-actorid-upis", "0088:123456"),
                                                                    TCConfig.getIdentifierFactory().createParticipantIdentifier("iso6523-actorid-upis", "0099:123456"),
        EPredefinedDocumentTypeIdentifier.URN_EU_TOOP_NS_DATAEXCHANGE_1P40_REQUEST_URN_EU_TOOP_REQUEST_REGISTEREDORGANIZATION_LIST_1_40,
        EPredefinedProcessIdentifier.URN_EU_TOOP_PROCESS_DATAREQUESTRESPONSE,
        ESMPTransportProfile.TRANSPORT_PROFILE_BDXR_AS4.getID(),
        targetURL,
        targetCert);

    return metadata;
  }

  @Nonnull
  public static MEMessage createSampleMessage() {
    return MEMessage.builder()
        .payload(x -> x.mimeType(CMimeType.APPLICATION_XML)
            .contentID("xmlpayload@dp")
            .data("<sample>that is a sample xml</sample>", StandardCharsets.ISO_8859_1))
        .build();
  }
}
