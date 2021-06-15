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
package eu.toop.connector.mem.external.notifications;

import javax.annotation.Nonnull;

/**
 * Implement this interface and register it to the MEMDelegate in order to
 * receive Notifications about the dispatch of the outbound message to the inner
 * corner of the receiving side
 *
 * @author yerlibilgin
 */
public interface IRelayResultHandler {

  /**
   * Implement this method in order to receive Notifications about the dispatch of
   * the outbound message to the inner corner of the receiving side
   *
   * @param notification Notification relay result
   */
  void handleNotification(@Nonnull RelayResult notification);
}
