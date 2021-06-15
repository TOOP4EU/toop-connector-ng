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
