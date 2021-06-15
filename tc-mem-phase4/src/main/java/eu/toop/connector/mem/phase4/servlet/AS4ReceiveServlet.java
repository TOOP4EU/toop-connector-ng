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
package eu.toop.connector.mem.phase4.servlet;

import javax.servlet.annotation.WebServlet;

import com.helger.commons.http.EHttpMethod;
import com.helger.phase4.attachment.IAS4IncomingAttachmentFactory;
import com.helger.phase4.model.pmode.resolve.DefaultPModeResolver;
import com.helger.phase4.model.pmode.resolve.IPModeResolver;
import com.helger.phase4.servlet.AS4XServletHandler;
import com.helger.xservlet.AbstractXServlet;

import eu.toop.connector.mem.phase4.Phase4Config;

/**
 * Local AS4 servlet. This endpoint must be used in the SMP for receiving
 * messages.
 *
 * @author Philip Helger
 */
@WebServlet ("/phase4")
public class AS4ReceiveServlet extends AbstractXServlet
{
  public AS4ReceiveServlet ()
  {
    // Multipart is handled specifically inside
    settings ().setMultipartEnabled (false);

    // The servlet handler takes all SPI implementations of
    // IAS4ServletMessageProcessorSPI and invokes them.
    // -> see AS4MessageProcessorSPI
    final IPModeResolver aPMR = DefaultPModeResolver.DEFAULT_PMODE_RESOLVER;
    final IAS4IncomingAttachmentFactory aIAF = IAS4IncomingAttachmentFactory.DEFAULT_INSTANCE;
    handlerRegistry ().registerHandler (EHttpMethod.POST, new AS4XServletHandler (Phase4Config::getCryptoFactory, aPMR, aIAF));
  }
}
