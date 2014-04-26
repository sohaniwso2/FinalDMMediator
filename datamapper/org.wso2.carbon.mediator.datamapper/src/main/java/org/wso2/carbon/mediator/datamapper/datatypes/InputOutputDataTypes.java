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

/**
 * Defines input output data types
 * 
 */
public class InputOutputDataTypes {

	private final static String CSV_CONTENT_TYPE = "text/csv";
	private final static String XML_CONTENT_TYPE = javax.ws.rs.core.MediaType.APPLICATION_XML;
	private final static String JSON_CONTENT_TYPE = javax.ws.rs.core.MediaType.APPLICATION_JSON;

	// Use to define input and output data formats
	public enum DataType {
		CSV(CSV_CONTENT_TYPE), XML(XML_CONTENT_TYPE), JSON(JSON_CONTENT_TYPE);
		private final String value;

		private DataType(String value) {
			this.value = value;
		}

		@Override
		public String toString() {
			return value;
		}

		// Use to get the DataType from the relevant input and output data type
		public static DataType fromString(String dataType) {
			if (dataType != null) {
				for (DataType definedTypes : DataType.values()) {
					if (dataType.equalsIgnoreCase(definedTypes.toString())) {
						return definedTypes;
					}
				}
			}
			return null;
		}

	};

}
