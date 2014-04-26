<%--
  ~  Copyright (c) 2008, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
  ~
  ~  Licensed under the Apache License, Version 2.0 (the "License");
  ~  you may not use this file except in compliance with the License.
  ~  You may obtain a copy of the License at
  ~
  ~        http://www.apache.org/licenses/LICENSE-2.0
  ~
  ~  Unless required by applicable law or agreed to in writing, software
  ~  distributed under the License is distributed on an "AS IS" BASIS,
  ~  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  ~  See the License for the specific language governing permissions and
  ~  limitations under the License.
  --%>

<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ page import="org.apache.axis2.context.ConfigurationContext"%>
<%@ page import="org.wso2.carbon.CarbonConstants"%>
<%@ page import="org.wso2.carbon.ui.CarbonUIUtil"%>
<%@ page import="org.wso2.carbon.utils.ServerConstants"%>
<%@ page import="org.wso2.carbon.mediator.datamapper.ui.DataMapperMediator"%>
<%@ page import="org.wso2.carbon.mediator.service.ui.Mediator"%>
<%@ page import="org.wso2.carbon.sequences.ui.util.SequenceEditorHelper"%>
<%@ page import="org.apache.synapse.mediators.Value"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://wso2.org/projects/carbon/taglibs/carbontags.jar"prefix="carbon"%>



<% 
	Mediator mediator = SequenceEditorHelper.getEditingMediator(request, session);
	if (!(mediator instanceof DataMapperMediator)) {
    // todo : proper error handling
    throw new RuntimeException("Unable to edit the mediator");
	}
	DataMapperMediator dataMapperMediator = (DataMapperMediator) mediator;

  
    String configuration = "";
    Value conKey = dataMapperMediator.getConfigurationKey();
    if(conKey != null && conKey.getKeyValue() != null) {
    	configuration = conKey.getKeyValue();
    }
    
    String inputSchema = "";
    Value inKey = dataMapperMediator.getInputSchemaKey();
    if(inKey != null && inKey.getKeyValue() != null) {
    	inputSchema = inKey.getKeyValue();
    }
    String outputSchema = "";
    Value outKey = dataMapperMediator.getOutputSchemaKey();
   if(outKey != null && outKey.getKeyValue() != null) {
    	outputSchema = outKey.getKeyValue();
    }
  
    
    
  
%>
<jsp:include page="../resources/resources-i18n-ajaxprocessor.jsp"/>

