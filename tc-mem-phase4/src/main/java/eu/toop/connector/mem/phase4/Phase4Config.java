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
package eu.toop.connector.mem.phase4;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.helger.config.IConfig;
import com.helger.phase4.config.AS4Configuration;
import com.helger.phase4.crypto.AS4CryptoFactoryProperties;
import com.helger.phase4.crypto.AS4CryptoProperties;
import com.helger.phase4.crypto.IAS4CryptoFactory;
import com.helger.security.keystore.EKeyStoreType;

import eu.toop.connector.api.TCConfig;

/**
 * Wrapper to access the configuration for the phase4 module.
 *
 * @author Philip Helger
 */
public final class Phase4Config
{
  private Phase4Config ()
  {}

  @Nonnull
  public static IConfig getConfig ()
  {
    return AS4Configuration.getConfig ();
  }

  @Nullable
  public static String getDataPath ()
  {
    return getConfig ().getAsString ("phase4.datapath");
  }

  public static boolean isHttpDebugEnabled ()
  {
    return getConfig ().getAsBoolean ("phase4.debug.http", false);
  }

  public static boolean isDebugLogIncoming ()
  {
    return getConfig ().getAsBoolean ("phase4.debug.incoming", false);
  }

  @Nullable
  public static String getDumpPathIncoming ()
  {
    return getConfig ().getAsString ("phase4.dump.incoming.path");
  }

  @Nullable
  public static String getDumpPathOutgoing ()
  {
    return getConfig ().getAsString ("phase4.dump.outgoing.path");
  }

  @Nullable
  public static String getFromPartyID ()
  {
    // Was added in 2.0.0-rc3
    String ret = getConfig ().getAsString ("phase4.send.fromparty.id");
    if (ret == null)
    {
      // Fallback to old version (prior to rc3)
      ret = TCConfig.getConfig ().getAsString ("toop.mem.as4.tc.partyid");
    }
    return ret;
  }

  /**
   * @return The <code>From/PartyId/@type</code> for receiving party id
   * @since 2.0.2
   */
  @Nullable
  public static String getFromPartyIDType ()
  {
    return getConfig ().getAsString ("phase4.send.fromparty.id.type");
  }

  /**
   * @return The <code>To/PartyId/@type</code> for receiving party id
   * @since 2.0.2
   */
  @Nullable
  public static String getToPartyIDType ()
  {
    return getConfig ().getAsString ("phase4.send.toparty.id.type");
  }

  /**
   * @return Optional folder where to store responses to
   */
  @Nullable
  public static String getSendResponseFolderName ()
  {
    // Can be relative or absolute
    return getConfig ().getAsString ("phase4.send.response.folder");
  }

  // Keystore stuff
  @Nonnull
  public static EKeyStoreType getKeyStoreType ()
  {
    return EKeyStoreType.getFromIDCaseInsensitiveOrDefault (getConfig ().getAsString ("phase4.keystore.type"), EKeyStoreType.JKS);
  }

  @Nullable
  public static String getKeyStorePath ()
  {
    return getConfig ().getAsString ("phase4.keystore.path");
  }

  @Nullable
  public static String getKeyStorePassword ()
  {
    return getConfig ().getAsString ("phase4.keystore.password");
  }

  @Nullable
  public static String getKeyStoreKeyAlias ()
  {
    return getConfig ().getAsString ("phase4.keystore.key-alias");
  }

  @Nullable
  public static String getKeyStoreKeyPassword ()
  {
    return getConfig ().getAsString ("phase4.keystore.key-password");
  }

  // Truststore stuff
  @Nonnull
  public static EKeyStoreType getTrustStoreType ()
  {
    return EKeyStoreType.getFromIDCaseInsensitiveOrDefault (getConfig ().getAsString ("phase4.truststore.type"), EKeyStoreType.JKS);
  }

  @Nullable
  public static String getTrustStorePath ()
  {
    return getConfig ().getAsString ("phase4.truststore.path");
  }

  @Nullable
  public static String getTrustStorePassword ()
  {
    return getConfig ().getAsString ("phase4.truststore.password");
  }

  @Nonnull
  public static IAS4CryptoFactory getCryptoFactory ()
  {
    return new AS4CryptoFactoryProperties (new AS4CryptoProperties ().setKeyStoreType (getKeyStoreType ())
                                                                     .setKeyStorePath (getKeyStorePath ())
                                                                     .setKeyStorePassword (getKeyStorePassword ())
                                                                     .setKeyAlias (getKeyStoreKeyAlias ())
                                                                     .setKeyPassword (getKeyStoreKeyPassword ())
                                                                     .setTrustStoreType (getTrustStoreType ())
                                                                     .setTrustStorePath (getTrustStorePath ())
                                                                     .setTrustStorePassword (getTrustStorePassword ()));
  }
}
