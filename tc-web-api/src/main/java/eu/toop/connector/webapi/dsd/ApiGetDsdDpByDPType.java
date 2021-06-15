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
package eu.toop.connector.webapi.dsd;

import java.util.Map;

import javax.annotation.Nonnull;

import com.helger.commons.annotation.Nonempty;
import com.helger.commons.collection.impl.ICommonsSet;
import com.helger.commons.string.StringHelper;
import com.helger.json.IJsonArray;
import com.helger.json.IJsonObject;
import com.helger.json.JsonArray;
import com.helger.json.JsonObject;
import com.helger.phive.json.PhiveJsonHelper;
import com.helger.photon.api.IAPIDescriptor;
import com.helger.web.scope.IRequestWebScopeWithoutResponse;

import eu.toop.connector.api.dsd.DSDDatasetResponse;
import eu.toop.connector.api.error.ITCErrorHandler;
import eu.toop.connector.app.api.TCAPIHelper;
import eu.toop.connector.webapi.APIParamException;
import eu.toop.connector.webapi.helper.AbstractTCAPIInvoker;
import eu.toop.connector.webapi.helper.CommonAPIInvoker;

/**
 * Search DSD participants by dataset and DP Type
 *
 * @author Philip Helger
 * @since 2.1.0
 */
public class ApiGetDsdDpByDPType extends AbstractTCAPIInvoker
{
  @Override
  public IJsonObject invokeAPI (@Nonnull final IAPIDescriptor aAPIDescriptor,
                                @Nonnull @Nonempty final String sPath,
                                @Nonnull final Map <String, String> aPathVariables,
                                @Nonnull final IRequestWebScopeWithoutResponse aRequestScope)
  {
    final String sDatasetType = aPathVariables.get ("datasetType");
    if (StringHelper.hasNoText (sDatasetType))
      throw new APIParamException ("Missing DatasetType");

    final String sDPType = aPathVariables.get ("dpType");
    if (StringHelper.hasNoText (sDPType))
      throw new APIParamException ("Missing DP Type");

    final IJsonObject aJson = new JsonObject ();
    CommonAPIInvoker.invoke (aJson, () -> {
      final IJsonArray aErrorMsgs = new JsonArray ();
      final ITCErrorHandler aErrorHdl = (eErrorLevel, sMsg, t, eCode) -> {
        if (eErrorLevel.isError ())
          aErrorMsgs.add (PhiveJsonHelper.getJsonError (eErrorLevel, eCode == null ? null : eCode.getID (), null, null, null, sMsg, t));
      };

      // Query DSD
      final ICommonsSet <DSDDatasetResponse> aResponses = TCAPIHelper.getDSDDatasetsByDPType (sDatasetType, sDPType, aErrorHdl);

      if (aErrorMsgs.isEmpty ())
      {
        aJson.add (JSON_SUCCESS, true);

        final JsonArray aList = new JsonArray ();
        for (final DSDDatasetResponse aResponse : aResponses)
          aList.add (aResponse.getAsJson ());
        aJson.addJson ("dataset-responses", aList);
      }
      else
      {
        aJson.add (JSON_SUCCESS, false);
        aJson.addJson ("errors", aErrorMsgs);
      }
    });

    return aJson;
  }
}
