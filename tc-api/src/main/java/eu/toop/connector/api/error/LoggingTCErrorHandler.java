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
package eu.toop.connector.api.error;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.helger.commons.error.level.EErrorLevel;
import com.helger.commons.log.LogHelper;

import eu.toop.edm.error.IToopErrorCode;

/**
 * Logging implementation of {@link ITCErrorHandler}
 * 
 * @author Philip Helger
 */
public class LoggingTCErrorHandler implements ITCErrorHandler
{
  public static final LoggingTCErrorHandler INSTANCE = new LoggingTCErrorHandler ();
  private static final Logger LOGGER = LoggerFactory.getLogger (LoggingTCErrorHandler.class);

  public void onMessage (@Nonnull final EErrorLevel eErrorLevel,
                         @Nonnull final String sMsg,
                         @Nullable final Throwable t,
                         @Nonnull final IToopErrorCode eCode)
  {
    LogHelper.log (LOGGER, eErrorLevel, sMsg, t);
  }
}
