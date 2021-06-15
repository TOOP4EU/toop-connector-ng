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
package eu.toop.connector.api.me;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.helger.commons.annotation.Nonempty;
import com.helger.commons.id.IHasID;
import com.helger.commons.lang.EnumHelper;
import com.helger.peppol.smp.ESMPTransportProfile;

/**
 * Contains all protocols supported by MP
 *
 * @author Philip Helger
 */
public enum EMEProtocol implements IHasID <String>
{
  /**
   * AS4 using the common transport profile introduced by eSENS
   */
  AS4 ("as4", ESMPTransportProfile.TRANSPORT_PROFILE_BDXR_AS4.getID ());

  public static final EMEProtocol DEFAULT = AS4;

  private final String m_sID;
  private final String m_sTransportProfileID;

  EMEProtocol (@Nonnull @Nonempty final String sID, @Nonnull @Nonempty final String sTransportProfileID)
  {
    m_sID = sID;
    m_sTransportProfileID = sTransportProfileID;
  }

  @Nonnull
  @Nonempty
  public String getID ()
  {
    return m_sID;
  }

  /**
   * @return The transport profile ID for the SMP to be used for this MP
   *         protocol. Neither <code>null</code> nor empty.
   */
  @Nonnull
  @Nonempty
  public String getTransportProfileID ()
  {
    return m_sTransportProfileID;
  }

  @Nullable
  public static EMEProtocol getFromIDOrNull (@Nullable final String sID)
  {
    return EnumHelper.getFromIDOrNull (EMEProtocol.class, sID);
  }
}
