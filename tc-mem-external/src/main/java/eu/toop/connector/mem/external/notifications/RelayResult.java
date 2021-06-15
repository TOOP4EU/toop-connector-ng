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
package eu.toop.connector.mem.external.notifications;

/**
 * A java representation of a notification C2 --- C3 message relay. See TOOP AS4
 * GW backend interface specification
 *
 * @author yerlibilgin
 */
public class RelayResult extends Notification {

  private String shortDescription;
  private String severity;

  public String getShortDescription() {
    return shortDescription;
  }

  public void setShortDescription(final String shortDescription) {
    this.shortDescription = shortDescription;
  }

  public void setSeverity(final String severity) {
    this.severity = severity;
  }

  public String getSeverity() {
    return severity;
  }
}
