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

import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.helger.commons.io.stream.NonBlockingByteArrayOutputStream;
import com.helger.commons.mime.CMimeType;

import eu.toop.connector.api.me.MEException;
import eu.toop.connector.api.me.model.MEMessage;
import eu.toop.connector.mem.external.EBMSUtils;
import eu.toop.connector.mem.external.SubmissionMessageProperties;

public final class EBSMUtilsTest {
  private static final Logger LOG = LoggerFactory.getLogger(EBSMUtilsTest.class);

  @Test
  public void testFault() throws SOAPException, IOException, MEException {
    final SubmissionMessageProperties sd = new SubmissionMessageProperties();
    sd.conversationId = "EBSMUtilsTestConv";
    final MEMessage msg = MEMessage.builder()
                                   .payload(x -> x.mimeType(CMimeType.APPLICATION_XML)
                                                  .contentID("blafoo")
                                                  .data("<?xml version='1.0'?><root demo='true' />",
                                                        StandardCharsets.ISO_8859_1))
                                   .build();
    final SOAPMessage sm = EBMSUtils.convert2MEOutboundAS4Message(sd, msg);
    assertNotNull(sm);
    try (final NonBlockingByteArrayOutputStream aBAOS = new NonBlockingByteArrayOutputStream()) {
      sm.writeTo(aBAOS);
      LOG.info(aBAOS.getAsString(StandardCharsets.UTF_8));
    }

    final byte[] aFault = EBMSUtils.createFault(sm, "Unit test fault");
    LOG.info(new String(aFault, StandardCharsets.UTF_8));
  }
}
