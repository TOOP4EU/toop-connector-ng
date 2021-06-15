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
package eu.toop.connector.api.rest;

import javax.annotation.Nonnull;

import com.helger.commons.annotation.Singleton;
import com.helger.xml.namespace.MapBasedNamespaceContext;

/**
 * The namespace context to be used as the namespace prefix mapper.
 *
 * @author Philip Helger
 */
@Singleton
public class TCRestNamespaceContext extends MapBasedNamespaceContext
{
  private static final class SingletonHolder
  {
    static final TCRestNamespaceContext s_aInstance = new TCRestNamespaceContext ();
  }

  protected TCRestNamespaceContext ()
  {
    addMapping (TCRestJAXB.DEFAULT_NAMESPACE_PREFIX, TCRestJAXB.NS_URI);
  }

  @Nonnull
  public static TCRestNamespaceContext getInstance ()
  {
    return SingletonHolder.s_aInstance;
  }
}
