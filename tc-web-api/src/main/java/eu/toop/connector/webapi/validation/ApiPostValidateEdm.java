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
package eu.toop.connector.webapi.validation;

import java.io.IOException;
import java.util.Map;

import javax.annotation.Nonnull;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.helger.commons.annotation.Nonempty;
import com.helger.commons.io.stream.StreamHelper;
import com.helger.commons.timing.StopWatch;
import com.helger.json.IJsonObject;
import com.helger.json.JsonObject;
import com.helger.phive.api.executorset.VESID;
import com.helger.phive.api.result.ValidationResultList;
import com.helger.phive.json.PhiveJsonHelper;
import com.helger.photon.api.IAPIDescriptor;
import com.helger.web.scope.IRequestWebScopeWithoutResponse;

import eu.toop.connector.app.api.TCAPIHelper;
import eu.toop.connector.app.validation.TCValidator;
import eu.toop.connector.webapi.ETCEdmType;
import eu.toop.connector.webapi.helper.AbstractTCAPIInvoker;
import eu.toop.connector.webapi.helper.CommonAPIInvoker;

/**
 * Perform validation via API
 *
 * @author Philip Helger
 */
public class ApiPostValidateEdm extends AbstractTCAPIInvoker
{
  private static final Logger LOGGER = LoggerFactory.getLogger (ApiPostValidateEdm.class);

  private final ETCEdmType m_eType;

  public ApiPostValidateEdm (@Nonnull final ETCEdmType eType)
  {
    m_eType = eType;
  }

  @Override
  public IJsonObject invokeAPI (@Nonnull final IAPIDescriptor aAPIDescriptor,
                                @Nonnull @Nonempty final String sPath,
                                @Nonnull final Map <String, String> aPathVariables,
                                @Nonnull final IRequestWebScopeWithoutResponse aRequestScope) throws IOException
  {
    final byte [] aPayload = StreamHelper.getAllBytes (aRequestScope.getRequest ().getInputStream ());
    final VESID aVESID = m_eType.getVESID ();

    if (LOGGER.isInfoEnabled ())
      LOGGER.info ("API validating " + aPayload.length + " bytes using '" + aVESID.getAsSingleID () + "'");

    final IJsonObject aJson = new JsonObject ();
    CommonAPIInvoker.invoke (aJson, () -> {
      // Main validation
      final StopWatch aSW = StopWatch.createdStarted ();
      final ValidationResultList aValidationResultList = TCAPIHelper.validateBusinessDocument (aVESID, aPayload);
      aSW.stop ();

      // Build response
      aJson.add (JSON_SUCCESS, true);
      PhiveJsonHelper.applyValidationResultList (aJson,
                                                 TCValidator.getVES (aVESID),
                                                 aValidationResultList,
                                                 TCAPIHelper.DEFAULT_LOCALE,
                                                 aSW.getMillis (),
                                                 null,
                                                 null);
    });

    return aJson;
  }
}
