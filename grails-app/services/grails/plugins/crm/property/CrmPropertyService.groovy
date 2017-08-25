package grails.plugins.crm.property

import grails.plugins.crm.core.TenantUtils
import groovy.transform.CompileStatic
import org.apache.commons.lang.StringUtils

class CrmPropertyService {

    def crmCoreService

    CrmPropertyConfig createConfig(Class domainClass, String name, String label = null) {
        def cfg = new CrmPropertyConfig(clazz: domainClass.name, param: paramify(name), name: label ?: StringUtils.capitalize(name))
        cfg.save(failOnError: true)
        cfg
    }

    CrmPropertyConfig createConfig(Map<String, Object> opts) {
        def cfg = new CrmPropertyConfig(opts)
        cfg.save(failOnError: true)
        cfg
    }

    List<CrmPropertyConfig> getConfigs(Class domainClass) {
        CrmPropertyConfig.createCriteria().get() {
            eq('tenantId', TenantUtils.tenant)
            eq('clazz', domainClass.name)
            order 'orderIndex', 'asc'
        }
    }

    CrmPropertyConfig getConfig(Class domainClass, String name) {
        CrmPropertyConfig.createCriteria().get() {
            eq('tenantId', TenantUtils.tenant)
            eq('clazz', domainClass.name)
            eq('param', name)
        }
    }

    List<Map> getValues(def reference) {
        String identifier = crmCoreService.getReferenceIdentifier(reference)
        List<CrmPropertyValue> result = CrmPropertyValue.createCriteria().list() {
            eq('ref', identifier)
            cfg {
                eq('tenantId', TenantUtils.tenant)
                order 'orderIndex', 'asc'
            }
        }
        result.collect { [(it.param): it.value] }
    }

    def getValue(Object reference, String name) {
        getValueInternal(reference, name)?.value
    }

    CrmPropertyValue setValue(Object reference, String name, Object value) {
        def prop = getValueInternal(reference, name)
        if (prop == null) {
            def instance = crmCoreService.isDomainClass(reference) ? reference : crmCoreService.getReference(reference.toString())
            if (instance == null) {
                throw new RuntimeException("Domain instance [$reference] not found")
            }
            def config = getConfig(instance.getClass(), name)
            if (config == null) {
                throw new RuntimeException("No dynamic property [$name] configured for [${instance.getClass().getName()}]")
            }
            String identifier = crmCoreService.getReferenceIdentifier(instance)
            prop = new CrmPropertyValue(cfg: config, ref: identifier)
        }
        bind(prop, value)
        prop.save()
    }

    boolean deleteValue(Object reference, String name) {
        def prop = getValueInternal(reference, name)
        if (prop != null) {
            prop.delete()
            return true
        }
        return false
    }

    List find(Object... args) {
        []
    }

    private CrmPropertyValue getValueInternal(Object reference, String name) {
        def instance = crmCoreService.isDomainClass(reference) ? reference : crmCoreService.getReference(reference.toString())
        if (instance == null) {
            return null
        }
        String identifier = crmCoreService.getReferenceIdentifier(instance)
        CrmPropertyValue.createCriteria().get() {
            eq('ref', identifier)
            cfg {
                eq('tenantId', TenantUtils.tenant)
                eq('clazz', instance.getClass().getName())
                eq('param', name)
            }
        }
    }

    private static final int NAME_SIZE = 20

    @CompileStatic
    private String paramify(final String name) {
        String param = name.toLowerCase().replace(' ', '-')
        if (param.length() > NAME_SIZE) {
            param = param[0..(NAME_SIZE - 1)]
            if (param[-1] == '-') {
                param = param[0..-2]
            }
        }
        param
    }

    private void bind(CrmPropertyValue prop, Object value) {
        if (value instanceof Date) {
            prop.dateValue = value
        } else if (value instanceof Number) {
            prop.numValue = value.doubleValue()
        } else {
            prop.stringValue = value.toString()
        }
    }
}
