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
import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;

import com.helger.commons.ValueEnforcer;
import com.helger.commons.annotation.ReturnsMutableCopy;
import com.helger.commons.collection.impl.CommonsArrayList;
import com.helger.commons.collection.impl.ICommonsList;
import com.helger.commons.io.resource.ClassPathResource;
import com.helger.jaxb.GenericJAXBMarshaller;
import com.helger.peppolid.IIdentifier;

/**
 * JAXB helper for TC NG REST classes.
 *
 * @author Philip Helger
 */
@Immutable
public final class TCRestJAXB
{
  public static final ClassPathResource XSD_RES = new ClassPathResource ("/schemas/tc-rest.xsd", TCRestJAXB.class.getClassLoader ());
  public static final String NS_URI = "urn:eu.toop/toop-connector-ng/2020/05/";
  public static final String DEFAULT_NAMESPACE_PREFIX = "tc";

  private TCRestJAXB ()
  {}

  @Nonnull
  @ReturnsMutableCopy
  public static ICommonsList <ClassPathResource> getAllXSDResources ()
  {
    final ICommonsList <ClassPathResource> ret = new CommonsArrayList <> ();
    ret.add (XSD_RES);
    return ret;
  }

  /**
   * @return A new marshaller to read and write {@link TCOutgoingMessage}
   *         objects. Never <code>null</code>.
   */
  @Nonnull
  public static GenericJAXBMarshaller <TCOutgoingMessage> outgoingMessage ()
  {
    final GenericJAXBMarshaller <TCOutgoingMessage> ret = new GenericJAXBMarshaller <> (TCOutgoingMessage.class,
                                                                                        getAllXSDResources (),
                                                                                        new ObjectFactory ()::createOutgoingMessage);
    ret.setFormattedOutput (true);
    ret.setNamespaceContext (TCRestNamespaceContext.getInstance ());
    return ret;
  }

  /**
   * @return A new marshaller to read and write {@link TCIncomingMessage}
   *         objects. Never <code>null</code>.
   */
  @Nonnull
  public static GenericJAXBMarshaller <TCIncomingMessage> incomingMessage ()
  {
    final GenericJAXBMarshaller <TCIncomingMessage> ret = new GenericJAXBMarshaller <> (TCIncomingMessage.class,
                                                                                        getAllXSDResources (),
                                                                                        new ObjectFactory ()::createIncomingMessage);
    ret.setFormattedOutput (true);
    ret.setNamespaceContext (TCRestNamespaceContext.getInstance ());
    return ret;
  }

  /**
   * @param aID
   *        The source identifier. May not be <code>null</code>.
   * @return The created {@link TCIdentifierType} and never <code>null</code>.
   */
  @Nonnull
  public static TCIdentifierType createTCID (@Nonnull final IIdentifier aID)
  {
    ValueEnforcer.notNull (aID, "ID");
    return createTCID (aID.getScheme (), aID.getValue ());
  }

  /**
   * Create a new {@link TCIdentifierType}
   *
   * @param sScheme
   *        The scheme to use. May be <code>null</code>.
   * @param sValue
   *        The value to use. May not be <code>null</code>.
   * @return The created {@link TCIdentifierType} and never <code>null</code>.
   */
  @Nonnull
  public static TCIdentifierType createTCID (@Nullable final String sScheme, @Nonnull final String sValue)
  {
    ValueEnforcer.notNull (sValue, "Value");
    final TCIdentifierType ret = new TCIdentifierType ();
    ret.setScheme (sScheme);
    ret.setValue (sValue);
    return ret;
  }
}
