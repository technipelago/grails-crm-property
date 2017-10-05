package grails.plugins.crm.property

import grails.plugins.crm.core.TenantUtils
import grails.transaction.Transactional
import grails.util.GrailsNameUtils

import javax.servlet.http.HttpServletResponse

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

    @Transactional
    def create(String entity, String type) {
        def cfg = new CrmPropertyConfig(tenantId: TenantUtils.tenant, entityName: entity, type: getType(type))
        if (request.post) {
            bindData(cfg, params, [exclude: ['entity', 'type']])
            if (cfg.save()) {
                flash.success = 'Configuration created'
                redirect action: 'index'
                return
            }
        } else {
            cfg.validate()
            cfg.clearErrors()
        }
        [bean: cfg]
    }

    @Transactional
    def edit(Long id) {
        def cfg = CrmPropertyConfig.get(id)
        if (cfg?.tenantId != TenantUtils.tenant) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND)
            return
        }
        if (request.post) {
            bindData(cfg, params, [exclude: ['id']])
            if (cfg.save()) {
                flash.success = 'Configuration updated'
                redirect action: 'index'
                return
            }
        }
        [bean: cfg]
    }

    @Transactional
    def delete(Long id) {
        def cfg = CrmPropertyConfig.get(id)
        if (cfg) {
            cfg.delete(flush: true)
            flash.warning = 'Configuration deleted'
            redirect action: "index"
        } else {
            response.sendError(404)
        }
    }

    private int getType(String type) {
        switch (type) {
            case 'string':
            case 'text':
                return CrmPropertyConfig.TYPE_STRING
            case 'numeric':
            case 'number':
                return CrmPropertyConfig.TYPE_NUMERIC
            case 'date':
                return CrmPropertyConfig.TYPE_DATE
            default:
                throw new IllegalArgumentException("Unsupported property type: $type")
        }
    }
}
