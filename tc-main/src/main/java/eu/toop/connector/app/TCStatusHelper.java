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
package eu.toop.connector.app;

import java.util.Map;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;

import com.helger.commons.annotation.ReturnsMutableCopy;
import com.helger.commons.collection.impl.CommonsLinkedHashMap;
import com.helger.commons.collection.impl.ICommonsOrderedMap;
import com.helger.commons.datetime.PDTFactory;
import com.helger.commons.datetime.PDTWebDateHelper;
import com.helger.commons.debug.GlobalDebug;
import com.helger.commons.system.SystemProperties;
import com.helger.config.source.res.IConfigurationSourceResource;
import com.helger.json.IJsonObject;
import com.helger.json.JsonObject;

import eu.toop.connector.api.TCConfig;

/**
 * Helper to create the TOOP Connector status reachable via the "/tc-status/"
 * servlet.
 *
 * @author Philip Helger
 */
@Immutable
public final class TCStatusHelper
{
  private TCStatusHelper ()
  {}

  @Nonnull
  @ReturnsMutableCopy
  public static IJsonObject getDefaultStatusData ()
  {
    final IJsonObject aStatusData = new JsonObject ();
    aStatusData.add ("status.datetime", PDTWebDateHelper.getAsStringXSD (PDTFactory.getCurrentZonedDateTimeUTC ()));
    aStatusData.add ("version.toop-connector", CTC.getVersionNumber ());
    aStatusData.add ("version.build-datetime", CTC.getBuildTimestamp ());
    aStatusData.add ("version.java", SystemProperties.getJavaVersion ());
    aStatusData.add ("global.debug", GlobalDebug.isDebugMode ());
    aStatusData.add ("global.production", GlobalDebug.isProductionMode ());

    // add all configuration items to status (all except passwords)
    final ICommonsOrderedMap <String, String> aVals = new CommonsLinkedHashMap <> ();
    TCConfig.getConfig ().forEachConfigurationValueProvider ( (aCVP, nPriority) -> {
      if (aCVP instanceof IConfigurationSourceResource)
      {
        final ICommonsOrderedMap <String, String> aAll = ((IConfigurationSourceResource) aCVP).getAllConfigItems ();
        for (final Map.Entry <String, String> aEntry : aAll.entrySet ())
        {
          // Never override, because highest priority values come first
          if (!aVals.containsKey (aEntry.getKey ()))
            aVals.put (aEntry);
        }
      }
    });

    // Maintain the retrieved order
    for (final Map.Entry <String, String> aEntry : aVals.entrySet ())
    {
      final String sKey = aEntry.getKey ();
      if (sKey.contains ("password"))
        aStatusData.add (aEntry.getKey (), "***");
      else
        aStatusData.add (aEntry.getKey (), aEntry.getValue ());
    }

    return aStatusData;
  }
}
