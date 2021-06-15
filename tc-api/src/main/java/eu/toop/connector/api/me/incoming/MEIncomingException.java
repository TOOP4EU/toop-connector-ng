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
package eu.toop.connector.api.me.incoming;

import javax.annotation.Nullable;

import eu.toop.connector.api.me.MEException;

/**
 * Exception when receiving messages via ME
 *
 * @author Philip Helger
 */
public class MEIncomingException extends MEException
{
  public MEIncomingException (@Nullable final String sMsg)
  {
    super (sMsg);
  }

  public MEIncomingException (@Nullable final String sMsg, @Nullable final Throwable aCause)
  {
    super (sMsg, aCause);
  }
}
