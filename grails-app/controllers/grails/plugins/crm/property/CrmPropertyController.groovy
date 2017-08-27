package grails.plugins.crm.property

import grails.plugins.crm.core.TenantUtils
import grails.util.GrailsNameUtils

class CrmPropertyController {

    def crmPropertyService

    def index() {
        def domainClasses = crmPropertyService.getPropertyDomainClasses()
        def result = [:]
        for (entity in domainClasses) {
            def name = GrailsNameUtils.getPropertyName(entity.name)
            for (cfg in crmPropertyService.getConfigs(name)) {
                result.get(name, []) << cfg
            }
        }
        return [keys: result.keySet().sort(), configs: result]
    }

    def create(String type) {
        def cfg = new CrmPropertyConfig(tenantId: TenantUtils.tenant, entityName: type)
        [bean: cfg]
    }

    def edit(Long id) {
        def cfg = CrmPropertyConfig.get(id)
        [bean: cfg]
    }

    def delete(Long id) {
        def cfg = CrmPropertyConfig.get(id)
        if (cfg) {
            cfg.delete(flush: true)
            redirect action: "index"
        } else {
            response.sendError(404)
        }
    }
}
