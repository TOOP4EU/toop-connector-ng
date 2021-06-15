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
package eu.toop.connector.servlet;

import javax.servlet.annotation.WebServlet;

import com.helger.commons.http.EHttpMethod;
import com.helger.xservlet.AbstractXServlet;

/**
 * The servlet to show the application status.
 *
 * @author Philip Helger
 */
@WebServlet ("/tc-status/*")
public class TCStatusServlet extends AbstractXServlet
{
  public static final String SERVLET_DEFAULT_NAME = "tc-status";
  public static final String SERVLET_DEFAULT_PATH = '/' + SERVLET_DEFAULT_NAME;

  public TCStatusServlet ()
  {
    handlerRegistry ().registerHandler (EHttpMethod.GET, new TCStatusXServletHandler ());
  }
}
