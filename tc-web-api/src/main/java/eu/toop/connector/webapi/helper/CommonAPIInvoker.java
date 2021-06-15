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
