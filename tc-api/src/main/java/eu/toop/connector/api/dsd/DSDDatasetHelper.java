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
package eu.toop.connector.api.dsd;

import java.util.List;

import javax.annotation.Nonnull;

import com.helger.commons.annotation.ReturnsMutableCopy;
import com.helger.commons.collection.impl.CommonsHashSet;
import com.helger.commons.collection.impl.ICommonsList;
import com.helger.commons.collection.impl.ICommonsSet;
import com.helger.commons.string.StringHelper;
import com.helger.peppolid.factory.IIdentifierFactory;

import eu.toop.connector.api.TCConfig;
import eu.toop.edm.jaxb.cv.agent.PublicOrganizationType;
import eu.toop.edm.jaxb.cv.cbc.IDType;
import eu.toop.edm.jaxb.dcatap.DCatAPDatasetType;
import eu.toop.edm.jaxb.dcatap.DCatAPDistributionType;

/**
 * Contains helper functions for DSD service.
 *
 * @author yerlibilgin
 */
public final class DSDDatasetHelper
{
  private DSDDatasetHelper ()
  {}

  /**
   * Creates a set of {@link DSDDatasetResponse} objects from the given
   * <code>datasetTypesList</code>
   *
   * @param aDatasetTypeList
   *        the list of {@link DCatAPDatasetType} objects. May not be
   *        <code>null</code>.
   * @return the set of {@link DSDDatasetResponse} objects. Never
   *         <code>null</code>.
   */
  @Nonnull
  @ReturnsMutableCopy
  public static ICommonsSet <DSDDatasetResponse> buildDSDResponseSet (@Nonnull final List <DCatAPDatasetType> aDatasetTypeList)
  {
    final IIdentifierFactory aIF = TCConfig.getIdentifierFactory ();
    final ICommonsSet <DSDDatasetResponse> ret = new CommonsHashSet <> ();
    for (final DCatAPDatasetType aItem : aDatasetTypeList)
      for (final DCatAPDistributionType aDist : aItem.getDistribution ())
      {
        final DSDDatasetResponse resp = new DSDDatasetResponse ();
        // Access Service Conforms To
        if (aDist.getAccessService ().hasConformsToEntries ())
          resp.setAccessServiceConforms (aDist.getAccessService ().getConformsToAtIndex (0).getValue ());

        // DP Identifier
        final IDType aDPID = ((PublicOrganizationType) aItem.getPublisherAtIndex (0)).getIdAtIndex (0);
        resp.setDPIdentifier (aIF.createParticipantIdentifier (aDPID.getSchemeID (), aDPID.getValue ()));

        // Access Service Identifier, used as Document Type ID
        final ICommonsList <String> aDTParts = StringHelper.getExploded ("::", aDist.getAccessService ().getIdentifier (), 2);
        if (aDTParts.size () == 2)
          resp.setDocumentTypeIdentifier (aIF.createDocumentTypeIdentifier (aDTParts.get (0), aDTParts.get (1)));

        resp.setDatasetIdentifier (aItem.getIdentifierAtIndex (0));
        if (aDist.hasConformsToEntries ())
          resp.setDistributionConforms (aDist.getConformsToAtIndex (0).getValue ());

        if (aDist.getFormat ().hasContentEntries ())
          resp.setDistributionFormat (aDist.getFormat ().getContentAtIndex (0).toString ());
        ret.add (resp);
      }

    return ret;
  }
}
