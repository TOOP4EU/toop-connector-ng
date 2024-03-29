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
package eu.toop.connector.app.incoming;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;

import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;

import com.helger.commons.ValueEnforcer;
import com.helger.commons.annotation.Nonempty;
import com.helger.commons.collection.ArrayHelper;
import com.helger.commons.error.level.EErrorLevel;
import com.helger.commons.mime.IMimeType;
import com.helger.commons.state.ESuccess;
import com.helger.commons.string.StringHelper;
import com.helger.commons.url.IURLProtocol;
import com.helger.commons.url.URLProtocolRegistry;
import com.helger.httpclient.HttpClientManager;
import com.helger.httpclient.response.ResponseHandlerByteArray;
import com.helger.regrep.CRegRep4;

import eu.toop.connector.api.TCConfig;
import eu.toop.connector.api.http.TCHttpClientSettings;
import eu.toop.connector.api.me.incoming.IMEIncomingTransportMetadata;
import eu.toop.connector.api.me.incoming.IncomingEDMErrorResponse;
import eu.toop.connector.api.me.incoming.IncomingEDMRequest;
import eu.toop.connector.api.me.incoming.IncomingEDMResponse;
import eu.toop.connector.api.me.model.MEPayload;
import eu.toop.connector.api.rest.TCIncomingMessage;
import eu.toop.connector.api.rest.TCIncomingMetadata;
import eu.toop.connector.api.rest.TCPayload;
import eu.toop.connector.api.rest.TCPayloadType;
import eu.toop.connector.api.rest.TCRestJAXB;
import eu.toop.kafkaclient.ToopKafkaClient;

/**
 * Push incoming messages to DC/DP via the HTTP interface.
 *
 * @author Philip Helger
 */
@Immutable
public final class DC_DP_TriggerViaHttp
{
  private DC_DP_TriggerViaHttp ()
  {}

  @Nonnull
  private static ESuccess _forwardMessage (@Nonnull final TCIncomingMessage aMsg, @Nonnull @Nonempty final String sDestURL)
  {
    ValueEnforcer.notNull (aMsg, "Msg");
    ValueEnforcer.notEmpty (sDestURL, "Destination URL");

    if (StringHelper.hasNoText (sDestURL))
      throw new IllegalStateException ("No URL for handling inbound messages is defined.");

    final IURLProtocol aProtocol = URLProtocolRegistry.getInstance ().getProtocol (sDestURL);
    if (aProtocol == null)
      throw new IllegalStateException ("The URL for handling inbound messages '" + sDestURL + "' is invalid.");

    // Convert XML to bytes
    final byte [] aPayload = TCRestJAXB.incomingMessage ().getAsBytes (aMsg);
    if (aPayload == null)
      throw new IllegalStateException ();

    ToopKafkaClient.send (EErrorLevel.INFO, () -> "Sending inbound message to '" + sDestURL + "' with " + aPayload.length + " bytes");

    // Main sending, using TC http settings
    try (final HttpClientManager aHCM = HttpClientManager.create (new TCHttpClientSettings ()))
    {
      final HttpPost aPost = new HttpPost (sDestURL);
      aPost.setEntity (new ByteArrayEntity (aPayload));
      final byte [] aResult = aHCM.execute (aPost, new ResponseHandlerByteArray ());

      ToopKafkaClient.send (EErrorLevel.INFO,
                            () -> "Sending inbound message was successful. Got " + ArrayHelper.getSize (aResult) + " bytes back");
      return ESuccess.SUCCESS;
    }
    catch (final Exception ex)
    {
      ToopKafkaClient.send (EErrorLevel.ERROR, () -> "Sending inbound message to '" + sDestURL + "' failed", ex);
      return ESuccess.FAILURE;
    }
  }

  @Nonnull
  private static TCIncomingMetadata _createMetadata (@Nonnull final IMEIncomingTransportMetadata aMD,
                                                     @Nonnull final TCPayloadType ePayloadType)
  {
    final TCIncomingMetadata ret = new TCIncomingMetadata ();
    ret.setSenderID (TCRestJAXB.createTCID (aMD.getSenderID ()));
    ret.setReceiverID (TCRestJAXB.createTCID (aMD.getReceiverID ()));
    ret.setDocTypeID (TCRestJAXB.createTCID (aMD.getDocumentTypeID ()));
    ret.setProcessID (TCRestJAXB.createTCID (aMD.getProcessID ()));
    ret.setPayloadType (ePayloadType);
    return ret;
  }

