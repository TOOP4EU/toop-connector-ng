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
package eu.toop.connector.api.as4;

import java.io.IOException;

import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.bouncycastle.util.encoders.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.helger.commons.io.resource.ClassPathResource;
import com.helger.commons.io.stream.StreamHelper;
import com.helger.commons.mime.CMimeType;
import com.helger.httpclient.HttpClientManager;
import com.helger.httpclient.response.ResponseHandlerJson;
import com.helger.json.IJson;
import com.helger.json.serialize.JsonWriter;
import com.helger.json.serialize.JsonWriterSettings;

import eu.toop.connector.api.TCIdentifierFactory;
import eu.toop.connector.api.me.EMEProtocol;
import eu.toop.connector.api.rest.TCOutgoingMessage;
import eu.toop.connector.api.rest.TCOutgoingMetadata;
import eu.toop.connector.api.rest.TCPayload;
import eu.toop.connector.api.rest.TCRestJAXB;

public class MainSendEdmRequest
{
  private static final Logger LOGGER = LoggerFactory.getLogger (MainSendEdmRequest.class);

  public static void main (final String [] args) throws IOException
  {
    final TCOutgoingMessage aOM = new TCOutgoingMessage ();
    {
      final TCOutgoingMetadata aMetadata = new TCOutgoingMetadata ();
      aMetadata.setSenderID (TCRestJAXB.createTCID (TCIdentifierFactory.PARTICIPANT_SCHEME, "9914:tc-ng-test-sender"));
      aMetadata.setReceiverID (TCRestJAXB.createTCID (TCIdentifierFactory.PARTICIPANT_SCHEME, "9915:tooptest"));
      aMetadata.setDocTypeID (TCRestJAXB.createTCID (TCIdentifierFactory.DOCTYPE_SCHEME,
                                                     "urn:eu:toop:ns:dataexchange-1p40::Response##urn:eu.toop.response.registeredorganization::1.40"));
      aMetadata.setProcessID (TCRestJAXB.createTCID (TCIdentifierFactory.PROCESS_SCHEME, "urn:eu.toop.process.datarequestresponse"));
      aMetadata.setTransportProtocol (EMEProtocol.AS4.getTransportProfileID ());
      aMetadata.setEndpointURL ("https://toop.usp.gv.at/noopdc/phase4");
      aMetadata.setReceiverCertificate (Base64.decode ("MIIEqjCCApKgAwIBAgICEAkwDQYJKoZIhvcNAQELBQAwVzELMAkGA1UEBhMCRVUxDTALBgNVBAoMBFRPT1AxDTALBgNVBAsMBENDVEYxKjAoBgNVBAMMIVRPT1AgUElMT1RTIFRFU1QgQUNDRVNTIFBPSU5UUyBDQTAeFw0xODEwMjYxMTA4MDBaFw0yMDEwMjUxMTA4MDBaMDkxCzAJBgNVBAYTAkFUMRwwGgYDVQQKDBNCdW5kZXNyZWNoZW56ZW50cnVtMQwwCgYDVQQDDANCUlowggEiMA0GCSqGSIb3DQEBAQUAA4IBDwAwggEKAoIBAQC0ZKt1+9ulKvbTxyVh6XKcLZ24CQIGQMkiLzZYSkPrdxa59Ac7jhrk3V0Rv+wjW6rT3kxHd+nmFKli7fiql1jj+pTlmnEL8vv1TLfDeH0ADLVkfsiinM4FgriNjPI62EY6n9L3WKrua/RMdCPstJqqbUBpNG52MsiGFDxoYnK96JAlzwfYatIga+jvGyxmeNP2vwpXkCDI8eTS2SF/XX4MM8om3/5HZ1yj0POONHYtXupkd7y8dU39VZcBq5YCxIGAee1A5K1eFTcJmBWRPkK9tqW+Y8vyb++WH8XB6PfQZ2Gc24ItVXUgXAsokbnUlhpzt2/v/YXH9EnmZjYoNlLJAgMBAAGjgZ0wgZowCQYDVR0TBAIwADARBglghkgBhvhCAQEEBAMCBDAwKgYJYIZIAYb4QgENBB0WG1RPT1AgUGlsb3QgVGVzdCBDZXJ0aWZpY2F0ZTAOBgNVHQ8BAf8EBAMCBeAwHQYDVR0OBBYEFJhuTeXVJ7OkEQtuP2uENZBpBMcdMB8GA1UdIwQYMBaAFHulODfJDc+HzJc6ouc/Z44yuaheMA0GCSqGSIb3DQEBCwUAA4ICAQCAFdUTuGJrTc0ex3neGkkWBqYSEMp+eHZ4tyJhabCeKKs8/U4WUMklfI9ubNx/ncN621/kxLbiRTIFT3n7oLc416zI/gPB7pz+QZZOj6AQM+ftonsn77E/sfgWwT2+UpzXwt9Bfrm4U2g64MKPU9WObu9960AOmNuKvDRjDbaeUl5SFYkU71QTcNcfHSUR4v7n3cV1heq3abQFguKL04BpCx21h3SnPWau+r+w4aNrODk9i/0Z2QyfnimSpc6mHGLYTXAemrSzHBKZln3C2rTC2rOMncTlcrPGJpHYOdAyOczBvi/P4Be7ZIjrHQZun40Me5PA/PudWcJCKx1Lt/qNYPCdOtsEnY20LUWjL1eJwfIeBbA1zXIlmTp+oKA+4PP4Pdz/LL/VlKejKXq0Y0Htl+AP5WDY3axqwnxqWHPp9MtHjNaxA4oldc484NUugYZCpijfLA/ZRNStRVQMIOoKk8aIF4oqAY8hk90wKWdbTQaugTfx2hyyMMzILNth+qgqOrvkSk4qA/g5r0AyrpdIMz83/Kn43Rcmbh72wMWKRMX8UxHCS4KCZZRS6dVVFtZES/GiNVy9aUbd+e+zywxiwb7fu5Ln/kDiDlYWHAG/FzGK9MOyvH3/2BFjULfG/jQ2ZaGeDe+0IqyXUSOhqZMSX78DSIGezyaIutkDrAN0Pw=="));
      aOM.setMetadata (aMetadata);
    }
    {
      final TCPayload aPayload = new TCPayload ();
      aPayload.setValue (StreamHelper.getAllBytes (new ClassPathResource ("edm/Concept Request_LP.xml")));
      aPayload.setMimeType (CMimeType.APPLICATION_XML.getAsString ());
      aPayload.setContentID ("mock-request@toop");
      aOM.addPayload (aPayload);
    }

    LOGGER.info (TCRestJAXB.outgoingMessage ().getAsString (aOM));

    try (final HttpClientManager aHCM = new HttpClientManager ())
    {
      final HttpPost aPost = new HttpPost ("http://localhost:8090/api/send");
      aPost.setEntity (new ByteArrayEntity (TCRestJAXB.outgoingMessage ().getAsBytes (aOM)));
      final IJson aJson = aHCM.execute (aPost, new ResponseHandlerJson ());
      LOGGER.info (new JsonWriter (new JsonWriterSettings ().setIndentEnabled (true)).writeAsString (aJson));
    }
  }
}