<fmt:bundle
	basename="org.wso2.carbon.mediator.datamapper.ui.i18n.Resources">
	<carbon:jsi18n
		resourceBundle="org.wso2.carbon.mediator.datamapper.ui.i18n.JSResources"
		request="<%=request%>" i18nObjectName="datamapperjsi18n" />
	<div>
		<script type="text/javascript"
			src="../datamapper-mediator/js/mediator-util.js"></script>
		<table class="normal" width="100%">
			<tr>
				<td>
					<h2>
						<fmt:message key="mediator.datamapper.header" />
					</h2>
				</td>
			</tr>
			<tr>
				<td>
					<table class="normal">
						<tr>
							<td><fmt:message key="mediator.datamapper.config" /></td>
							<td><input class="longInput" type="text"
								value="<%=configuration%>" name="config"
								id="config" readonly="true" /></td>
							<td>	<a href="#" class="registry-picker-icon-link"
										onclick="showRegistryBrowser('config','/_system/config');"><fmt:message
										key="conf.registry.keys" /></a></td>
										<td>
									<a href="#" class="registry-picker-icon-link"
										onclick="showRegistryBrowser('config','/_system/governance');"><fmt:message
										key="gov.registry.keys" /></a></td>
						</tr>
						<tr>
							<td><fmt:message key="mediator.datamapper.inputSchema"/></td>
							<td><input class="longInput" type="text"
								value="<%=inputSchema %>" name="inputSchema" id="inputSchema"
								readonly="true" /></td>
							<td>	<a href="#" class="registry-picker-icon-link"
										onclick="showRegistryBrowser('inputSchema','/_system/config');"><fmt:message
										key="conf.registry.keys" /></a></td>
										<td>
									<a href="#" class="registry-picker-icon-link"
										onclick="showRegistryBrowser('inputSchema','/_system/governance');"><fmt:message
										key="gov.registry.keys" /></a></td>
						</tr>
						<tr>
							<td><fmt:message key="mediator.datamapper.outputSchema"/></td>
							<td><input class="longInput" type="text"
								value="<%=outputSchema %>" name="outputSchema" id="outputSchema"
								readonly="true" /></td>
							<td>	<a href="#" class="registry-picker-icon-link"
										onclick="showRegistryBrowser('outputSchema','/_system/config');"><fmt:message
										key="conf.registry.keys" /></a></td>
										<td>
									<a href="#" class="registry-picker-icon-link"
										onclick="showRegistryBrowser('outputSchema','/_system/governance');"><fmt:message
										key="gov.registry.keys" /></a></td>
						</tr>
						<tr>
							<td><fmt:message key="mediator.datamapper.inputType" /></td>
							<td><select id="mediator.datamapper.inputType"
								name="mediator.datamapper.inputType">
									<%
                            if(dataMapperMediator.getInputType() == DataMapperMediator.CSV_VALUE){
                        %>
									<option value="<%=DataMapperMediator.CSV_VALUE%>"
										selected="true"><fmt:message
											key="mediator.datamapper.inputType.csv" /></option>
									<option value="<%=DataMapperMediator.XML_VALUE%>"><fmt:message
											key="mediator.datamapper.inputType.xml" /></option>
									<option value="<%=DataMapperMediator.JSON_VALUE%>"><fmt:message
											key="mediator.datamapper.inputType.json" /></option>
									<%
                            } else if(dataMapperMediator.getInputType() == DataMapperMediator.XML_VALUE){
                        %>
									<option value="<%=DataMapperMediator.CSV_VALUE%>"><fmt:message
											key="mediator.datamapper.inputType.csv" /></option>
									<option value="<%=DataMapperMediator.XML_VALUE%>" selected="true"><fmt:message
											key="mediator.datamapper.inputType.xml" /></option>
									<option value="<%=DataMapperMediator.JSON_VALUE%>"><fmt:message
											key="mediator.datamapper.inputType.json" /></option>
									<%
                            } else if (dataMapperMediator.getInputType() == DataMapperMediator.JSON_VALUE) {
                        %>
									<option value="<%=DataMapperMediator.CSV_VALUE%>"><fmt:message
											key="mediator.datamapper.inputType.csv" /></option>
									<option value="<%=DataMapperMediator.XML_VALUE%>"><fmt:message
											key="mediator.datamapper.inputType.xml" /></option>
									<option value="<%=DataMapperMediator.JSON_VALUE%>" selected="true"><fmt:message
											key="mediator.datamapper.inputType.json" /></option>
									<%}%>
							</select></td>
						</tr>
						<tr>
							<td><fmt:message key="mediator.datamapper.outputType" /></td>
							<td><select id="mediator.datamapper.outputType"
								name="mediator.datamapper.outputType">
									<%
                            if(dataMapperMediator.getOutputType() == DataMapperMediator.CSV_VALUE){
                        %>
									<option value="<%=DataMapperMediator.CSV_VALUE%>"
										selected="true"><fmt:message
											key="mediator.datamapper.outputType.csv" /></option>
									<option value="<%=DataMapperMediator.XML_VALUE%>"><fmt:message
											key="mediator.datamapper.outputType.xml" /></option>
									<option value="<%=DataMapperMediator.JSON_VALUE%>"><fmt:message
											key="mediator.datamapper.outputType.json" /></option>
									<%
                            } else if(dataMapperMediator.getOutputType() == DataMapperMediator.XML_VALUE){
                        %>
									<option value="<%=DataMapperMediator.CSV_VALUE%>"><fmt:message
											key="mediator.datamapper.outputType.csv"/></option>
									<option value="<%=DataMapperMediator.XML_VALUE%>" selected="true"><fmt:message
											key="mediator.datamapper.outputType.xml"/></option>
									<option value="<%=DataMapperMediator.JSON_VALUE%>" ><fmt:message
											key="mediator.datamapper.outputType.json"/></option>
									<%
                            } else if(dataMapperMediator.getOutputType() == DataMapperMediator.JSON_VALUE) {
                        %>
									<option value="<%=DataMapperMediator.CSV_VALUE%>"><fmt:message
											key="mediator.datamapper.outputType.csv" /></option>
									<option value="<%=DataMapperMediator.XML_VALUE%>"><fmt:message
											key="mediator.datamapper.outputType.xml" /></option>
									<option value="<%=DataMapperMediator.JSON_VALUE%>" selected="true"><fmt:message
											key="mediator.datamapper.outputType.json" /></option>
									<%}%>
							</select></td>
						</tr>
					</table>
				</td>
			</tr>
		</table>
		<a name="registryBrowserLink"></a>
		<div id="registryBrowser" style="display:none;"></div>
	</div>
</fmt:bundle>
