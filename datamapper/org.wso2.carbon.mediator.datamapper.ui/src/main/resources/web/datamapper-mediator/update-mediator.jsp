<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1" %>
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
<%@ page import="org.wso2.carbon.mediator.service.ui.Mediator" %>
<%@ page import="org.wso2.carbon.mediator.datamapper.ui.DataMapperMediator" %>
<%@ page import="org.wso2.carbon.sequences.ui.util.SequenceEditorHelper" %>
<%@ page import="org.apache.synapse.mediators.Value"%>

<%
    Mediator mediator = SequenceEditorHelper.getEditingMediator(request, session);
    if (!(mediator instanceof DataMapperMediator)) {
        // todo : proper error handling
        throw new RuntimeException("Unable to edit the mediator");
    }
    
    
    DataMapperMediator dataMapperMediator = (DataMapperMediator) mediator;
    
    String configuration = request.getParameter("config");
    if(configuration!=null && !configuration.equals("")) {
    	Value conKey = new Value(configuration);
        dataMapperMediator.setConfigurationKey(conKey);
    }
    String inputSchema = request.getParameter("inputSchema");
    if(inputSchema!=null && !inputSchema.equals("")) {
    	Value inKey = new Value(inputSchema);
        dataMapperMediator.setInputSchemaKey(inKey);
    }
    String outputSchema = request.getParameter("outputSchema");
    if(outputSchema!=null && !outputSchema.equals("")) {
    	Value outKey = new Value(outputSchema);
        dataMapperMediator.setOutputSchemaKey(outKey);
    }

    dataMapperMediator.setInputType(Integer.parseInt(request.getParameter("mediator.datamapper.inputType")));
    dataMapperMediator.setOutputType(Integer.parseInt(request.getParameter("mediator.datamapper.outputType")));


%>

