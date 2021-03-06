/*
 * Copyright (c) 2017 Goran Ehrsson.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import org.codehaus.groovy.grails.commons.GrailsClassUtils

class CrmPropertyGrailsPlugin {
    def groupId = ""
    def version = "2.4.0-SNAPSHOT"
    def grailsVersion = "2.4 > *"
    def dependsOn = [:]
    def loadAfter = ['crmCore']
    def pluginExcludes = [
            "grails-app/conf/ApplicationResources.groovy",
            "grails-app/domain/grails/plugins/crm/property/TestEntity.groovy",
            "src/groovy/grails/plugins/crm/property/PropertyTestSecurityDelegate.groovy",
            "grails-app/views/error.gsp"
    ]
    def title = "GR8 CRM Custom Properties"
    def author = "Goran Ehrsson"
    def authorEmail = "goran@technipelago.se"
    def description = '''\
A GR8 CRM plugin that provides user defined properties on any domain instance.
'''
    def documentation = "http://gr8crm.github.io/plugins/crm-property/"
    def license = "APACHE"
    def organization = [name: "Technipelago AB", url: "http://www.technipelago.se/"]
    def issueManagement = [system: "github", url: "https://github.com/goeh/grails-crm-property/issues"]
    def scm = [url: "https://github.com/goeh/grails-crm-property"]

    def features = {
        crmProperty {
            description "Custom properties"
            link controller: "crmProperty", action: "index"
            permissions {
                user "crmProperty:*"
                admin "crmProperty:*"
            }
        }
    }

    def doWithDynamicMethods = { ctx ->
        def crmPropertyService = ctx.getBean("crmPropertyService")
        for (domainClass in application.domainClasses) {
            def customProperty = getCustomProperty(domainClass)
            if (customProperty) {
                addDomainMethods(domainClass.clazz.metaClass, crmPropertyService)
            }
        }
    }

    def onChange = { event ->
        def ctx = event.ctx
        if (event.source && ctx && event.application) {
            def service = ctx.getBean('crmPropertyService')
            // enhance domain classes with custom properties
            if ((event.source instanceof Class) && application.isDomainClass(event.source)) {
                def domainClass = application.getDomainClass(event.source.name)
                if (getCustomProperty(domainClass)) {
                    addDomainMethods(domainClass.metaClass, service)
                }
            }
        }
    }

    private void addDomainMethods(MetaClass mc, def service) {
        mc.setCustomProperty = { String propertyName, Object value ->
            service.setValue(delegate, propertyName, value)
        }
        mc.getCustomProperty = { String propertyName ->
            service.getValue(delegate, propertyName)
        }
        mc.getCustomProperties = { ->
            service.getValues(delegate)
        }
        mc.deleteCustomProperty = { String propertyName ->
            service.deleteValue(delegate, propertyName)
            return delegate
        }
        mc.static.find = { String propertyName, Object[] args ->
            service.find(delegate, *args)
        }
    }

    private getCustomProperty(domainClass) {
        // dynamicProperties is a legacy name used by a deprecated plugin.
        GrailsClassUtils.getStaticPropertyValue(domainClass.clazz, "dynamicProperties") ?: GrailsClassUtils.getStaticPropertyValue(domainClass.clazz, "customProperties")
    }
}
