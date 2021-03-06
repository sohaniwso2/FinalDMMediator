/*
*  Copyright (c) 2005-2010, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
*
*  WSO2 Inc. licenses this file to you under the Apache License,
*  Version 2.0 (the "License"); you may not use this file except
*  in compliance with the License.
*  You may obtain a copy of the License at
*
*    http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing,
* software distributed under the License is distributed on an
* "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
* KIND, either express or implied.  See the License for the
* specific language governing permissions and limitations
* under the License.
*/
package org.wso2.carbon.mediator.datamapper.ui;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.wso2.carbon.mediator.service.MediatorService;

/**
 * The bundle activator of the DataMapperMediator which implements start and stop methods
 *
 */
public class DataMapperMediatorActivator implements BundleActivator {

    private static final Log log = LogFactory.getLog(DataMapperMediatorActivator.class);

    /**
     * Start method of the DataMapperMediator
     */
    public void start(BundleContext bundleContext) throws Exception {

        if (log.isDebugEnabled()) {
            log.debug("Starting the DataMapper mediator component ...");
        }

        bundleContext.registerService(
                MediatorService.class.getName(), new DataMapperMediatorService(), null);

        if (log.isDebugEnabled()) {
            log.debug("Successfully registered the DataMapper mediator service");
        }
    }

    /**
     * Terminate method of the DataMapperMediator
     */
    public void stop(BundleContext bundleContext) throws Exception {
        if (log.isDebugEnabled()) {
            log.debug("Stopped the DataMapper mediator component ...");
        }
    }
}