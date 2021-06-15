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
package eu.toop.connector.jetty;

import java.util.concurrent.Callable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.helger.commons.io.resource.ClassPathResource;
import com.helger.photon.jetty.JettyStarter;
import com.helger.photon.jetty.JettyStopper;

import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;

/**
 * Main command line client
 */
@Command (description = "Standalone TOOP Connector NG", name = "tc-jetty", mixinStandardHelpOptions = true, separator = " ")
public class MainTC implements Callable <Integer>
{
  private static final Logger LOGGER = LoggerFactory.getLogger (MainTC.class);

  enum ECommand
  {
    start,
    stop
  }

  @Option (names = { "-p", "--port" },
           paramLabel = "Port",
           defaultValue = "" + JettyStarter.DEFAULT_PORT,
           description = "Port to run the TOOP Connector on (default: ${DEFAULT-VALUE})")
  private Integer m_aStartPort;

  @Option (names = { "-s", "--stopPort" },
           paramLabel = "Stop Port",
           defaultValue = "" + JettyStarter.DEFAULT_STOP_PORT,
           description = "Internal port to watch for the shutdown command (default: ${DEFAULT-VALUE})")
  private Integer m_aStopPort;

  @Parameters (arity = "1", paramLabel = "command", description = "What to do. Options are ${COMPLETION-CANDIDATES}")
  private ECommand m_eCommand;

  // doing the business
  public Integer call () throws Exception
  {
    try
    {
      switch (m_eCommand)
      {
        case start:
        {
          final JettyStarter js = new JettyStarter ("Standalone TOOP Connector NG");
          js.setPort (m_aStartPort.intValue ())
            .setStopPort (m_aStopPort.intValue ())
            .setSessionCookieName ("TOOP_TC_SESSION")
            .setContainerIncludeJarPattern (JettyStarter.CONTAINER_INCLUDE_JAR_PATTERN_ALL);

          final String sPath = "WEB-INF/web.xml";
          final ClassPathResource aCPR = new ClassPathResource (sPath);
          if (!aCPR.exists ())
            throw new IllegalStateException ();

          final String sURL = aCPR.getAsURL ().toExternalForm ();
          // Is "file:/C:/dev/git/tc-jetty/target/classes/WEB-INF/web.xml" in
          // Eclipse
          // Is
          // "jar:file:/C:/dev/git/tc-jetty/target/tc-jetty-2.0.0-SNAPSHOT-jar-with-dependencies.jar!/WEB-INF/web.xml"
          // standalone
          if (LOGGER.isDebugEnabled ())
            LOGGER.debug ("Using web.xml resource: '" + sURL + "'");
          js.setWebXmlResource (aCPR.getAsURL ().toExternalForm ());

          // Go go go
          js.run ();
          break;
        }
        case stop:
        {
          new JettyStopper ().setStopPort (m_aStopPort.intValue ()).run ();
          break;
        }
        default:
          throw new IllegalStateException ("Don't know how to do that");
      }
    }
    catch (final Exception ex)
    {
      LOGGER.error ("Error running TC NG Standalone", ex);
    }
    return Integer.valueOf (0);
  }

  public static void main (final String [] aArgs)
  {
    final CommandLine cmd = new CommandLine (new MainTC ());
    cmd.setCaseInsensitiveEnumValuesAllowed (true);
    final int nExitCode = cmd.execute (aArgs);
    System.exit (nExitCode);
  }
}
