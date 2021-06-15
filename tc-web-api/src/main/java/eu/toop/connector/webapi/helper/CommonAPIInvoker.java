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
package eu.toop.connector.webapi.helper;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;

import com.helger.commons.callback.IThrowingRunnable;
import com.helger.commons.datetime.PDTFactory;
import com.helger.commons.timing.StopWatch;
import com.helger.json.IJsonObject;
import com.helger.phive.json.PhiveJsonHelper;

@Immutable
public final class CommonAPIInvoker
{
  private CommonAPIInvoker ()
  {}

  public static void invoke (@Nonnull final IJsonObject aJson, @Nonnull final IThrowingRunnable <Exception> r)
  {
    final ZonedDateTime aQueryDT = PDTFactory.getCurrentZonedDateTimeUTC ();
    final StopWatch aSW = StopWatch.createdStarted ();
    try
    {
      r.run ();
    }
    catch (final Exception ex)
    {
      aJson.add (AbstractTCAPIInvoker.JSON_SUCCESS, false);
      aJson.addJson ("exception", PhiveJsonHelper.getJsonStackTrace (ex));
    }
    aSW.stop ();

    aJson.add ("invocationDateTime", DateTimeFormatter.ISO_ZONED_DATE_TIME.format (aQueryDT));
    aJson.add ("invocationDurationMillis", aSW.getMillis ());
  }
}
