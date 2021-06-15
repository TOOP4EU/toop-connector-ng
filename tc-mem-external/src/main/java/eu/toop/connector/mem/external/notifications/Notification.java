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

import java.io.Serializable;

import com.helger.commons.CGlobal;

import eu.toop.connector.mem.external.ResultType;

/**
 * @author yerlibilgin
 */
public class Notification implements Serializable {

  private static final long EXPIRATION_PERIOD = 5 * CGlobal.MILLISECONDS_PER_MINUTE;
  /**
   * The message id of the SUBMIT message (C1 --&gt; C2)
   */
  private String messageID;
  /**
   * The message id of the outbound message (C2 --&gt; C3)
   */
  private String refToMessageID;
  /**
   * The type of this notification
   */
  private ResultType result;
  /**
   * The context specific error code (or null in case of success)
   */
  private String errorCode;
  /**
   * Long description if any
   */
  private String description;

  /**
   * The local milliseconds time when this object was created
   */
  private final long creationTime;

  Notification() {
    creationTime = System.currentTimeMillis();
  }

  public String getRefToMessageID() {
    return refToMessageID;
  }

  public void setRefToMessageID(final String refToMessageID) {
    this.refToMessageID = refToMessageID;
  }

  public ResultType getResult() {
    return result;
  }

  public void setResult(final ResultType result) {
    this.result = result;
  }

  public String getErrorCode() {
    return errorCode;
  }

  public void setErrorCode(final String errorCode) {
    this.errorCode = errorCode;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(final String description) {
    this.description = description;
  }

  @Override
  public String toString() {
    return "Notification for " + refToMessageID;
  }

  public String getMessageID() {
    return messageID;
  }

  public void setMessageID(final String messageID) {
    this.messageID = messageID;
  }

  public boolean isExpired(final long currentTime) {
    return (currentTime - creationTime) > EXPIRATION_PERIOD;
  }
}
