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

import javax.annotation.Nonnull;
import javax.servlet.ServletContext;

import com.helger.photon.api.IAPIRegistry;
import com.helger.photon.audit.AuditHelper;
import com.helger.photon.audit.DoNothingAuditor;
import com.helger.photon.core.servlet.WebAppListener;
import com.helger.photon.security.login.LoggedInUserManager;

import eu.toop.connector.api.TCConfig;
import eu.toop.connector.api.me.incoming.IMEIncomingHandler;
import eu.toop.connector.app.TCInit;
import eu.toop.connector.webapi.TCAPIInit;

/**
 * Global startup etc. listener.
 *
 * @author Philip Helger
 */
public class TCWebAppListener extends WebAppListener
{
  public TCWebAppListener ()
  {
    setHandleStatisticsOnEnd (false);
  }

  @Override
  protected String getDataPath (@Nonnull final ServletContext aSC)
  {
    String ret = TCConfig.WebApp.getDataPath ();
    if (ret == null)
    {
      // Fall back to servlet context path
      ret = super.getDataPath (aSC);
    }
    return ret;
  }

  @Override
  protected String getServletContextPath (final ServletContext aSC)
  {
    try
    {
      return super.getServletContextPath (aSC);
    }
    catch (final IllegalStateException ex)
    {
      // E.g. "Unpack WAR files" in Tomcat is disabled
      return getDataPath (aSC);
    }
  }

  @Override
  protected void afterContextInitialized (final ServletContext aSC)
  {
    // Use default handler
    TCInit.initGlobally (aSC, (IMEIncomingHandler) null);

    // Don't write audit logs
    AuditHelper.setAuditor (new DoNothingAuditor (LoggedInUserManager.getInstance ()));
  }

  @Override
  protected void initAPI (@Nonnull final IAPIRegistry aAPIRegistry)
  {
    TCAPIInit.initAPI (aAPIRegistry);
  }

  @Override
  protected void beforeContextDestroyed (final ServletContext aSC)
  {
    TCInit.shutdownGlobally (aSC);
  }
}
