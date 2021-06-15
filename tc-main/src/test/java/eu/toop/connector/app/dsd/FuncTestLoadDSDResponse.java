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
package eu.toop.connector.app.dsd;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.helger.commons.io.resource.FileSystemResource;
import com.helger.xml.XMLHelper;
import com.helger.xml.serialize.read.DOMReader;

import eu.toop.edm.jaxb.dcatap.DCatAPDatasetType;
import eu.toop.edm.xml.dcatap.DatasetMarshaller;

public class FuncTestLoadDSDResponse
{
  @Test
  public void testRead ()
  {
    final Document aDoc = DOMReader.readXMLDOM (new FileSystemResource ("src/test/resources/dsd/dsd-response1.xml"));
    final Element aROL = XMLHelper.getFirstChildElementOfName (aDoc.getDocumentElement (), "RegistryObjectList");
    final Element aRO = XMLHelper.getFirstChildElementOfName (aROL, "RegistryObject");
    final Element aSlot = XMLHelper.getFirstChildElementOfName (aRO, "Slot");
    final Element aSlotValue = XMLHelper.getFirstChildElementOfName (aSlot, "SlotValue");
    assertNotNull (aSlotValue);
    final Element aDataset = XMLHelper.getFirstChildElement (aSlotValue);
    final DCatAPDatasetType aDS = new DatasetMarshaller ().read (aDataset);
    assertNotNull (aDS);
  }
}
