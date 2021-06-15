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
package eu.toop.connector.app;

import java.net.InetAddress;
import java.net.URI;
import java.net.UnknownHostException;
import java.security.cert.X509Certificate;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.NotThreadSafe;
import javax.servlet.ServletContext;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.helger.commons.debug.GlobalDebug;
import com.helger.commons.error.level.EErrorLevel;
import com.helger.commons.exception.InitializationException;
import com.helger.commons.id.factory.GlobalIDFactory;
import com.helger.commons.id.factory.StringIDFromGlobalPersistentLongIDFactory;
import com.helger.commons.string.StringHelper;
import com.helger.commons.url.IURLProtocol;
import com.helger.commons.url.URLHelper;
import com.helger.commons.url.URLProtocolRegistry;
import com.helger.xservlet.requesttrack.RequestTracker;

import eu.toop.connector.api.TCConfig;
import eu.toop.connector.api.me.MessageExchangeManager;
import eu.toop.connector.api.me.incoming.IMEIncomingHandler;
import eu.toop.kafkaclient.ToopKafkaClient;
import eu.toop.kafkaclient.ToopKafkaSettings;

/**
 * Contains TOOP Connector global init and shutdown methods.
 *
 * @author Philip Helger
 */
@NotThreadSafe
public final class TCInit
{
  private static final Logger LOGGER = LoggerFactory.getLogger (TCInit.class);
  private static final AtomicBoolean INITED = new AtomicBoolean (false);
  private static String s_sLogPrefix;

  private TCInit ()
  {}

  /**
   * Globally init the TOOP Connector. Calling it, if it is already initialized
   * will thrown an exception.
   *
   * @param aServletContext
   *        The servlet context used for initialization. May not be
   *        <code>null</code> but maybe a mocked one.
   * @throws IllegalStateException
   *         If the TOOP Connector is already initialized
   * @throws InitializationException
   *         If any of the settings are totally bogus
   * @deprecated Since 2.0.0-rc4; Use
   *             {@link #initGlobally(ServletContext, IMEIncomingHandler)}
   *             instead
   */
  @Deprecated
  public static void initGlobally (@Nonnull final ServletContext aServletContext)
  {
    initGlobally (aServletContext, (IMEIncomingHandler) null);
  }

