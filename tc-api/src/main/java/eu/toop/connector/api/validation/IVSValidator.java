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
package eu.toop.connector.api.validation;

import java.util.Locale;

import javax.annotation.Nonnull;

import com.helger.phive.api.executorset.VESID;
import com.helger.phive.api.result.ValidationResultList;

/**
 * TC Validation Service
 *
 * @author Philip Helger
 */
public interface IVSValidator
{
  /**
   * Perform validation
   *
   * @param aVESID
   *        VESID to use.
   * @param aPayload
   *        Payload to validate.
   * @param aDisplayLocale
   *        Display locale for the error message.
   * @return A non-<code>null</code> result list.
   */
  @Nonnull
  ValidationResultList validate (@Nonnull VESID aVESID, @Nonnull byte [] aPayload, @Nonnull Locale aDisplayLocale);
}
