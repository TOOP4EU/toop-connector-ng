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
package eu.toop.connector.app.validation;

import static org.junit.Assert.assertTrue;

import java.util.Locale;

import javax.annotation.Nonnull;

import org.junit.Test;

import com.helger.commons.io.resource.ClassPathResource;
import com.helger.commons.io.stream.StreamHelper;
import com.helger.phive.api.executorset.VESID;
import com.helger.phive.api.result.ValidationResultList;

/**
 * Test class for class {@link TCValidator}
 *
 * @author Philip Helger
 */
public final class TCValidatorTest
{
  @Nonnull
  private static ValidationResultList _validate (@Nonnull final VESID aVESID, @Nonnull final String sFilename)
  {
    return new TCValidator ().validate (aVESID, StreamHelper.getAllBytes (new ClassPathResource ("edm/" + sFilename)), Locale.US);
  }

  private static void _assumeSuccess (@Nonnull final ValidationResultList aRL)
  {
    // No errors and no warnings
    assertTrue ("Hmpf: " + aRL.toString (), aRL.containsNoFailure ());
  }

  private static void _assumeError (@Nonnull final ValidationResultList aRL)
  {
    // At least one error
    assertTrue ("Hmpf: " + aRL.toString (), aRL.containsAtLeastOneError ());
  }

  @Test
  public void testRequest ()
  {
    final VESID aVESID = TCValidationRules.VID_TOOP_EDM_REQUEST_210;
    _assumeSuccess (_validate (aVESID, "Concept Request_LP.xml"));
    _assumeSuccess (_validate (aVESID, "Concept Request_NP.xml"));
    _assumeError (_validate (aVESID, "Concept Response.xml"));
    _assumeSuccess (_validate (aVESID, "Document Request_LP.xml"));
    _assumeSuccess (_validate (aVESID, "Document Request_NP.xml"));
    _assumeError (_validate (aVESID, "Document Response.xml"));
    _assumeError (_validate (aVESID, "Error Response 1.xml"));
    _assumeError (_validate (aVESID, "Bogus.xml"));
  }

  @Test
  public void testResponse ()
  {
    final VESID aVESID = TCValidationRules.VID_TOOP_EDM_RESPONSE_210;
    _assumeError (_validate (aVESID, "Concept Request_LP.xml"));
    _assumeError (_validate (aVESID, "Concept Request_NP.xml"));
    _assumeSuccess (_validate (aVESID, "Concept Response.xml"));
    _assumeError (_validate (aVESID, "Document Request_LP.xml"));
    _assumeError (_validate (aVESID, "Document Request_NP.xml"));
    _assumeSuccess (_validate (aVESID, "Document Response.xml"));
    _assumeError (_validate (aVESID, "Error Response 1.xml"));
    _assumeError (_validate (aVESID, "Bogus.xml"));
  }

  @Test
  public void testErrorResponse ()
  {
    final VESID aVESID = TCValidationRules.VID_TOOP_EDM_ERROR_RESPONSE_210;
    _assumeError (_validate (aVESID, "Concept Request_LP.xml"));
    _assumeError (_validate (aVESID, "Concept Request_NP.xml"));
    _assumeError (_validate (aVESID, "Concept Response.xml"));
    _assumeError (_validate (aVESID, "Document Request_LP.xml"));
    _assumeError (_validate (aVESID, "Document Request_NP.xml"));
    _assumeError (_validate (aVESID, "Document Response.xml"));
    _assumeSuccess (_validate (aVESID, "Error Response 1.xml"));
    _assumeError (_validate (aVESID, "Bogus.xml"));
  }
}
