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
package eu.toop.connector.api.me.incoming;

import javax.annotation.Nonnull;

/**
 * The callback handler for incoming messages from the AS4 Gateway. An
 * implementation of this interface must be provided when calling
 * "TCInit.initGlobally". The default implementation is
 * "TCIncomingHandlerViaHttp". If you are embedding the TC into your application
 * you must provide an implementation of this interface.
 *
 * @author Philip Helger
 */
public interface IMEIncomingHandler
{
  /**
   * Handle an incoming request for step 2/4 (on DP side).
   *
   * @param aRequest
   *        The request to handle. Never <code>null</code>.
   * @throws MEIncomingException
   *         In case of error.
   */
  void handleIncomingRequest (@Nonnull IncomingEDMRequest aRequest) throws MEIncomingException;

  /**
   * Handle an incoming response for step 4/4 (on DC side).
   *
   * @param aResponse
   *        The response to handle. Contains attachments and metadata. Never
   *        <code>null</code>.
   * @throws MEIncomingException
   *         In case of error.
   */
  void handleIncomingResponse (@Nonnull IncomingEDMResponse aResponse) throws MEIncomingException;

  /**
   * Handle an incoming error response for step 4/4 (on DC side).
   *
   * @param aErrorResponse
   *        The response to handle. Never <code>null</code>.
   * @throws MEIncomingException
   *         In case of error.
   */
  void handleIncomingErrorResponse (@Nonnull IncomingEDMErrorResponse aErrorResponse) throws MEIncomingException;
}
