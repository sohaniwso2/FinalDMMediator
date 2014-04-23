package org.wso2.carbon.mediator.datamapper.datatypes;

import java.io.IOException;

import org.apache.avro.generic.GenericRecord;
import org.apache.axiom.om.OMElement;

/**
 * Interface for writer classes
 * 
 */
public interface OutputWriter {

	public OMElement getOutputMessage(String outputType,
			GenericRecord result) throws IOException;

}
