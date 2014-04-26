/*
 *  Copyright (c) 2008, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */




function isContainRaw(tbody) {
    if (tbody.childNodes == null || tbody.childNodes.length == 0) {
        return false;
    } else {
        for (var i = 0; i < tbody.childNodes.length; i++) {
            var child = tbody.childNodes[i];
            if (child != undefined && child != null) {
                if (child.nodeName == "tr" || child.nodeName == "TR") {
                    return true;
                }
            }
        }
    }
    return false;
}


function getSelectedValue(id) {
    var propertyType = document.getElementById(id);
    var propertyType_indexstr = null;
    var propertyType_value = null;
    if (propertyType != null) {
        propertyType_indexstr = propertyType.selectedIndex;
        if (propertyType_indexstr != null) {
            propertyType_value = propertyType.options[propertyType_indexstr].value;
        }
    }
    return propertyType_value;
}


function DataMapperMediatorValidate() {
var conKey;
var inKey;
var outKey;
conkey = document.getElementById("config");
inKey = document.getElementById("inputSchema");
outKey = document.getElementById("outputSchema");

if (conkey && conkey.value == "") {
    CARBON.showErrorDialog(datamapperjsi18n["mediator.datamapper.script.conkey.empty"]);
    return false;
}
if (inKey && inKey.value == "") {
    CARBON.showErrorDialog(datamapperjsi18n["mediator.datamapper.script.inKey.empty"]);
    return false;
}
if (outKey && outKey.value == "") {
    CARBON.showErrorDialog(datamapperjsi18n["mediator.datamapper.script.outKey.empty"]);
    return false;
}

return true;
}


function displayElement(elementId, isDisplay) {
    var toDisplayElement = document.getElementById(elementId);
    if (toDisplayElement != null) {
        if (isDisplay) {
            toDisplayElement.style.display = '';
        } else {
            toDisplayElement.style.display = 'none';
        }
    }
}





