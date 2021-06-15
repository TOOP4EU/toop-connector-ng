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
