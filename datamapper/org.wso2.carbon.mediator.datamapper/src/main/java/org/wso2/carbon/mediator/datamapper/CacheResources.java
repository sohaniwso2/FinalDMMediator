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

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import org.apache.axiom.om.impl.llom.OMTextImpl;
import org.apache.axis2.context.ConfigurationContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.synapse.MessageContext;
import org.apache.synapse.SynapseException;
import org.apache.synapse.core.axis2.Axis2MessageContext;
import org.wso2.datamapper.engine.core.MappingResourceLoader;

/**
 * Handles caching of the mapping resources
 * 
 */
public class CacheResources {
	private final static  String CACHABLE_DURATION = "cachableDuration";
	private final static  String DATAMAPPER_CACHE_MAP_KEY = "dataMapperCacheMap";
	private static MappingResourceLoader mappingResourceLoader = null;
	private static int time = 10000;
	private final static  Log log = LogFactory.getLog(CacheResources.class);

	/**
	 * Use to get the cached mapping resources
	 * 
	 * @param context
	 *            the message context
	 * @param configkey
	 *            the location of the mapping configuration
	 * @param inSchemaKey
	 *            the location of the input schema
	 * @param outSchemaKey
	 *            the location of the output schema
	 * @param uuid
	 *            the unique ID
	 * @return the mapping resource loader containing the mapping resources
	 * @throws IOException
	 */
	public static MappingResourceLoader getCachedResources(
			MessageContext context, String configkey, String inSchemaKey,
			String outSchemaKey, String datamapperMediatorUuid)
			throws SynapseException {

		DataMapperCacheContext dmcc = null;

		// Gets the axis2 message context
		org.apache.axis2.context.MessageContext axis2MsgCtx = ((Axis2MessageContext) context)
				.getAxis2MessageContext();

		ConfigurationContext configurationContext = axis2MsgCtx
				.getConfigurationContext();

		// Gets the registry cacheble duration
		String cacheDurable = context.getConfiguration().getRegistry()
				.getConfigurationProperties().getProperty(CACHABLE_DURATION);
		long cacheTime = (cacheDurable != null && !cacheDurable.isEmpty()) ? Long
				.parseLong(cacheDurable) : time;

		// When proxy invokes initially this creates a property in Axis2 and then creates the cacheble object
		if (configurationContext.getProperty(DATAMAPPER_CACHE_MAP_KEY) == null) {
			if (log.isDebugEnabled()) {
				log.debug("Creates a new property in Axis2 for data mapper cache map");
			}
			try {
				// Creates a new mapping resource loader
				mappingResourceLoader = getMappingResourceLoader(context,
						configkey, inSchemaKey, outSchemaKey);
			} catch (Exception e) {
				handleException(
						"Creating a new MappingResourceLoader failed...", e);
			}
			// Creates a cacheble object
			dmcc = new DataMapperCacheContext(Calendar.getInstance().getTime(),
					mappingResourceLoader);
			Map<String, DataMapperCacheContext> mappingResourceMap = new HashMap<String, DataMapperCacheContext>();
			mappingResourceMap.put(datamapperMediatorUuid, dmcc);
			// Creates a property in Axis2 containing the cacheble object
			configurationContext.setProperty(DATAMAPPER_CACHE_MAP_KEY,
					mappingResourceMap);
		} else {
			// Checks the property in Axis2 and get the map from Axis2
			@SuppressWarnings("unchecked")
			Map<String, DataMapperCacheContext> mappingResourceMapFromAxis = (Map<String, DataMapperCacheContext>) configurationContext
					.getProperty(DATAMAPPER_CACHE_MAP_KEY);
			if (mappingResourceMapFromAxis.containsKey(datamapperMediatorUuid)) {
				if (log.isDebugEnabled()) {
					log.debug("Contains a property in Axis2 for the DataMapperCacheMap with the key"
							+ datamapperMediatorUuid);
				}
				// Gets the cacheble object
				dmcc = mappingResourceMapFromAxis.get(datamapperMediatorUuid);
				// Gets the cacheble limit
				long cachebleLimit = dmcc.getDateTime().getTime() + cacheTime;
				// Checks for the cacheble limit against the current time
				if (cachebleLimit >= System.currentTimeMillis()) {
					mappingResourceLoader = dmcc.getCachedResources();
				} else {
					if (log.isDebugEnabled()) {
						log.debug("exceeds the cachebleLimit " + cachebleLimit);
					}
					// clear the map and insert the new data mapper cache
					// context
					mappingResourceMapFromAxis.clear();

					try {
						// Creates a new mapping resource loader
						mappingResourceLoader = getMappingResourceLoader(
								context, configkey, inSchemaKey, outSchemaKey);
					} catch (Exception e) {
						handleException(
								"Creating a new MappingResourceLoader failed...",
								e);
					}
					dmcc = new DataMapperCacheContext(Calendar.getInstance()
							.getTime(), mappingResourceLoader);
					mappingResourceMapFromAxis
							.put(datamapperMediatorUuid, dmcc);
				}
			} else {
				if (log.isDebugEnabled()) {
					log.debug("Doesn't contain the key"
							+ datamapperMediatorUuid + " in the map from Axis");
				}
				try {
					// Creates a cacheble object and include in the map from
					// axis
					mappingResourceLoader = getMappingResourceLoader(context,
							configkey, inSchemaKey, outSchemaKey);
				} catch (Exception e) {
					handleException(
							"Creating a MappingResourceLoader failed...", e);
				}
				dmcc = new DataMapperCacheContext(Calendar.getInstance()
						.getTime(), mappingResourceLoader);
				mappingResourceMapFromAxis.put(datamapperMediatorUuid, dmcc);
			}
		}

		return mappingResourceLoader;
	}

	/**
	 * When proxy invokes initially, this creates a mapping resource loader
	 * 
	 * @param context
	 *            message context
	 * @param configkey
	 *            the location of the mapping configuration
	 * @param inSchemaKey
	 *            the location of the input schema
	 * @param outSchemaKey
	 *            the location of the output schema
	 * @return the MappingResourceLoader object
	 * @throws IOException
	 *             throws this if any parser exception occurs while passing
	 *             inputStreams
	 */
	private static MappingResourceLoader getMappingResourceLoader(
			MessageContext context, String configkey, String inSchemaKey,
			String outSchemaKey) throws IOException {

		InputStream configFileInputStream = getInputStream(context, configkey);
		InputStream inputSchemaStream = getInputStream(context, inSchemaKey);
		InputStream outputSchemaStream = getInputStream(context, outSchemaKey);
		// Creates a new mappingResourceLoader
		mappingResourceLoader = new MappingResourceLoader(inputSchemaStream,
				outputSchemaStream, configFileInputStream);
		return mappingResourceLoader;
	}

	/**
	 * Input streams to create the the MappingResourceLoader object
	 * 
	 * @param context
	 *            Message context
	 * @param key
	 *            registry key
	 * @return mapping configuration, inputSchema and outputSchema as
	 *         inputStreams
	 */
	private static InputStream getInputStream(MessageContext context, String key) {

		InputStream inputStream = null;
		Object entry = context.getEntry(key);
		if (entry instanceof OMTextImpl) {
			if (log.isDebugEnabled()) {
				log.debug("Value for the key is ");
			}
			OMTextImpl text = (OMTextImpl) entry;
			String content = text.getText();
			inputStream = new ByteArrayInputStream(content.getBytes());
		}
		return inputStream;
	}

	private static void handleException(String message, Exception e) {
		log.error(message, e);
		throw new SynapseException(message, e);
	}

}
