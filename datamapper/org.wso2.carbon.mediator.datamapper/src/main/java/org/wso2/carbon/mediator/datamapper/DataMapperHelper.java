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

import java.io.IOException;
import org.apache.avro.generic.GenericRecord;
import org.apache.axiom.om.OMElement;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.synapse.MessageContext;
import org.apache.synapse.SynapseException;
import org.json.JSONException;
import org.wso2.carbon.mediator.datamapper.datatypes.InputOutputDataTypes;
import org.wso2.carbon.mediator.datamapper.datatypes.OutputWriter;
import org.wso2.carbon.mediator.datamapper.datatypes.OutputWriterFactory;
import org.wso2.datamapper.engine.core.MappingHandler;
import org.wso2.datamapper.engine.core.MappingResourceLoader;
import org.wso2.datamapper.engine.inputAdapters.CsvInputReader;
import org.wso2.datamapper.engine.inputAdapters.InputDataReaderAdapter;
import org.wso2.datamapper.engine.inputAdapters.XmlInputReader;

/**
 * Using the input schema, output schema,mapping configuration, input and output
 * data types DataMapperHelper generates the required output
 */
public class DataMapperHelper {

	private final static  Log log = LogFactory.getLog(DataMapperHelper.class);

	/**
	 * Does message conversion and gives the output message as the final result
	 * 
	 * @param context
	 *            the message context
	 * @param configkey
	 *            registry location of the mapping configuration
	 * @param inSchemaKey
	 *            registry location of the input schema
	 * @param outSchemaKey
	 *            registry location of the output schema
	 * @param inputType
	 *            input data type
	 * @param outputType
	 *            output data type
	 * @param uuid
	 *            unique ID for the DataMapperMediator instance
	 * @throws SynapseException
	 * @throws IOException
	 */
	public static void transform(MessageContext context, String configkey,
			String inSchemaKey, String outSchemaKey, String inputType,
			String outputType, String uuid) throws SynapseException,
			IOException {

		MappingResourceLoader mappingResourceLoader = null;
		OMElement inputMessage, outputMessage = null;

		try {
			// Gets the mapping resources needed for the final output
			mappingResourceLoader = CacheResources.getCachedResources(context,
					configkey, inSchemaKey, outSchemaKey, uuid);
		}

		catch (Exception e) {
			handleException("Caching failed...", e);
		}

		inputMessage = context.getEnvelope();
		InputDataReaderAdapter inputReader = null;
		try {
			// FIXME include DatumReaders
			inputReader = convertInputMessage(inputType);
		} catch (Exception e) {
			handleException("Generating InputReaders failed...", e);

		}

		GenericRecord result = null;
		try {
			result = MappingHandler.doMap(inputMessage, mappingResourceLoader,
					inputReader);
		} catch (IllegalAccessException e) {
			handleException(
					"Mapping failed at generating the output result...", e);

		} catch (InstantiationException e) {
			handleException(
					"Mapping failed at generating the output result...", e);

		} catch (JSONException e) {
			handleException(
					"Mapping failed at generating the output result...", e);
		}

		// Gets the Output message based on output data type
		OutputWriter writer = OutputWriterFactory.getWriter(outputType);
		try {
			outputMessage = writer.getOutputMessage(outputType, result);
		} catch (Exception e) {
			handleException(
					"Generating output message from datum writers failed ....",
					e);
		}

		if (outputMessage != null) {
			if (log.isDebugEnabled()) {
				log.debug("Output message received ... ");
			}

			SOAPMessage.createSOAPMessage(outputMessage, context);
		}
	}

	/**
	 * Give the Input for the mapping
	 * 
	 * @param inputDataType
	 *            input data type
	 * @param inMessage
	 *            the incoming message
	 * @return the input as a OMElement
	 * @throws IOException
	 */
	private static InputDataReaderAdapter convertInputMessage(
			String inputDataType) throws IOException {
		InputDataReaderAdapter inputReader = null;

		if (inputDataType != null) {
			if (log.isDebugEnabled()) {
				log.debug("Input data type is ... " + inputDataType);
			}
			switch (InputOutputDataTypes.DataType.fromString(inputDataType)) {
			case CSV:
				inputReader = new CsvInputReader();
				break;
			case XML:
				inputReader = new XmlInputReader();
				break;
			default:
				// HandleJSONMessages.getOutputMessage(outputDataType, result);
			}
		} else {
			// FIXME with default dataType if user didn't mention input dataType
			inputReader = new XmlInputReader();
		}
		return inputReader;
	}

	private static void handleException(String message, Exception e) {
		log.error(message, e);
		throw new SynapseException(message, e);
	}

}
