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

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletHandler;

/**
 * @author yerlibilgin
 */
public class GWMocServletContainer {

  private static Server server;
  private static SampleGWServlet servlet;

  public static void createServletOn(final int port, final String localPath) {
    new Thread(() -> {
      try {
        server = new Server(port);

        ServletHandler servletHandler = new ServletHandler();
        server.setHandler(servletHandler);

        servlet = (SampleGWServlet) servletHandler.addServletWithMapping(SampleGWServlet.class, localPath).getServlet();

        server.start();
        server.join();
      } catch (Exception ex) {
        throw new IllegalStateException(ex.getMessage(), ex);
      }

    }).start();
  }

  public static void stop() {
    try {
      server.stop();
    } catch (Exception ignored) {
    }
  }
}
