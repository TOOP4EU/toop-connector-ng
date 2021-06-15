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

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;

import com.helger.commons.exception.InitializationException;
import com.helger.commons.io.resource.ClassPathResource;
import com.helger.settings.ISettings;
import com.helger.settings.exchange.properties.SettingsPersistenceProperties;

/**
 * This class contains global TC (TOOP Connector) server constants.
 *
 * @author Philip Helger
 */
@Immutable
public final class CTC
{
  public static final String TOOP_CONNECTOR_VERSION_FILENAME = "toop-connector-version.properties";

  private static final String VERSION_NUMBER;
  private static final String TIMESTAMP;

  static
  {
    // Read version number
    final SettingsPersistenceProperties aSPP = new SettingsPersistenceProperties ();
    final ISettings aVersionProps = aSPP.readSettings (new ClassPathResource (TOOP_CONNECTOR_VERSION_FILENAME));
    VERSION_NUMBER = aVersionProps.getAsString ("version");
    if (VERSION_NUMBER == null)
      throw new InitializationException ("Error determining TOOP Connector NG version number!");
    TIMESTAMP = aVersionProps.getAsString ("timestamp");
    if (TIMESTAMP == null)
      throw new InitializationException ("Error determining TOOP Connector NG ´timestamp!");
  }

  private CTC ()
  {}

  /**
   * @return The version number of the TC server read from the internal
   *         properties file. Never <code>null</code>. This is the same as the
   *         Maven project version.
   */
  @Nonnull
  public static String getVersionNumber ()
  {
    return VERSION_NUMBER;
  }

  /**
   * @return The build timestamp of the TC server read from the internal
   *         properties file. Never <code>null</code>.
   */
  @Nonnull
  public static String getBuildTimestamp ()
  {
    return TIMESTAMP;
  }
}
