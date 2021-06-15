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
package eu.toop.connector.api;

import java.nio.charset.StandardCharsets;
import java.util.function.Function;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.helger.commons.annotation.Nonempty;
import com.helger.commons.io.resource.IReadableResource;
import com.helger.commons.string.StringHelper;
import com.helger.config.source.res.AbstractConfigurationSourceResource;
import com.helger.config.source.res.ConfigurationSourceJson;
import com.helger.config.source.res.ConfigurationSourceProperties;

/**
 * Defines the type of configuration source resources.
 *
 * @author Philip Helger
 */
enum EConfigSourceResourceType
{
  JSON ("json", x -> new ConfigurationSourceJson (x, StandardCharsets.UTF_8)),
  PROPERTIES ("properties", x -> new ConfigurationSourceProperties (x, StandardCharsets.UTF_8));

  private final String m_sExt;
  private final Function <IReadableResource, AbstractConfigurationSourceResource> m_aFactory;

  EConfigSourceResourceType (@Nonnull @Nonempty final String sExt,
                             @Nonnull final Function <IReadableResource, AbstractConfigurationSourceResource> aFactory)
  {
    m_sExt = sExt;
    m_aFactory = aFactory;
  }

  @Nonnull
  @Nonempty
  public String getExtension ()
  {
    return m_sExt;
  }

  @Nonnull
  @Nonempty
  public AbstractConfigurationSourceResource createConfigurationSource (@Nonnull final IReadableResource aRes)
  {
    return m_aFactory.apply (aRes);
  }

  @Nullable
  public static EConfigSourceResourceType getFromExtensionOrNull (@Nullable final String sExt)
  {
    return getFromExtensionOrDefault (sExt, null);
  }

  @Nullable
  public static EConfigSourceResourceType getFromExtensionOrDefault (@Nullable final String sExt,
                                                                     @Nullable final EConfigSourceResourceType eDefault)
  {
    if (StringHelper.hasText (sExt))
      for (final EConfigSourceResourceType e : values ())
        if (e.m_sExt.equalsIgnoreCase (sExt))
          return e;
    return eDefault;
  }
}
