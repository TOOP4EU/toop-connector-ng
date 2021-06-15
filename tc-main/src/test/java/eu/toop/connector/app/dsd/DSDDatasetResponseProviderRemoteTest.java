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
package eu.toop.connector.app.dsd;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import com.helger.commons.collection.impl.ICommonsSet;

import eu.toop.connector.api.dsd.DSDDatasetResponse;
import eu.toop.connector.api.error.LoggingTCErrorHandler;

/**
 * Test class of class {@link DSDDatasetResponseProviderRemote}.
 * 
 * @author Philip Helger
 */
public final class DSDDatasetResponseProviderRemoteTest
{
  @Test
  public void testSimple ()
  {
    final ICommonsSet <DSDDatasetResponse> aResp = new DSDDatasetResponseProviderRemote ().getAllDatasetResponsesByCountry ("test",
                                                                                                                   "REGISTERED_ORGANIZATION_TYPE",
                                                                                                                   null,
                                                                                                                   LoggingTCErrorHandler.INSTANCE);
    assertNotNull (aResp);
  }
}
