package org.wso2.carbon.mediator.datamapper.datatypes;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 * Factory class for writer classes
 * 
 */
public class OutputWriterFactory {
	
	private static final Log log = LogFactory.getLog(OutputWriterFactory.class);

	public static OutputWriter getWriter(String dataType) {
		if (log.isDebugEnabled()) {
			log.debug("Output data type .."
					+ dataType);
		}
		if (dataType.equals(InputOutputDataTypes.DataType.CSV.toString())) {
			return new CSVWriter();
		} else if (dataType
				.equals(InputOutputDataTypes.DataType.XML.toString())) {
			return new XMLWriter();
		} else if (dataType.equals(InputOutputDataTypes.DataType.JSON
				.toString())) {
			return new JSONWriter();

		} else {
			return new JSONWriter();
		}

	}

}
