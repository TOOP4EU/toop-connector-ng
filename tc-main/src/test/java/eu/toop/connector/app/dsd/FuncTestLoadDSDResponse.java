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
