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

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletHandler;
import org.eclipse.jetty.servlet.ServletHolder;

import eu.toop.connector.mem.external.servlet.AS4InterfaceServlet;

/**
 * @author yerlibilgin
 */
public class BackendServletContainer {
  public static void createServletOn(final int port,  String localPath) {
    new Thread(() -> {
      try {
        Server server = new Server(port);

        ServletHandler servletHandler = new ServletHandler();
        server.setHandler(servletHandler);

        ServletHolder servletHolder = servletHandler.addServletWithMapping(AS4InterfaceServlet.class, localPath);

        servletHolder.getServlet();

        server.start();
        server.join();
      } catch (Exception ex) {
        throw new IllegalStateException(ex.getMessage(), ex);
      }

    }).start();
  }

  public static void stop() {

  }
}
