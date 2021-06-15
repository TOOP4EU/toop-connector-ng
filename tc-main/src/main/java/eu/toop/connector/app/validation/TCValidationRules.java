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
package eu.toop.connector.app.validation;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;

import com.helger.commons.ValueEnforcer;
import com.helger.commons.io.resource.IReadableResource;
import com.helger.jaxb.builder.IJAXBDocumentType;
import com.helger.phive.api.EValidationType;
import com.helger.phive.api.artefact.ValidationArtefact;
import com.helger.phive.api.executorset.VESID;
import com.helger.phive.api.executorset.ValidationExecutorSet;
import com.helger.phive.api.executorset.ValidationExecutorSetRegistry;
import com.helger.phive.engine.schematron.ValidationExecutorSchematron;
import com.helger.phive.engine.source.IValidationSourceXML;
import com.helger.phive.engine.xsd.ValidationExecutorXSD;
import com.helger.regrep.CRegRep4;
import com.helger.regrep.RegRep4Reader;
import com.helger.ubl23.UBL23NamespaceContext;

import eu.toop.edm.schematron.CEDMSchematron;
import eu.toop.edm.xml.cagv.CCAGV;
import eu.toop.edm.xml.cccev.CCCEV;

/**
 * Generic TOOP EDM 2.1.0 validation configuration
 *
 * @author Philip Helger
 */
@Immutable
public final class TCValidationRules
{
  public static final String GROUP_ID = "eu.toop";

  // The 2.1.0 release was using "2.0.0" by accident
  public static final VESID VID_TOOP_EDM_REQUEST_210 = new VESID (GROUP_ID, "edm-request", "2.1.0");
  public static final VESID VID_TOOP_EDM_RESPONSE_210 = new VESID (GROUP_ID, "edm-response", "2.1.0");
  public static final VESID VID_TOOP_EDM_ERROR_RESPONSE_210 = new VESID (GROUP_ID, "edm-error-response", "2.1.0");

  private TCValidationRules ()
  {}

  @Nonnull
  private static ClassLoader _getCL ()
  {
    return TCValidationRules.class.getClassLoader ();
  }

  @Nonnull
  private static ValidationExecutorSchematron _createXSLT (@Nonnull final IReadableResource aRes)
  {
    return new ValidationExecutorSchematron (new ValidationArtefact (EValidationType.SCHEMATRON_XSLT, aRes),
                                             null,
                                             UBL23NamespaceContext.getInstance ());
  }

  /**
   * Register all standard TOOP EDM v2 validation execution sets to the provided
   * registry.
   *
   * @param aRegistry
   *        The registry to add the artefacts. May not be <code>null</code>.
   */
  public static void initToopEDM (@Nonnull final ValidationExecutorSetRegistry <IValidationSourceXML> aRegistry)
  {
    ValueEnforcer.notNull (aRegistry, "Registry");

    final boolean bNotDeprecated = false;

    // Request
    {
      final IJAXBDocumentType aDT = RegRep4Reader.queryRequest (CCAGV.XSDS).getJAXBDocumentType ();
      aRegistry.registerValidationExecutorSet (ValidationExecutorSet.create (VID_TOOP_EDM_REQUEST_210,
                                                                             "TOOP EDM Request 2.1.0",
                                                                             bNotDeprecated,
                                                                             new ValidationExecutorXSD (new ValidationArtefact (EValidationType.XSD,
                                                                                                                                CRegRep4.getXSDResourceQuery ()),
                                                                                                        aDT::getSchema),
                                                                             _createXSLT (CEDMSchematron.TOOP_IS_REQUEST).setStopValidationOnError (true),
                                                                             _createXSLT (CEDMSchematron.TOOP_EDM2_XSLT),
                                                                             _createXSLT (CEDMSchematron.TOOP_BUSINESS_RULES_XSLT)));
    }

    // Response
    {
      final IJAXBDocumentType aDT = RegRep4Reader.queryResponse (CCCEV.XSDS).getJAXBDocumentType ();
      aRegistry.registerValidationExecutorSet (ValidationExecutorSet.create (VID_TOOP_EDM_RESPONSE_210,
                                                                             "TOOP EDM Response 2.1.0",
                                                                             bNotDeprecated,
                                                                             new ValidationExecutorXSD (new ValidationArtefact (EValidationType.XSD,
                                                                                                                                CRegRep4.getXSDResourceQuery ()),
                                                                                                        aDT::getSchema),
                                                                             _createXSLT (CEDMSchematron.TOOP_IS_RESPONSE).setStopValidationOnError (true),
                                                                             _createXSLT (CEDMSchematron.TOOP_EDM2_XSLT),
                                                                             _createXSLT (CEDMSchematron.TOOP_BUSINESS_RULES_XSLT)));
    }

    // Error Response
    {
      final IJAXBDocumentType aDT = RegRep4Reader.queryResponse (CCCEV.XSDS).getJAXBDocumentType ();
      aRegistry.registerValidationExecutorSet (ValidationExecutorSet.create (VID_TOOP_EDM_ERROR_RESPONSE_210,
                                                                             "TOOP EDM Error Response 2.1.0",
                                                                             bNotDeprecated,
                                                                             new ValidationExecutorXSD (new ValidationArtefact (EValidationType.XSD,
                                                                                                                                CRegRep4.getXSDResourceQuery ()),
                                                                                                        aDT::getSchema),
                                                                             _createXSLT (CEDMSchematron.TOOP_IS_ERROR_RESPONSE).setStopValidationOnError (true),
                                                                             _createXSLT (CEDMSchematron.TOOP_EDM2_XSLT),
                                                                             _createXSLT (CEDMSchematron.TOOP_BUSINESS_RULES_XSLT)));
    }
  }
}
