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
package org.wso2.carbon.mediator.datamapper.datatypes;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.xml.stream.XMLStreamException;

import org.apache.avro.generic.GenericRecord;
import org.apache.avro.io.DatumWriter;
import org.apache.avro.io.Encoder;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.util.AXIOMUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.synapse.SynapseException;
import org.wso2.datamapper.engine.core.writer.DummyEncoder;
import org.wso2.datamapper.engine.core.writer.WriterRegistry;

/**
 * 
 * Generates the relevant output message when the data type is XML
 * 
 */

public class XMLWriter implements OutputWriter {

	private final static Log log = LogFactory.getLog(XMLWriter.class);

	/**
	 * Gives the output message
	 * 
	 * @param outputType
	 *            output data type
	 * @param result
	 *            mapping result
	 * @return the output as an OMElement
	 * @throws IOException
	 */

	public OMElement getOutputMessage(String outputType, GenericRecord result)
			throws SynapseException, IOException {

		DatumWriter<GenericRecord> writer = null;
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		Encoder encoder = new DummyEncoder(byteArrayOutputStream);
		OMElement outMessage = null;
		try {

			writer = WriterRegistry.getInstance().get(outputType).newInstance();
			writer.setSchema(result.getSchema());
			writer.write(result, encoder);

			if (log.isDebugEnabled()) {
				log.debug("Output received from datum writer.."
						+ byteArrayOutputStream.toString());
			}
			// Converts the result into the desired outputType
			outMessage = getOutputResult(byteArrayOutputStream.toString());

		} catch (Exception e) {
			handleException("Data coversion Failed at XMLWriter..", e);
		} finally {
			encoder.flush();
		}

		try {
			// Converts the result into an OMElement
			outMessage = getOutputResult(byteArrayOutputStream.toString());
		} catch (XMLStreamException e) {
			handleException(
					"Failed at generating the OMElement for the XML output received...",
					e);
		}
		return outMessage;
	}

	/**
	 * Gives the final output as an OMElement
	 * 
	 * @param result
	 *            mapping result
	 * @return output message as an OMElement
	 * @throws XMLStreamException
	 */

	private static OMElement getOutputResult(String result)
			throws XMLStreamException {
		return AXIOMUtil.stringToOM(result);

	}

	private static void handleException(String message, Exception e) {
		log.error(message, e);
		throw new SynapseException(message, e);
	}

}
