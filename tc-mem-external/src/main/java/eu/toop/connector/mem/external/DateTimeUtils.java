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
package eu.toop.connector.mem.external;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import javax.annotation.Nonnull;

import com.helger.commons.annotation.Nonempty;

/**
 * Utilities related to date-time formatting, parsing etc..
 *
 * @author myildiz on 16.02.2018.
 */
public final class DateTimeUtils {
  private DateTimeUtils() {
  }

  /**
   * Create the current date time string in format
   *
   * uuuu-MM-dd'T'HH:mm:ss.SSSX
   *
   *
   * @return current date time with milliseconds and timezone in UTC
   */
  @Nonnull
  @Nonempty
  public static String getCurrentTimestamp() {
    final ZonedDateTime now = ZonedDateTime.now(ZoneOffset.UTC);
    return now.format(DateTimeFormatter.ofPattern("uuuu-MM-dd'T'HH:mm:ss.SSSX", Locale.US));
  }
}
