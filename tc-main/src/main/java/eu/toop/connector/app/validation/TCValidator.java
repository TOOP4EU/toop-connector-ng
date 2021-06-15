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
package eu.toop.connector.app.validation;

import java.util.Locale;

import javax.annotation.Nonnull;

import org.w3c.dom.Document;

import com.helger.commons.error.list.ErrorList;
import com.helger.commons.io.resource.inmemory.ReadableResourceByteArray;
import com.helger.phive.api.EValidationType;
import com.helger.phive.api.artefact.ValidationArtefact;
import com.helger.phive.api.execute.ValidationExecutionManager;
import com.helger.phive.api.executorset.IValidationExecutorSet;
import com.helger.phive.api.executorset.VESID;
import com.helger.phive.api.executorset.ValidationExecutorSetRegistry;
import com.helger.phive.api.result.ValidationResult;
import com.helger.phive.api.result.ValidationResultList;
import com.helger.phive.engine.source.IValidationSourceXML;
import com.helger.phive.engine.source.ValidationSourceXML;
import com.helger.xml.EXMLParserFeature;
import com.helger.xml.sax.WrappedCollectingSAXErrorHandler;
import com.helger.xml.serialize.read.DOMReader;
import com.helger.xml.serialize.read.DOMReaderSettings;

import eu.toop.connector.api.validation.IVSValidator;

/**
 * The default implementation of {@link IVSValidator}
 *
 * @author Philip Helger
 */
public class TCValidator implements IVSValidator
{
  private static final ValidationExecutorSetRegistry <IValidationSourceXML> VER = new ValidationExecutorSetRegistry <> ();
  static
  {
    // Init all TOOP rules
    TCValidationRules.initToopEDM (VER);
  }

  @Nonnull
  public static ValidationExecutorSetRegistry <IValidationSourceXML> internalGetRegistry ()
  {
    return VER;
  }

  @Nonnull
  public static IValidationExecutorSet <IValidationSourceXML> getVES (@Nonnull final VESID aVESID)
  {
    final IValidationExecutorSet <IValidationSourceXML> aVES = VER.getOfID (aVESID);
    if (aVES == null)
      throw new IllegalStateException ("Unexpected VESID '" + aVESID.getAsSingleID () + "'");
    return aVES;
  }

  public TCValidator ()
  {}

  @Nonnull
  public ValidationResultList validate (@Nonnull final VESID aVESID, @Nonnull final byte [] aPayload, @Nonnull final Locale aDisplayLocale)
  {
    final ErrorList aXMLErrors = new ErrorList ();
    final ValidationResultList aValidationResultList = new ValidationResultList ();

    final ReadableResourceByteArray aXMLRes = new ReadableResourceByteArray (aPayload);
    final Document aDoc = DOMReader.readXMLDOM (aXMLRes,
                                                new DOMReaderSettings ().setErrorHandler (new WrappedCollectingSAXErrorHandler (aXMLErrors))
                                                                        .setLocale (aDisplayLocale)
                                                                        .setFeatureValues (EXMLParserFeature.AVOID_XML_ATTACKS));
    if (aDoc != null)
    {
      // What to validate?
      final IValidationSourceXML aValidationSource = ValidationSourceXML.create ("uploaded content", DOMReader.readXMLDOM (aPayload));
      // Start validation
      ValidationExecutionManager.executeValidation (getVES (aVESID), aValidationSource, aValidationResultList, aDisplayLocale);
    }

    // Add all XML parsing stuff - always first item
    // Also add if no error is present to have it shown in the list
    aValidationResultList.add (0, new ValidationResult (new ValidationArtefact (EValidationType.XML, aXMLRes), aXMLErrors));
    return aValidationResultList;
  }
}
