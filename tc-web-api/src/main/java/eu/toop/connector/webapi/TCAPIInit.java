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
package eu.toop.connector.webapi;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;

import com.helger.photon.api.APIDescriptor;
import com.helger.photon.api.APIPath;
import com.helger.photon.api.IAPIRegistry;

import eu.toop.connector.webapi.as4.ApiPostSend;
import eu.toop.connector.webapi.dsd.ApiGetDsdDpByCountry;
import eu.toop.connector.webapi.dsd.ApiGetDsdDpByDPType;
import eu.toop.connector.webapi.smp.ApiGetSmpDocTypes;
import eu.toop.connector.webapi.smp.ApiGetSmpEndpoints;
import eu.toop.connector.webapi.user.ApiPostUserSubmitEdm;
import eu.toop.connector.webapi.validation.ApiPostValidateEdm;

/**
 * Register all APIs
 *
 * @author Philip Helger
 */
@Immutable
public final class TCAPIInit
{
  private TCAPIInit ()
  {}

  public static void initAPI (@Nonnull final IAPIRegistry aAPIRegistry)
  {
    // DSD stuff
    aAPIRegistry.registerAPI (new APIDescriptor (APIPath.get ("/dsd/dp/{datasetType}/by-country/{country}"), ApiGetDsdDpByCountry.class));
    aAPIRegistry.registerAPI (new APIDescriptor (APIPath.get ("/dsd/dp/{datasetType}/by-dp-type/{dpType}"), ApiGetDsdDpByDPType.class));

    // SMP stuff
    aAPIRegistry.registerAPI (new APIDescriptor (APIPath.get ("/smp/doctypes/{pid}"), ApiGetSmpDocTypes.class));
    aAPIRegistry.registerAPI (new APIDescriptor (APIPath.get ("/smp/endpoints/{pid}/{doctypeid}"), ApiGetSmpEndpoints.class));

    // Validation stuff
    aAPIRegistry.registerAPI (new APIDescriptor (APIPath.post ("/validate/request"), new ApiPostValidateEdm (ETCEdmType.REQUEST)));
    aAPIRegistry.registerAPI (new APIDescriptor (APIPath.post ("/validate/response"), new ApiPostValidateEdm (ETCEdmType.RESPONSE)));
    aAPIRegistry.registerAPI (new APIDescriptor (APIPath.post ("/validate/error"), new ApiPostValidateEdm (ETCEdmType.ERROR_RESPONSE)));

    // AS4 stuff
    aAPIRegistry.registerAPI (new APIDescriptor (APIPath.post ("/send"), ApiPostSend.class));

    // User stuff
    aAPIRegistry.registerAPI (new APIDescriptor (APIPath.post ("/user/submit/request"), new ApiPostUserSubmitEdm (ETCEdmType.REQUEST)));
    aAPIRegistry.registerAPI (new APIDescriptor (APIPath.post ("/user/submit/response"), new ApiPostUserSubmitEdm (ETCEdmType.RESPONSE)));
    aAPIRegistry.registerAPI (new APIDescriptor (APIPath.post ("/user/submit/error"),
                                                 new ApiPostUserSubmitEdm (ETCEdmType.ERROR_RESPONSE)));
  }
}
