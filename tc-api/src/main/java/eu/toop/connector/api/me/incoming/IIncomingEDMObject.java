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

import javax.annotation.Nonnull;

import com.helger.commons.annotation.MustImplementEqualsAndHashcode;
import com.helger.commons.annotation.Nonempty;

/**
 * Base interface for incoming EDM objects.
 *
 * @author Philip Helger
 */
@MustImplementEqualsAndHashcode
public interface IIncomingEDMObject
{
  /**
   * @return The AS4 Content-ID of the AS4 MIME part containing the EDM object.
   */
  @Nonnull
  @Nonempty
  String getTopLevelContentID ();

  /**
   * @return The incoming metadata associated with this request. Never
   *         <code>null</code>.
   */
  @Nonnull
  IMEIncomingTransportMetadata getMetadata ();
}