  @Nonnull
  private static TCPayload _createPayload (@Nonnull final byte [] aValue,
                                           @Nullable final String sContentID,
                                           @Nonnull final IMimeType aMimeType)
  {
    final TCPayload ret = new TCPayload ();
    ret.setValue (aValue);
    ret.setContentID (sContentID);
    ret.setMimeType (aMimeType.getAsString ());
    return ret;
  }

  @Nonnull
  @Nonempty
  private static String _getConfiguredDestURL ()
  {
    final String ret = TCConfig.MEM.getMEMIncomingURL ();
    if (StringHelper.hasNoText (ret))
      throw new IllegalStateException ("The MEM incoming URL for forwarding to DC/DP is not configured.");
    return ret;
  }

  @Nonnull
  public static ESuccess forwardMessage (@Nonnull final IncomingEDMRequest aRequest)
  {
    return forwardMessage (aRequest, _getConfiguredDestURL ());
  }

  @Nonnull
  public static ESuccess forwardMessage (@Nonnull final IncomingEDMRequest aRequest, @Nonnull @Nonempty final String sDestURL)
  {
    ValueEnforcer.notEmpty (sDestURL, "Destination URL");

    final TCIncomingMessage aMsg = new TCIncomingMessage ();
    aMsg.setMetadata (_createMetadata (aRequest.getMetadata (), TCPayloadType.REQUEST));
    aMsg.addPayload (_createPayload (aRequest.getRequest ().getWriter ().getAsBytes (),
                                     aRequest.getTopLevelContentID (),
                                     CRegRep4.MIME_TYPE_EBRS_XML));
    return _forwardMessage (aMsg, sDestURL);
  }

  @Nonnull
  public static ESuccess forwardMessage (@Nonnull final IncomingEDMResponse aResponse)
  {
    return forwardMessage (aResponse, _getConfiguredDestURL ());
  }

  @Nonnull
  public static ESuccess forwardMessage (@Nonnull final IncomingEDMResponse aResponse, @Nonnull @Nonempty final String sDestURL)
  {
    ValueEnforcer.notEmpty (sDestURL, "Destination URL");

    final TCIncomingMessage aMsg = new TCIncomingMessage ();
    aMsg.setMetadata (_createMetadata (aResponse.getMetadata (), TCPayloadType.RESPONSE));
    aMsg.addPayload (_createPayload (aResponse.getResponse ().getWriter ().getAsBytes (),
                                     aResponse.getTopLevelContentID (),
                                     CRegRep4.MIME_TYPE_EBRS_XML));
    // Add all attachments
    for (final MEPayload aPayload : aResponse.attachments ().values ())
      aMsg.addPayload (_createPayload (aPayload.getData ().bytes (), aPayload.getContentID (), aPayload.getMimeType ()));
    return _forwardMessage (aMsg, sDestURL);
  }

  @Nonnull
  public static ESuccess forwardMessage (@Nonnull final IncomingEDMErrorResponse aErrorResponse)
  {
    return forwardMessage (aErrorResponse, _getConfiguredDestURL ());
  }

  @Nonnull
  public static ESuccess forwardMessage (@Nonnull final IncomingEDMErrorResponse aErrorResponse, @Nonnull @Nonempty final String sDestURL)
  {
    ValueEnforcer.notEmpty (sDestURL, "Destination URL");

    final TCIncomingMessage aMsg = new TCIncomingMessage ();
    aMsg.setMetadata (_createMetadata (aErrorResponse.getMetadata (), TCPayloadType.ERROR_RESPONSE));
    aMsg.addPayload (_createPayload (aErrorResponse.getErrorResponse ().getWriter ().getAsBytes (),
                                     aErrorResponse.getTopLevelContentID (),
                                     CRegRep4.MIME_TYPE_EBRS_XML));
    return _forwardMessage (aMsg, sDestURL);
  }
}
