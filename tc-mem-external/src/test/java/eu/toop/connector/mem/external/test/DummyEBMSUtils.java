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
package eu.toop.connector.mem.external.test;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;

import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;

import eu.toop.connector.api.me.incoming.MEIncomingException;
import eu.toop.connector.mem.external.DateTimeUtils;
import eu.toop.connector.mem.external.EBMSUtils;
import eu.toop.connector.mem.external.MEMConstants;
import eu.toop.connector.mem.external.SoapUtil;
import eu.toop.connector.mem.external.SoapXPathUtil;

/**
 * @author myildiz
 */
final class DummyEBMSUtils {
  private static boolean failOnSubmissionResult;
  private static boolean failOnRelayResult;
  private static String relayEbmsError;

  final static String xmlTemplate = com.helger.commons.io.stream.StreamHelper.getAllBytesAsString(DummyEBMSUtils.class.getResourceAsStream("/relay-sr-template.txt"),
                                                                                                  StandardCharsets.UTF_8);

  /*
   * Process the soap message and create a SubmissionResult from it
   */
  public static SOAPMessage inferSubmissionResult(final SOAPMessage receivedMessage) throws MEIncomingException {
    final String action = MEMConstants.ACTION_SUBMISSION_RESULT;

    String theAS4Message;
    try {
      theAS4Message = SoapXPathUtil.safeFindSingleNode(receivedMessage.getSOAPHeader(),
                                                       ".//:Property[@name='MessageId']/text()")
                                   .getTextContent();
    } catch (final SOAPException e) {
      throw new MEIncomingException(e.getMessage(), e);
    }

    String xml = xmlTemplate.replace("${timestamp}", DateTimeUtils.getCurrentTimestamp())
                            .replace("${messageId}", EBMSUtils.genereateEbmsMessageId("test"))
                            .replace("${action}", action)
                            .replace("${propMessageId}", theAS4Message)
                            .replace("${propRefToMessageId}", EBMSUtils.getMessageId(receivedMessage));

    if (failOnSubmissionResult) {
      xml = xml.replace("${result}", "Error");
      xml = xml.replace("${description}", "Shit happens!");
      xml = xml.replace("${errorCode}", "Shit happens!");
    } else {
      xml = xml.replace("${result}", "Receipt");
    }

    try {
      return SoapUtil.createMessage(null, new ByteArrayInputStream(xml.getBytes(StandardCharsets.UTF_8)));
    } catch (final Exception e) {
      throw new MEIncomingException(e.getMessage(), e);
    }
  }

  /*
   * Process the soap message and create a RelayResult from it
   */
  public static SOAPMessage inferRelayResult(final SOAPMessage receivedMessage) throws MEIncomingException {
    final String action = MEMConstants.ACTION_RELAY;

    String refToMessageId;
    try {
      refToMessageId = SoapXPathUtil.safeFindSingleNode(receivedMessage.getSOAPHeader(),
                                                        ".//:Property[@name='MessageId']/text()")
                                    .getTextContent();
    } catch (final SOAPException e) {
      throw new MEIncomingException(e.getMessage(), e);
    }

    String xml = xmlTemplate.replace("${timestamp}", DateTimeUtils.getCurrentTimestamp())
                            .replace("${messageId}", EBMSUtils.genereateEbmsMessageId("test"))
                            .replace("${action}", action)
                            .replace("${propMessageId}", refToMessageId)
                            .replace("${propRefToMessageId}", refToMessageId);

    if (failOnRelayResult) {
      xml = xml.replace("${result}", "Error");
      xml = xml.replace("${errorCode}", relayEbmsError);
    } else {
      xml = xml.replace("${result}", "Receipt");
    }

    try {
      return SoapUtil.createMessage(null, new ByteArrayInputStream(xml.getBytes(StandardCharsets.UTF_8)));
    } catch (final Exception e) {
      throw new MEIncomingException(e.getMessage(), e);
    }
  }

  /*
   * Process the soap message and create a Deliver message from it
   */
  public static SOAPMessage inferDelivery(final SOAPMessage receivedMessage) {
    return receivedMessage;
  }

  public static void setFailOnSubmissionResult(final boolean fail) {
    DummyEBMSUtils.failOnSubmissionResult = fail;
  }

  public static void setFailOnRelayResult(final boolean fail, final String errorCode) {
    DummyEBMSUtils.failOnRelayResult = fail;
    DummyEBMSUtils.relayEbmsError = errorCode;

  }

}
