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
package eu.toop.connector.api.http;

import java.security.GeneralSecurityException;

import org.apache.http.HttpHost;

import com.helger.commons.exception.InitializationException;
import com.helger.httpclient.HttpClientSettings;

import eu.toop.connector.api.TCConfig;

/**
 * Common TOOP Connector HTTPClient settings
 *
 * @author Philip Helger
 */
public class TCHttpClientSettings extends HttpClientSettings
{
  public TCHttpClientSettings ()
  {
    // Add settings from configuration file here centrally
    if (TCConfig.HTTP.isProxyServerEnabled ())
    {
      setProxyHost (new HttpHost (TCConfig.HTTP.getProxyServerAddress (), TCConfig.HTTP.getProxyServerPort ()));

      // Non-proxy hosts
      addNonProxyHostsFromPipeString (TCConfig.HTTP.getProxyServerNonProxyHosts ());
    }

    // Disable SSL checks?
    if (TCConfig.HTTP.isTLSTrustAll ())
      try
      {
        setSSLContextTrustAll ();
        setHostnameVerifierVerifyAll ();
      }
      catch (final GeneralSecurityException ex)
      {
        throw new InitializationException (ex);
      }

    final int nConnectionTimeoutMS = TCConfig.HTTP.getConnectionTimeoutMS ();
    if (nConnectionTimeoutMS >= 0)
      setConnectionTimeoutMS (nConnectionTimeoutMS);

    final int nReadTimeoutMS = TCConfig.HTTP.getReadTimeoutMS ();
    if (nReadTimeoutMS >= 0)
      setSocketTimeoutMS (nReadTimeoutMS);
  }
}
