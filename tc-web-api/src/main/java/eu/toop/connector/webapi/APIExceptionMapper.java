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
package eu.toop.connector.webapi;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.client.HttpResponseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.helger.commons.debug.GlobalDebug;
import com.helger.commons.state.EHandled;
import com.helger.commons.string.StringHelper;
import com.helger.photon.api.AbstractAPIExceptionMapper;
import com.helger.photon.api.InvokableAPIDescriptor;
import com.helger.servlet.response.UnifiedResponse;
import com.helger.web.scope.IRequestWebScopeWithoutResponse;

/**
 * Special API exception mapper for the SMP REST API.
 *
 * @author Philip Helger
 */
public class APIExceptionMapper extends AbstractAPIExceptionMapper
{
  private static final Logger LOGGER = LoggerFactory.getLogger (APIExceptionMapper.class);

  private static void _logRestException (@Nonnull final String sMsg, @Nonnull final Throwable t)
  {
    LOGGER.error (sMsg, t);
  }

  private static void _setSimpleTextResponse (@Nonnull final UnifiedResponse aUnifiedResponse,
                                              final int nStatusCode,
                                              @Nullable final String sContent)
  {
    if (GlobalDebug.isDebugMode ())
    {
      // With payload
      setSimpleTextResponse (aUnifiedResponse, nStatusCode, sContent);
      if (StringHelper.hasText (sContent))
        aUnifiedResponse.disableCaching ();
    }
    else
    {
      // No payload
      aUnifiedResponse.setStatus (nStatusCode);
    }
  }

  @Nonnull
  public EHandled applyExceptionOnResponse (@Nonnull final InvokableAPIDescriptor aInvokableDescriptor,
                                            @Nonnull final IRequestWebScopeWithoutResponse aRequestScope,
                                            @Nonnull final UnifiedResponse aUnifiedResponse,
                                            @Nonnull final Throwable aThrowable)
  {
    // From specific to general
    if (aThrowable instanceof HttpResponseException)
    {
      _logRestException ("HttpResponse exception", aThrowable);
      final HttpResponseException aEx = (HttpResponseException) aThrowable;
      _setSimpleTextResponse (aUnifiedResponse, aEx.getStatusCode (), aEx.getReasonPhrase ());
      return EHandled.HANDLED;
    }
    if (aThrowable instanceof APIParamException)
    {
      _logRestException ("Parameter exception", aThrowable);
      _setSimpleTextResponse (aUnifiedResponse,
                              HttpServletResponse.SC_BAD_REQUEST,
                              GlobalDebug.isDebugMode () ? getResponseEntityWithStackTrace (aThrowable)
                                                         : getResponseEntityWithoutStackTrace (aThrowable));
      return EHandled.HANDLED;
    }
    if (aThrowable instanceof RuntimeException)
    {
      _logRestException ("Runtime exception - " + aThrowable.getClass ().getName (), aThrowable);
      _setSimpleTextResponse (aUnifiedResponse,
                              HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                              GlobalDebug.isDebugMode () ? getResponseEntityWithStackTrace (aThrowable)
                                                         : getResponseEntityWithoutStackTrace (aThrowable));
      return EHandled.HANDLED;
    }

    // We don't know that exception
    return EHandled.UNHANDLED;
  }
}
