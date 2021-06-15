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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

/**
 * Test class for class {@link TCConfig}.
 *
 * @author Philip Helger
 */
public final class TCConfigTest
{
  @Test
  public void testBasic ()
  {
    assertEquals ("http://directory.central.toop/pd", TCConfig.DSD.getDSDBaseUrl ());
    assertFalse (TCConfig.R2D2.isR2D2UseDNS ());
    assertNotNull (TCConfig.getIdentifierFactory ());
  }
}
