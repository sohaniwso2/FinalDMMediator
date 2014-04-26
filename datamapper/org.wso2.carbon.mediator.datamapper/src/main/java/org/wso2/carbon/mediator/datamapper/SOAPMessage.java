/*
 *  Licensed to the Apache Software Foundation (ASF) under one
 *  or more contributor license agreements.  See the NOTICE file
 *  distributed with this work for additional information
 *  regarding copyright ownership.  The ASF licenses this file
 *  to you under the Apache License, Version 2.0 (the
 *  "License"); you may not use this file except in compliance
 *  with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *   * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 */

package org.wso2.carbon.mediator.datamapper;

import javax.xml.namespace.QName;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.soap.SOAP11Constants;
import org.apache.axiom.soap.SOAP12Constants;
import org.apache.axiom.soap.SOAPEnvelope;
import org.apache.axis2.AxisFault;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.synapse.util.AXIOMUtils;
import org.apache.synapse.MessageContext;
import org.apache.synapse.SynapseException;

/**
 * Creates the SOAP message
 * 
 */
public class SOAPMessage {

	private final static  String ENVELOPE = "Envelope";
	private final static  Log log = LogFactory.getLog(DataMapperHelper.class);

	/**
	 * Use to create the SOAP message
	 * 
	 * @param ouputMessage
	 *            the result as an OMElement
	 */
	public static void createSOAPMessage(OMElement outputMessage,
			MessageContext context) {

		if (outputMessage != null) {
			OMElement firstChild = outputMessage.getFirstElement();
			if (firstChild != null) {
				if (log.isDebugEnabled()) {
					log.debug("Contains a first child");
				}
				QName resultQName = firstChild.getQName();
				if (resultQName.getLocalPart().equals(ENVELOPE)
						&& (resultQName.getNamespaceURI().equals(
								SOAP11Constants.SOAP_ENVELOPE_NAMESPACE_URI) || resultQName
								.getNamespaceURI()
								.equals(SOAP12Constants.SOAP_ENVELOPE_NAMESPACE_URI))) {
					SOAPEnvelope soapEnvelope = AXIOMUtils
							.getSOAPEnvFromOM(outputMessage.getFirstElement());
					if (soapEnvelope != null) {
						try {
							if (log.isDebugEnabled()) {
								log.debug("Valid Envelope");
							}
							context.setEnvelope(soapEnvelope);
						} catch (AxisFault axisFault) {
							handleException("Invalid Envelope", axisFault);
						}
					}
				} else {
					context.getEnvelope().getBody().getFirstElement().detach();
					context.getEnvelope().getBody().addChild(outputMessage);

				}
			} else {
				context.getEnvelope().getBody().getFirstElement().detach();
				context.getEnvelope().getBody().addChild(outputMessage);
			}
		}
	}

	private static void handleException(String message, Exception e) {
		log.error(message, e);
		throw new SynapseException(message, e);
	}

}
