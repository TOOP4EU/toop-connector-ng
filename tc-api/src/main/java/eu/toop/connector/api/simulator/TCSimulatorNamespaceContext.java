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
package eu.toop.connector.api.simulator;

import javax.annotation.Nonnull;

import com.helger.commons.annotation.Singleton;
import com.helger.xml.namespace.MapBasedNamespaceContext;
import com.helger.xsds.bdxr.smp1.CBDXRSMP1;
import com.helger.xsds.xmldsig.CXMLDSig;

/**
 * The namespace context to be used as the namespace prefix mapper.
 *
 * @author Philip Helger
 */
@Singleton
public class TCSimulatorNamespaceContext extends MapBasedNamespaceContext
{
  private static final class SingletonHolder
  {
    static final TCSimulatorNamespaceContext s_aInstance = new TCSimulatorNamespaceContext ();
  }

  protected TCSimulatorNamespaceContext ()
  {
    addMapping (CXMLDSig.DEFAULT_PREFIX, CXMLDSig.NAMESPACE_URI);
    addMapping (CBDXRSMP1.DEFAULT_PREFIX, CBDXRSMP1.NAMESPACE_URI);
    addDefaultNamespaceURI (TCSimulatorJAXB.NS_URI);
  }

  @Nonnull
  public static TCSimulatorNamespaceContext getInstance ()
  {
    return SingletonHolder.s_aInstance;
  }
}
