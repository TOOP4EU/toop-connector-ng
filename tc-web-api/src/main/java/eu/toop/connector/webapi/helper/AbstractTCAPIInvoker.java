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
package eu.toop.connector.webapi.helper;

import java.io.IOException;
import java.util.Map;

import javax.annotation.Nonnull;

import com.helger.commons.CGlobal;
import com.helger.commons.annotation.Nonempty;
import com.helger.commons.error.level.EErrorLevel;
import com.helger.commons.http.CHttp;
import com.helger.commons.http.EHttpMethod;
import com.helger.commons.timing.StopWatch;
import com.helger.json.IJsonObject;
import com.helger.json.serialize.JsonWriterSettings;
import com.helger.photon.api.IAPIDescriptor;
import com.helger.photon.api.IAPIExecutor;
import com.helger.photon.app.PhotonUnifiedResponse;
import com.helger.servlet.response.UnifiedResponse;
import com.helger.web.scope.IRequestWebScopeWithoutResponse;

import eu.toop.kafkaclient.ToopKafkaClient;

/**
 * Abstract base invoker for TC REST API
 *
 * @author Philip Helger
 */
public abstract class AbstractTCAPIInvoker implements IAPIExecutor
{
  protected static final String JSON_SUCCESS = "success";

  @Nonnull
  public abstract IJsonObject invokeAPI (@Nonnull final IAPIDescriptor aAPIDescriptor,
                                         @Nonnull @Nonempty final String sPath,
                                         @Nonnull final Map <String, String> aPathVariables,
                                         @Nonnull final IRequestWebScopeWithoutResponse aRequestScope) throws IOException;

  public final void invokeAPI (@Nonnull final IAPIDescriptor aAPIDescriptor,
                               @Nonnull @Nonempty final String sPath,
                               @Nonnull final Map <String, String> aPathVariables,
                               @Nonnull final IRequestWebScopeWithoutResponse aRequestScope,
                               @Nonnull final UnifiedResponse aUnifiedResponse) throws Exception
  {
    final StopWatch aSW = StopWatch.createdStarted ();

    final IJsonObject aJson = invokeAPI (aAPIDescriptor, sPath, aPathVariables, aRequestScope);

    final PhotonUnifiedResponse aPUR = (PhotonUnifiedResponse) aUnifiedResponse;
    aPUR.setJsonWriterSettings (new JsonWriterSettings ().setIndentEnabled (true));
    aPUR.json (aJson);

    final boolean bSuccess = aJson.getAsBoolean (JSON_SUCCESS, false);
    if (!bSuccess)
      aPUR.setAllowContentOnStatusCode (true).setStatus (CHttp.HTTP_BAD_REQUEST);
    else
      if (aRequestScope.getHttpMethod () == EHttpMethod.GET)
        aPUR.enableCaching (3 * CGlobal.SECONDS_PER_HOUR);
      else
        aPUR.disableCaching ();

    aSW.stop ();

    ToopKafkaClient.send (bSuccess ? EErrorLevel.INFO : EErrorLevel.ERROR,
                          () -> "[API] Finished '" +
                                aAPIDescriptor.getPathDescriptor ().getAsURLString () +
                                "' after " +
                                aSW.getMillis () +
                                " milliseconds with " +
                                (bSuccess ? "success" : "error"));
  }
}
