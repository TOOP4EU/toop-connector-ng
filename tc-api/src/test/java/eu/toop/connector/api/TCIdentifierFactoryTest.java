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
package eu.toop.connector.api;

import static org.junit.Assert.assertNotEquals;

import org.junit.Test;

import com.helger.peppolid.IParticipantIdentifier;

/**
 * Test class for class {@link TCIdentifierFactory}.
 *
 * @author Philip Helger
 */
public final class TCIdentifierFactoryTest
{
  @Test
  public void testBasic ()
  {
    final TCIdentifierFactory aIF = TCIdentifierFactory.INSTANCE_TC;

    final IParticipantIdentifier aPI1 = aIF.createParticipantIdentifier (null, "iso6523-actorid-upis::9999:elonia");
    final IParticipantIdentifier aPI2 = aIF.createParticipantIdentifier ("iso6523-actorid-upis", "9999:elonia");
    assertNotEquals (aPI1.getURIEncoded (), aPI2.getURIEncoded ());
  }
}
