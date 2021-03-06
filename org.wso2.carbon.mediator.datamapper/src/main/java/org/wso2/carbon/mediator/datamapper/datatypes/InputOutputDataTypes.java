package org.wso2.carbon.mediator.datamapper.datatypes;



public class InputOutputDataTypes {
	
	/*public static final MediaType APPLICATION_XML_TYPE;
	public static final MediaType APPLICATION_JSON_TYPE;
	public static final java.lang.String APPLICATION_JSON;
	*/
	
	private final static String CSV_CONTENT_TYPE = "text/csv";
	private final static String XML_CONTENT_TYPE = javax.ws.rs.core.MediaType.APPLICATION_XML;
	private final static String JSON_CONTENT_TYPE =  javax.ws.rs.core.MediaType.APPLICATION_JSON;

	
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
