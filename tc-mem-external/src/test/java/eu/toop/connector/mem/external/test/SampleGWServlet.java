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

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.soap.MimeHeaders;
import javax.xml.soap.SOAPMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.helger.commons.io.stream.NonBlockingByteArrayInputStream;
import com.helger.commons.io.stream.StreamHelper;

import eu.toop.connector.mem.external.EBMSUtils;
import eu.toop.connector.mem.external.SoapUtil;
import eu.toop.connector.mem.external.servlet.AS4InterfaceServlet;

/**
 * A moc gateway.
 *
 * @author myildiz at 06.05.2018.
 */
public class SampleGWServlet extends AS4InterfaceServlet {

  private static final Logger LOG = LoggerFactory.getLogger(SampleGWServlet.class);

  @Override
  protected void doPost(final HttpServletRequest req, final HttpServletResponse resp) throws IOException {

    LOG.info("MOC Gateway received a submission");

    // Convert the request headers into MimeHeaders
    final MimeHeaders mimeHeaders = readMimeHeaders(req);

    // no matter what happens, we will return either a receipt or a fault
    resp.setContentType("text/xml");

    SOAPMessage receivedMessage = null;
    try {
      final byte[] bytes = StreamHelper.getAllBytes(req.getInputStream());

      LOG.info("Read inbound message");

      // Todo, remove buffering later
      receivedMessage = SoapUtil.createMessage(mimeHeaders, new NonBlockingByteArrayInputStream(bytes));

      // check if the message is a notification message
      LOG.info(SoapUtil.describe(receivedMessage));

      // get the action from the soap message
      //final Node node = SoapXPathUtil
      //    .safeFindSingleNode(receivedMessage.getSOAPHeader(), "//:CollaborationInfo/:Action");
      //final String action = node.getTextContent();

      final byte[] successReceipt = EBMSUtils.createSuccessReceipt(receivedMessage);

      LOG.debug("Send success receipt");
      resp.setStatus(HttpServletResponse.SC_OK);
      resp.getOutputStream().write(successReceipt);
      resp.getOutputStream().flush();


      //trigger the handling of the received submit message
      SubmissionHandler.handle(receivedMessage);

    } catch (final Throwable th) {
      sendBackFault(resp, receivedMessage, th);
    }
  }
}
