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
package eu.toop.connector.api.me;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.GuardedBy;

import com.helger.commons.collection.impl.CommonsLinkedHashMap;
import com.helger.commons.collection.impl.ICommonsMap;
import com.helger.commons.concurrent.SimpleReadWriteLock;
import com.helger.commons.exception.InitializationException;
import com.helger.commons.lang.ServiceLoaderHelper;

import eu.toop.connector.api.TCConfig;

public class MessageExchangeManager
{
  private static final SimpleReadWriteLock s_aRWLock = new SimpleReadWriteLock ();
  @GuardedBy ("s_aRWLock")
  private static ICommonsMap <String, IMessageExchangeSPI> s_aMap = new CommonsLinkedHashMap <> ();

  public static void reinitialize ()
  {
    s_aRWLock.writeLocked ( () -> {
      s_aMap.clear ();
      for (final IMessageExchangeSPI aImpl : ServiceLoaderHelper.getAllSPIImplementations (IMessageExchangeSPI.class))
      {
        final String sID = aImpl.getID ();
        if (s_aMap.containsKey (sID))
          throw new InitializationException ("The IMessageExchangeSPI ID '" +
                                             sID +
                                             "' is already in use - please provide a different one!");
        s_aMap.put (sID, aImpl);
      }
      if (s_aMap.isEmpty ())
        throw new InitializationException ("No IMessageExchangeSPI implementation is registered!");
    });
  }

  static
  {
    // Initial init
    reinitialize ();
  }

  private MessageExchangeManager ()
  {}

  @Nullable
  public static IMessageExchangeSPI getImplementationOfID (@Nullable final String sID)
  {
    // Fallback to default
    return s_aRWLock.readLockedGet ( () -> s_aMap.get (sID));
  }

  @Nonnull
  public static IMessageExchangeSPI getConfiguredImplementation ()
  {
    final String sID = TCConfig.MEM.getMEMImplementationID ();
    final IMessageExchangeSPI ret = getImplementationOfID (sID);
    if (ret == null)
      throw new IllegalStateException ("Failed to resolve MEM implementation with ID '" + sID + "'");
    return ret;
  }

  @Nonnegative
  public static ICommonsMap <String, IMessageExchangeSPI> getAll ()
  {
    return s_aRWLock.readLockedGet ( () -> s_aMap.getClone ());
  }
}
