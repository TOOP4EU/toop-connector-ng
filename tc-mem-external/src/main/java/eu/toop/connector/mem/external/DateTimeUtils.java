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
