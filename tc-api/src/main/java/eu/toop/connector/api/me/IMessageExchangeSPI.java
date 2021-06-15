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
package eu.toop.connector.api.me;

import javax.annotation.Nonnull;
import javax.servlet.ServletContext;

import com.helger.commons.annotation.IsSPIInterface;
import com.helger.commons.annotation.Nonempty;

import eu.toop.connector.api.me.incoming.IMEIncomingHandler;
import eu.toop.connector.api.me.model.MEMessage;
import eu.toop.connector.api.me.outgoing.IMERoutingInformation;
import eu.toop.connector.api.me.outgoing.MEOutgoingException;

/**
 * Abstract API to be implemented for sending and receiving AS4 messages. This
 * interface is used to differentiate the different AS4 implementations and
 * integrations like Holodeck and phase4. It requires the usage of Java SPI
 * technology for registration.
 *
 * @author Philip Helger
 */
@IsSPIInterface
public interface IMessageExchangeSPI
{
  /**
   * @return The unique ID of the SPI implementation, so that it can be
   *         referenced from a configuration file. The implementer must ensure
   *         the uniqueness of the ID.
   */
  @Nonnull
  @Nonempty
  String getID ();

  /**
   * Register an incoming handler that takes the request/response to handle. The
   * differentiation between step 2/4 and 4/4 must be inside of the SPI
   * implementation. This method is only called once for the chosen
   * implementation, so the implementation can act as an "init" method and
   * perform further implementation activities. If this method is not called, it
   * is ensured that {@link #sendOutgoing(IMERoutingInformation, MEMessage)} of
   * this implementation are also never called.
   *
   * @param aServletContext
   *        The servlet context in which the handler should be registered. Never
   *        <code>null</code>.
   * @param aIncomingHandler
   *        The handler to use. May not be <code>null</code>.
   */
  void registerIncomingHandler (@Nonnull ServletContext aServletContext, @Nonnull IMEIncomingHandler aIncomingHandler);

  /**
   * Trigger the message transmission in step 1/4 and 3/4.
   *
   * @param aRoutingInfo
   *        Routing information. May not be <code>null</code>.
   * @param aMessage
   *        The message to be exchanged. May not be <code>null</code>.
   * @throws MEOutgoingException
   *         In case of error.
   */
  void sendOutgoing (@Nonnull IMERoutingInformation aRoutingInfo,
                     @Nonnull MEMessage aMessage) throws MEOutgoingException;

  /**
   * Shutdown the Message Exchange.
   *
   * @param aServletContext
   *        The servlet context in which the handler should be registered. Never
   *        <code>null</code>.
   */
  void shutdown (@Nonnull ServletContext aServletContext);
}
