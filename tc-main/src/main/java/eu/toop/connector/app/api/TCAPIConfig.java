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
package eu.toop.connector.app.api;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.GuardedBy;

import com.helger.commons.ValueEnforcer;
import com.helger.commons.concurrent.SimpleReadWriteLock;

import eu.toop.connector.api.dd.IDDServiceGroupHrefProvider;
import eu.toop.connector.api.dd.IDDServiceMetadataProvider;
import eu.toop.connector.api.dsd.IDSDDatasetResponseProvider;
import eu.toop.connector.api.validation.IVSValidator;
import eu.toop.connector.app.dsd.DSDDatasetResponseProviderRemote;
import eu.toop.connector.app.smp.DDServiceGroupHrefProviderSMP;
import eu.toop.connector.app.smp.DDServiceMetadataProviderSMP;
import eu.toop.connector.app.validation.TCValidator;

/**
 * Global TOOP Connector NG API configuration.<br>
 * This configuration is e.g. changed by the TOOP Simulator to install "mock"
 * handler.
 *
 * @author Philip Helger
 */
public final class TCAPIConfig
{
  private static final SimpleReadWriteLock s_aRWLock = new SimpleReadWriteLock ();

  @GuardedBy ("s_aRWLock")
  private static IDSDDatasetResponseProvider s_aDSDPartyIDProvider = new DSDDatasetResponseProviderRemote ();
  @GuardedBy ("s_aRWLock")
  private static IDDServiceGroupHrefProvider s_aDDSGHrefProvider = new DDServiceGroupHrefProviderSMP ();
  @GuardedBy ("s_aRWLock")
  private static IDDServiceMetadataProvider s_aDDSMProvider = new DDServiceMetadataProviderSMP ();
  @GuardedBy ("s_aRWLock")
  private static IVSValidator s_aValidator = new TCValidator ();

  private TCAPIConfig ()
  {}

  @Nonnull
  public static IDSDDatasetResponseProvider getDSDDatasetResponseProvider ()
  {
    return s_aRWLock.readLockedGet ( () -> s_aDSDPartyIDProvider);
  }

  public static void setDSDDatasetResponseProvider (@Nonnull final IDSDDatasetResponseProvider aProvider)
  {
    ValueEnforcer.notNull (aProvider, "IDSDDatasetResponseProvider");
    s_aRWLock.writeLockedGet ( () -> s_aDSDPartyIDProvider = aProvider);
  }

  @Nonnull
  public static IDDServiceGroupHrefProvider getDDServiceGroupHrefProvider ()
  {
    return s_aRWLock.readLockedGet ( () -> s_aDDSGHrefProvider);
  }

  public static void setDDServiceGroupHrefProvider (@Nonnull final IDDServiceGroupHrefProvider aProvider)
  {
    ValueEnforcer.notNull (aProvider, "IDDServiceGroupHrefProvider");
    s_aRWLock.writeLockedGet ( () -> s_aDDSGHrefProvider = aProvider);
  }

  @Nonnull
  public static IDDServiceMetadataProvider getDDServiceMetadataProvider ()
  {
    return s_aRWLock.readLockedGet ( () -> s_aDDSMProvider);
  }

  public static void setDDServiceMetadataProvider (@Nonnull final IDDServiceMetadataProvider aProvider)
  {
    ValueEnforcer.notNull (aProvider, "IDDServiceMetadataProvider");
    s_aRWLock.writeLockedGet ( () -> s_aDDSMProvider = aProvider);
  }

  @Nonnull
  public static IVSValidator getVSValidator ()
  {
    return s_aRWLock.readLockedGet ( () -> s_aValidator);
  }

  public static void setVSValidator (@Nonnull final IVSValidator aValidator)
  {
    ValueEnforcer.notNull (aValidator, "IVSValidator");
    s_aRWLock.writeLockedGet ( () -> s_aValidator = aValidator);
  }
}
