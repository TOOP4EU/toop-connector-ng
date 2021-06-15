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
