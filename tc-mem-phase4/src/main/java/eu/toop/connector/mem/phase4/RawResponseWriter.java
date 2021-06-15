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
package eu.toop.connector.mem.phase4;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.helger.commons.io.file.FilenameHelper;
import com.helger.commons.io.file.SimpleFileIO;
import com.helger.commons.string.StringHelper;
import com.helger.datetime.util.PDTIOHelper;
import com.helger.phase4.client.AS4ClientSentMessage;
import com.helger.phase4.client.IAS4RawResponseConsumer;
import com.helger.phase4.util.Phase4Exception;

public class RawResponseWriter implements IAS4RawResponseConsumer
{
  private static final Logger LOGGER = LoggerFactory.getLogger (RawResponseWriter.class);

  public void handleResponse (final AS4ClientSentMessage <byte []> aResponseEntity) throws Phase4Exception
  {
    final String sFolderName = Phase4Config.getSendResponseFolderName ();
    if (StringHelper.hasText (sFolderName))
    {
      final String sMessageID = aResponseEntity.getMessageID ();
      final String sFilename = PDTIOHelper.getCurrentLocalDateTimeForFilename () +
                               "-" +
                               FilenameHelper.getAsSecureValidASCIIFilename (sMessageID) +
                               "-response.xml";
      final File aResponseFile = new File (sFolderName, sFilename);
      if (SimpleFileIO.writeFile (aResponseFile, aResponseEntity.getResponse ()).isSuccess ())
        LOGGER.info ("[phase4] Response file was written to '" + aResponseFile.getAbsolutePath () + "'");
      else
        LOGGER.error ("[phase4] Error writing response file to '" + aResponseFile.getAbsolutePath () + "'");
    }
  }
}
