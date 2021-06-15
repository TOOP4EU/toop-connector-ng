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
package eu.toop.connector.mem.external;

/**
 * @author myildiz
 */
public final class MEMConstants {

  public static final String MEM_AS4_SUFFIX = "message-exchange.toop.eu";
  public static final String MEM_PARTY_ROLE = "http://www.toop.eu/edelivery/backend";
  public static final String GW_PARTY_ROLE = "http://www.toop.eu/edelivery/gateway";

  public static final String ACTION_SUBMIT = "Submit";
  public static final String ACTION_DELIVER = "Deliver";
  // this is recommended to be a Relay instead of Notify
  // but its kept like this for a while
  public static final String ACTION_RELAY = "Notify";
  public static final String ACTION_SUBMISSION_RESULT = "SubmissionResult";
  public static final String SERVICE = "http://www.toop.eu/edelivery/bit";

  private MEMConstants() {
  }
}
