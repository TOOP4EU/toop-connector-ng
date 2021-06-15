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
package eu.toop.connector.api.dsd;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.helger.json.IJsonObject;
import com.helger.json.JsonObject;
import com.helger.peppolid.IDocumentTypeIdentifier;
import com.helger.peppolid.IParticipantIdentifier;

/**
 * Simplified DSD Response Representation
 *
 * @author jerouris at 21.05.2020
 */
public class DSDDatasetResponse
{
  private IParticipantIdentifier m_aDPIdentifier;

  private String m_sDatasetIdentifier;

  private String m_sDistributionFormat;
  private String m_sDistributionConforms;
  private String m_sDistributionMediaType;

  private String m_sAccessServiceConforms;

  private IDocumentTypeIdentifier m_aDocumentTypeIdentifier;

  public DSDDatasetResponse ()
  {}

  @Nullable
  public IParticipantIdentifier getDPIdentifier ()
  {
    return m_aDPIdentifier;
  }

  @Nonnull
  public DSDDatasetResponse setDPIdentifier (@Nullable final IParticipantIdentifier aDPIdentifier)
  {
    m_aDPIdentifier = aDPIdentifier;
    return this;
  }

  @Nullable
  public String getDatasetIdentifier ()
  {
    return m_sDatasetIdentifier;
  }

  @Nonnull
  public DSDDatasetResponse setDatasetIdentifier (@Nullable final String sDatasetIdentifier)
  {
    m_sDatasetIdentifier = sDatasetIdentifier;
    return this;
  }

  @Nullable
  public String getDistributionFormat ()
  {
    return m_sDistributionFormat;
  }

  @Nonnull
  public DSDDatasetResponse setDistributionFormat (@Nullable final String sDistributionFormat)
  {
    m_sDistributionFormat = sDistributionFormat;
    return this;
  }

  @Nullable
  public String getDistributionConforms ()
  {
    return m_sDistributionConforms;
  }

  @Nonnull
  public DSDDatasetResponse setDistributionConforms (@Nullable final String sDistributionConforms)
  {
    m_sDistributionConforms = sDistributionConforms;
    return this;
  }

  @Nullable
  public String getDistributionMediaType ()
  {
    return m_sDistributionMediaType;
  }

  @Nonnull
  public DSDDatasetResponse setDistributionMediaType (@Nullable final String sDistributionMediaType)
  {
    m_sDistributionMediaType = sDistributionMediaType;
    return this;
  }

  @Nullable
  public String getAccessServiceConforms ()
  {
    return m_sAccessServiceConforms;
  }

  @Nonnull
  public DSDDatasetResponse setAccessServiceConforms (@Nullable final String sAccessServiceConforms)
  {
    m_sAccessServiceConforms = sAccessServiceConforms;
    return this;
  }

  @Nullable
  public IDocumentTypeIdentifier getDocumentTypeIdentifier ()
  {
    return m_aDocumentTypeIdentifier;
  }

  @Nonnull
  public DSDDatasetResponse setDocumentTypeIdentifier (@Nullable final IDocumentTypeIdentifier aDocumentTypeIdentifier)
  {
    m_aDocumentTypeIdentifier = aDocumentTypeIdentifier;
    return this;
  }

  @Nonnull
  public IJsonObject getAsJson ()
  {
    final IJsonObject ret = new JsonObject ();
    if (m_aDPIdentifier != null)
      ret.addJson ("participant-id",
                   new JsonObject ().add ("scheme", m_aDPIdentifier.getScheme ()).add ("value", m_aDPIdentifier.getValue ()));

    ret.addIfNotNull ("dataset-id", m_sDatasetIdentifier);

    ret.addIfNotNull ("distribution-format", m_sDistributionFormat);
    ret.addIfNotNull ("distribution-conforms", m_sDistributionConforms);
    ret.addIfNotNull ("distribution-mediatype", m_sDistributionMediaType);

    ret.addIfNotNull ("accessservice-conforms", m_sAccessServiceConforms);

    if (m_aDocumentTypeIdentifier != null)
      ret.addJson ("doctype-id",
                   new JsonObject ().add ("scheme", m_aDocumentTypeIdentifier.getScheme ())
                                    .add ("value", m_aDocumentTypeIdentifier.getValue ()));
    return ret;
  }
}
