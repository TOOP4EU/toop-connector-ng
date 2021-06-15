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

import static org.junit.Assert.assertSame;

import java.net.URL;
import java.nio.charset.StandardCharsets;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.helger.commons.concurrent.ThreadHelper;
import com.helger.commons.io.resource.ClassPathResource;
import com.helger.commons.url.URLHelper;
import com.helger.config.Config;
import com.helger.config.source.res.ConfigurationSourceProperties;
import com.helger.scope.mock.ScopeAwareTestSetup;

import eu.toop.connector.api.TCConfig;
import eu.toop.connector.api.me.model.MEMessage;
import eu.toop.connector.api.me.outgoing.IMERoutingInformation;
import eu.toop.connector.api.me.outgoing.MEOutgoingException;
import eu.toop.connector.mem.external.EActingSide;
import eu.toop.connector.mem.external.MEMDelegate;
import eu.toop.connector.mem.external.notifications.IMessageHandler;
import eu.toop.edm.error.EToopErrorCode;

/**
 * This test suite tests the whole sending/receiving of a simple MEMessage by
 * mocking the as4 gateway
 *
 * @author myildiz at 16.02.2018.
 */
@FixMethodOrder
public class TestSendReceive {
  static {
    TCConfig.setConfig(Config.create(new ConfigurationSourceProperties(new ClassPathResource("toop-connector.elonia.unitTest.properties"),
                                                                       StandardCharsets.UTF_8)));
  }

  // this must be created after the above level setting statement
  private static final Logger LOG = LoggerFactory.getLogger(TestSendReceive.class);
  private static IMERoutingInformation gatewayRoutingMetadata;
  private static MEMessage sampleMessage;

  /**
   * Create a mock server on localhost that reads and sends back a MEMessage.
   */
  @BeforeClass
  public static void prepare() {
    // Port must match the message-processor.properties
    if (LOG.isDebugEnabled())
      LOG.debug("Prepare for the test");

    final URL backendURL = URLHelper.getAsURL("http://localhost:10001/backend");
    final URL gwURL = URLHelper.getAsURL(TCConfig.MEM.getMEMAS4Endpoint());

    if (LOG.isInfoEnabled()) {
      LOG.info("backend port: " + backendURL.getPort());
      LOG.info("backend localpath: " + backendURL.getPath());
      LOG.info("GW port: " + gwURL.getPort());
      LOG.info("GW localpath: " + gwURL.getPath());
    }

    BackendServletContainer.createServletOn(backendURL.getPort(), backendURL.getPath());
    GWMocServletContainer.createServletOn(gwURL.getPort(), gwURL.getPath());
    ScopeAwareTestSetup.setupScopeTests();
    gatewayRoutingMetadata = SampleDataProvider.createGatewayRoutingMetadata(EActingSide.DC,
                                                                             TCConfig.MEM.getMEMAS4Endpoint());
    sampleMessage = SampleDataProvider.createSampleMessage();
    final IMessageHandler handler = meMessage1 -> LOG.info("hooray! I Got a message");
    MEMDelegate.getInstance().registerMessageHandler(handler);

    ThreadHelper.sleep(1000);

  }

  @AfterClass
  public static void shutdown() {
    ScopeAwareTestSetup.shutdownScopeTests();

    BackendServletContainer.stop();
    GWMocServletContainer.stop();
  }

  @Test
  public void testSendReceive() throws MEOutgoingException {
    DummyEBMSUtils.setFailOnRelayResult(false, null);
    DummyEBMSUtils.setFailOnSubmissionResult(false);
    MEMDelegate.getInstance().sendMessage(gatewayRoutingMetadata, sampleMessage);
  }

  @Test
  public void testME002() {
    DummyEBMSUtils.setFailOnRelayResult(false, null);
    DummyEBMSUtils.setFailOnSubmissionResult(true);
    try {
      MEMDelegate.getInstance().sendMessage(gatewayRoutingMetadata, sampleMessage);
      throw new IllegalStateException("An error must occur");
    } catch (final MEOutgoingException meException) {
      LOG.error(meException.getMessage(), meException);
      assertSame(EToopErrorCode.ME_002, meException.getToopErrorCode());
    }
  }

  @Test
  public void testME003() {
    DummyEBMSUtils.setFailOnSubmissionResult(false);
    DummyEBMSUtils.setFailOnRelayResult(true, "EBMS:0301");
    try {
      MEMDelegate.getInstance().sendMessage(gatewayRoutingMetadata, sampleMessage);
      throw new IllegalStateException("An error must occur");
    } catch (final MEOutgoingException meException) {
      LOG.error(meException.getMessage(), meException);
      assertSame(EToopErrorCode.ME_003, meException.getToopErrorCode());
    }

  }

  @Test
  public void testME004() {
    DummyEBMSUtils.setFailOnSubmissionResult(false);
    DummyEBMSUtils.setFailOnRelayResult(true, "EBMS:0345");
    try {
      MEMDelegate.getInstance().sendMessage(gatewayRoutingMetadata, sampleMessage);
      throw new IllegalStateException("An error must occur");
    } catch (final MEOutgoingException meException) {
      LOG.error(meException.getMessage(), meException);
      assertSame(EToopErrorCode.ME_004, meException.getToopErrorCode());
    }
  }

}
