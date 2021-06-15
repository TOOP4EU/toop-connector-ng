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

import javax.annotation.Nonnull;

import eu.toop.connector.api.me.model.MEMessage;

/**
 * @author myildiz at 15.02.2018.
 */
public interface IMessageHandler {
  /**
   * implement this method to receive messages when an inbound message arrives to
   * the AS4 endpoint
   * 
   * @param meMessage the object that contains the payloads and their metadata´
   * @throws Exception in case of error
   */
  void handleMessage(@Nonnull MEMessage meMessage) throws Exception;
}
