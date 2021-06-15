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
package eu.toop.connector.app.smp;

import javax.annotation.Nonnull;

import com.helger.commons.ValueEnforcer;
import com.helger.commons.collection.impl.CommonsTreeMap;
import com.helger.commons.collection.impl.ICommonsSortedMap;
import com.helger.peppolid.CIdentifier;
import com.helger.peppolid.IParticipantIdentifier;
import com.helger.smpclient.bdxr1.IBDXRServiceGroupProvider;
import com.helger.smpclient.exception.SMPClientException;
import com.helger.smpclient.url.SMPDNSResolutionException;
import com.helger.xsds.bdxr.smp1.ServiceGroupType;
import com.helger.xsds.bdxr.smp1.ServiceMetadataReferenceType;

import eu.toop.connector.api.dd.IDDServiceGroupHrefProvider;
import eu.toop.connector.api.error.ITCErrorHandler;
import eu.toop.edm.error.EToopErrorCode;

public class DDServiceGroupHrefProviderSMP extends AbstractDDClient implements IDDServiceGroupHrefProvider
{
  public DDServiceGroupHrefProviderSMP ()
  {}

  @Nonnull
  public ICommonsSortedMap <String, String> getAllServiceGroupHrefs (@Nonnull final IParticipantIdentifier aParticipantID,
                                                                     @Nonnull final ITCErrorHandler aErrorHandler)
  {
    ValueEnforcer.notNull (aParticipantID, "ParticipantID");
    ValueEnforcer.notNull (aErrorHandler, "ErrorHandler");

    try
    {
      final ICommonsSortedMap <String, String> ret = new CommonsTreeMap <> ();
      final IBDXRServiceGroupProvider aClient = getServiceGroupProvider (aParticipantID);

      // Get all HRefs and sort them by decoded URL
      final ServiceGroupType aSG = aClient.getServiceGroupOrNull (aParticipantID);

      // Map from cleaned URL to original URL
      if (aSG != null && aSG.getServiceMetadataReferenceCollection () != null)
      {
        for (final ServiceMetadataReferenceType aSMR : aSG.getServiceMetadataReferenceCollection ().getServiceMetadataReference ())
        {
          // Decoded href is important for unification
          final String sHref = CIdentifier.createPercentDecoded (aSMR.getHref ());
          if (ret.put (sHref, aSMR.getHref ()) != null)
            aErrorHandler.onWarning ("The SMP ServiceGroup list contains the duplicate URL '" + sHref + "'", EToopErrorCode.GEN);
        }
      }
      return ret;
    }
    catch (final SMPDNSResolutionException | SMPClientException ex)
    {
      throw new IllegalStateException (ex);
    }
  }
}
