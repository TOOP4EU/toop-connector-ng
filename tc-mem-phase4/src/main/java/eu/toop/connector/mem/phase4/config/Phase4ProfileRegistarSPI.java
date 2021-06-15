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
package eu.toop.connector.mem.phase4.config;

import java.util.function.Supplier;

import javax.annotation.Nonnull;

import com.helger.commons.annotation.IsSPIImplementation;
import com.helger.phase4.model.pmode.IPModeIDProvider;
import com.helger.phase4.profile.AS4Profile;
import com.helger.phase4.profile.IAS4Profile;
import com.helger.phase4.profile.IAS4ProfileRegistrar;
import com.helger.phase4.profile.IAS4ProfileRegistrarSPI;
import com.helger.phase4.profile.IAS4ProfileValidator;

/**
 * TOOP specific implementation of {@link IAS4ProfileRegistrarSPI}.
 *
 * @author Philip Helger
 */
@IsSPIImplementation
public final class Phase4ProfileRegistarSPI implements IAS4ProfileRegistrarSPI
{
  public static final String AS4_PROFILE_ID = "toop1";
  public static final String AS4_PROFILE_NAME = "TOOP v1";

  public void registerAS4Profile (@Nonnull final IAS4ProfileRegistrar aRegistrar)
  {
    final Supplier <? extends IAS4ProfileValidator> aProfileValidatorProvider = () -> null;
    final IPModeIDProvider aPModeIDProvider = IPModeIDProvider.DEFAULT_DYNAMIC;
    final IAS4Profile aProfile = new AS4Profile (AS4_PROFILE_ID,
                                                 AS4_PROFILE_NAME,
                                                 aProfileValidatorProvider,
                                                 (i, r, a) -> TOOPPMode.createTOOPMode (i, r, a, i + "-" + r, true),
                                                 aPModeIDProvider,
                                                 false);
    aRegistrar.registerProfile (aProfile);
    aRegistrar.setDefaultProfile (aProfile);
  }
}
