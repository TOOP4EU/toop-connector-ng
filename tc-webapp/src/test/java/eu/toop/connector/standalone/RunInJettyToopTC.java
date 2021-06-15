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
package eu.toop.connector.standalone;

import javax.annotation.concurrent.Immutable;

import com.helger.photon.jetty.JettyStarter;

/**
 * Run as a standalone web application in Jetty on port 8090.<br>
 * http://localhost:8090/
 *
 * @author Philip Helger
 */
@Immutable
public final class RunInJettyToopTC
{
  public static void main (final String [] args) throws Exception
  {
    final JettyStarter js = new JettyStarter (RunInJettyToopTC.class).setPort (8090)
                                                                     .setStopPort (9090)
                                                                     .setSessionCookieName ("TOOP_TC_SESSION")
                                                                     .setContainerIncludeJarPattern (JettyStarter.CONTAINER_INCLUDE_JAR_PATTERN_ALL);
    js.run ();
  }
}