  /**
   * Globally init the TOOP Connector. Calling it, if it is already initialized
   * will thrown an exception.
   *
   * @param aServletContext
   *        The servlet context used for initialization. May not be
   *        <code>null</code> but maybe a mocked one.
   * @param aIncomingHandler
   *        The incoming handler to be used. If <code>null</code> the default of
   *        {@link TCIncomingHandlerViaHttp} will be used.
   * @throws IllegalStateException
   *         If the TOOP Connector is already initialized
   * @throws InitializationException
   *         If any of the settings are totally bogus
   * @since 2.0.0-rc4
   */
  public static void initGlobally (@Nonnull final ServletContext aServletContext, @Nullable final IMEIncomingHandler aIncomingHandler)
  {
    if (!INITED.compareAndSet (false, true))
      throw new IllegalStateException ("TOOP Connector NG is already initialized");

    GlobalIDFactory.setPersistentStringIDFactory (new StringIDFromGlobalPersistentLongIDFactory ("toop-tc-"));
    GlobalDebug.setDebugModeDirect (TCConfig.Global.isGlobalDebug ());
    GlobalDebug.setProductionModeDirect (TCConfig.Global.isGlobalProduction ());

    String sLogPrefix = TCConfig.Global.getToopInstanceName ();
    if (StringHelper.hasNoText (sLogPrefix))
    {
      // Get my IP address for debugging as default
      try
      {
        sLogPrefix = "[" + InetAddress.getLocalHost ().getHostAddress () + "] ";
      }
      catch (final UnknownHostException ex)
      {
        sLogPrefix = "";
      }
    }
    else
    {
      if (!sLogPrefix.startsWith ("["))
        sLogPrefix = "[" + sLogPrefix + "]";

      // Would have been trimmed when reading the properties file, so add
      // manually
      sLogPrefix += " ";
    }
    s_sLogPrefix = sLogPrefix;

    // Disable RequestTracker
    RequestTracker.getInstance ().getRequestTrackingMgr ().setLongRunningCheckEnabled (false);
    RequestTracker.getInstance ().getRequestTrackingMgr ().setParallelRunningRequestCheckEnabled (false);

    {
      // Init tracker client
      ToopKafkaSettings.setKafkaEnabled (TCConfig.Tracker.isToopTrackerEnabled ());
      if (TCConfig.Tracker.isToopTrackerEnabled ())
      {
        // Set tracker URL
        final String sToopTrackerUrl = TCConfig.Tracker.getToopTrackerUrl ();
        if (StringHelper.hasNoText (sToopTrackerUrl))
          throw new InitializationException ("If the tracker is enabled, the tracker URL MUST be provided in the configuration file!");
        // Consistency check - no protocol like "http://" or so may be present
        final IURLProtocol aProtocol = URLProtocolRegistry.getInstance ().getProtocol (sToopTrackerUrl);
        if (aProtocol != null)
          throw new InitializationException ("The tracker URL MUST NOT start with a protocol like '" + aProtocol.getProtocol () + "'!");
        ToopKafkaSettings.defaultProperties ().put (ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, sToopTrackerUrl);

        // Set the topic
        final String sToopTrackerTopic = TCConfig.Tracker.getToopTrackerTopic ();
        ToopKafkaSettings.setKafkaTopic (sToopTrackerTopic);
      }
    }

    {
      // Check R2D2 configuration
      final String sDirectoryURL = TCConfig.DSD.getDSDBaseUrl ();
      if (StringHelper.hasNoText (sDirectoryURL))
        throw new InitializationException ("The URL of the DSD Service is missing in the configuration file!");

      if (!TCConfig.R2D2.isR2D2UseDNS ())
      {
        final String sStaticEndpoint = TCConfig.R2D2.getR2D2StaticEndpointURL ();
        final X509Certificate aStaticCert = TCConfig.R2D2.getR2D2StaticCertificate ();
        if (URLHelper.getAsURL (sStaticEndpoint) != null && aStaticCert != null)
        {
          if (LOGGER.isInfoEnabled ())
            LOGGER.info ("Using static R2D2 target endpoint '" + sStaticEndpoint + "'");
        }
        else
        {
          final URI aSMPURI = TCConfig.R2D2.getR2D2SMPUrl ();
          if (aSMPURI != null)
          {
            if (LOGGER.isInfoEnabled ())
              LOGGER.info ("Using static R2D2 SMP address '" + aSMPURI.toString () + "'");
          }
          else
            throw new InitializationException ("Since the usage of SML/DNS is disabled, the fixed URL of the SMP or the static parameters to be used must be provided in the configuration file!");
        }
      }
    }

    // Init incoming message handler
    final IMEIncomingHandler aRealIncomingHandler = aIncomingHandler != null ? aIncomingHandler
                                                                             : new TCIncomingHandlerViaHttp (s_sLogPrefix);
    MessageExchangeManager.getConfiguredImplementation ().registerIncomingHandler (aServletContext, aRealIncomingHandler);

    ToopKafkaClient.send (EErrorLevel.INFO, () -> s_sLogPrefix + "TOOP Connector NG WebApp " + CTC.getVersionNumber () + " started");
  }

  /**
   * @return <code>true</code> if the TOOP Connector was initialized (or is
   *         currently initializing or is currently shutdown),
   *         <code>false</code> if not.
   */
  public static boolean isInitialized ()
  {
    return INITED.get ();
  }

  /**
   * Globally shutdown the TOOP Connector. Calling it, if it was not already
   * initialized will thrown an exception.
   *
   * @param aServletContext
   *        The servlet context used for shutdown. May not be <code>null</code>
   *        but maybe a mocked one.
   * @throws IllegalStateException
   *         If the TOOP Connector is not initialized
   */
  public static void shutdownGlobally (@Nonnull final ServletContext aServletContext)
  {
    if (!isInitialized ())
      throw new IllegalStateException ("TOOP Connector NG is not initialized");

    ToopKafkaClient.send (EErrorLevel.INFO, () -> s_sLogPrefix + "TOOP Connector NG shutting down");

    // Shutdown message exchange
    MessageExchangeManager.getConfiguredImplementation ().shutdown (aServletContext);

    // Shutdown tracker
    ToopKafkaClient.close ();

    s_sLogPrefix = null;

    if (!INITED.compareAndSet (true, false))
      throw new IllegalStateException ("TOOP Connector NG was already shutdown");
  }
}
