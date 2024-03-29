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
package eu.toop.connector.api.me.model;

import java.io.Serializable;
import java.nio.charset.Charset;
import java.util.UUID;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;

import com.helger.commons.ValueEnforcer;
import com.helger.commons.annotation.MustImplementEqualsAndHashcode;
import com.helger.commons.annotation.Nonempty;
import com.helger.commons.annotation.ReturnsMutableObject;
import com.helger.commons.hashcode.HashCodeGenerator;
import com.helger.commons.io.ByteArrayWrapper;
import com.helger.commons.mime.IMimeType;
import com.helger.commons.string.StringHelper;
import com.helger.commons.string.ToStringGenerator;
import com.helger.regrep.CRegRep4;

/**
 * A single payload of an AS4 message. Used inside {@link MEMessage}
 *
 * @author myildiz at 15.02.2018.
 * @author Philip Helger
 */
@Immutable
@MustImplementEqualsAndHashcode
public final class MEPayload implements Serializable
{
  /**
   * Type of the payload
   */
  private final IMimeType m_aMimeType;

  /**
   * Content-ID for the payload
   */
  private final String m_sContentID;

  /**
   * The actual payload content
   */
  private final ByteArrayWrapper m_aData;

  protected MEPayload (@Nonnull final IMimeType aMimeType,
                       @Nonnull @Nonempty final String sContentID,
                       @Nonnull final ByteArrayWrapper aData)
  {
    ValueEnforcer.notNull (aMimeType, "MimeType");
    ValueEnforcer.notEmpty (sContentID, "ContentID");
    ValueEnforcer.notNull (aData, "Data");

    m_aMimeType = aMimeType;
    m_sContentID = sContentID;
    m_aData = aData;
  }

  @Nonnull
  public IMimeType getMimeType ()
  {
    return m_aMimeType;
  }

  @Nonnull
  public String getMimeTypeString ()
  {
    return m_aMimeType.getAsString ();
  }

  @Nonnull
  public String getContentID ()
  {
    return m_sContentID;
  }

  @Nonnull
  @ReturnsMutableObject
  public ByteArrayWrapper getData ()
  {
    return m_aData;
  }

  @Override
  public boolean equals (final Object o)
  {
    if (o == this)
      return true;
    if (o == null || !getClass ().equals (o.getClass ()))
      return false;

    final MEPayload rhs = (MEPayload) o;
    return m_aMimeType.equals (rhs.m_aMimeType) && m_sContentID.equals (rhs.m_sContentID) && m_aData.equals (rhs.m_aData);
  }

  @Override
  public int hashCode ()
  {
    return new HashCodeGenerator (this).append (m_aMimeType).append (m_sContentID).append (m_aData).getHashCode ();
  }

  @Override
  public String toString ()
  {
    return new ToStringGenerator (this).append ("MimeType", m_aMimeType)
                                       .append ("ContentID", m_sContentID)
                                       .append ("Data", m_aData)
                                       .getToString ();
  }

  @Nonnull
  public static Builder builder ()
  {
    return new Builder ();
  }

  @Nonnull
  @Nonempty
  public static String createRandomContentID ()
  {
    // Must use RFC 2822 style
    return UUID.randomUUID ().toString () + "@tc-ng.toop";
  }

  /**
   * Builder class for {@link MEPayload}
   *
   * @author Philip Helger
   */
  public static class Builder
  {
    private IMimeType m_aMimeType;
    private String m_sContentID;
    private ByteArrayWrapper m_aData;

    protected Builder ()
    {}

    @Nonnull
    public Builder mimeTypeRegRep ()
    {
      return mimeType (CRegRep4.MIME_TYPE_EBRS_XML);
    }

    @Nonnull
    public Builder mimeType (@Nullable final IMimeType a)
    {
      m_aMimeType = a;
      return this;
    }

    @Nonnull
    public Builder randomContentID ()
    {
      return contentID (createRandomContentID ());
    }

    @Nonnull
    public Builder contentID (@Nullable final String s)
    {
      m_sContentID = s;
      return this;
    }

    @Nonnull
    public Builder data (@Nullable final byte [] a)
    {
      // Assume: no copy
      return data (a == null ? null : new ByteArrayWrapper (a, false));
    }

    @Nonnull
    public Builder data (@Nullable final String s, @Nonnull final Charset aCharset)
    {
      return data (s == null ? null : ByteArrayWrapper.create (s, aCharset));
    }

    @Nonnull
    public Builder data (@Nullable final ByteArrayWrapper a)
    {
      m_aData = a;
      return this;
    }

    public void checkConsistency ()
    {
      if (m_aMimeType == null)
        throw new IllegalStateException ("MimeType MUST be present");
      if (StringHelper.hasNoText (m_sContentID))
        throw new IllegalStateException ("Content ID MUST be present");
      if (m_aData == null)
        throw new IllegalStateException ("Data MUST be present");
    }

    @Nonnull
    public MEPayload build ()
    {
      checkConsistency ();
      return new MEPayload (m_aMimeType, m_sContentID, m_aData);
    }
  }
}
