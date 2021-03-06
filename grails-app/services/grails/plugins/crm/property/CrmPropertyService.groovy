package grails.plugins.crm.property

import grails.events.Listener
import grails.plugins.crm.core.TenantUtils
import grails.util.GrailsNameUtils
import groovy.transform.CompileStatic
import org.apache.commons.lang.StringUtils
import org.codehaus.groovy.grails.commons.GrailsClassUtils
import org.grails.databinding.SimpleMapDataBindingSource
import org.grails.plugin.platform.events.EventMessage
import org.springframework.validation.Errors

import java.util.regex.Pattern

class CrmPropertyService {

    private static final Pattern NUMBER_PATTERN = Pattern.compile('(\\d+)')

    def grailsApplication
    def crmCoreService
    def grailsWebDataBinder

    List getPropertyDomainClasses() {
        grailsApplication.domainClasses.findAll { getCustomProperty(it) }
    }

    CrmPropertyConfig createStringConfig(Class domainClass, String name, String label = null) {
        createConfig(domainClass, name, CrmPropertyConfig.TYPE_STRING, label)
    }

    CrmPropertyConfig createNumberConfig(Class domainClass, String name, String label = null) {
        createConfig(domainClass, name, CrmPropertyConfig.TYPE_NUMERIC, label)
    }

    CrmPropertyConfig createDateConfig(Class domainClass, String name, String label = null) {
        createConfig(domainClass, name, CrmPropertyConfig.TYPE_DATE, label)
    }

    CrmPropertyConfig createConfig(Class domainClass, String name, Integer type = CrmPropertyConfig.TYPE_STRING, String label = null) {
        createConfig(entityName: GrailsNameUtils.getPropertyName(domainClass), param: paramify(name), type: type, name: label ?: StringUtils.capitalize(name))
    }

    CrmPropertyConfig createConfig(Map<String, Object> opts) {
        def cfg = new CrmPropertyConfig(opts)
        if (!opts.containsKey('enabled')) {
            cfg.enabled = true
        }
        cfg.save(failOnError: true)
        cfg
    }

    List<CrmPropertyConfig> getConfigs(Class domainClass) {
        getConfigs(GrailsNameUtils.getPropertyName(domainClass))
    }

    List<CrmPropertyConfig> getConfigs(String entityName) {
        CrmPropertyConfig.createCriteria().list() {
            eq('tenantId', TenantUtils.tenant)
            eq('entityName', entityName)
            order 'orderIndex', 'asc'
        }
    }

    CrmPropertyConfig getConfig(Class domainClass, String name) {
        CrmPropertyConfig.createCriteria().get() {
            eq('tenantId', TenantUtils.tenant)
            eq('entityName', GrailsNameUtils.getPropertyName(domainClass))
            or {
                eq('param', name)
                ilike('name', name)
            }
        }
    }

    Map<CrmPropertyConfig, Object> getValues(def reference) {
        String identifier = crmCoreService.getReferenceIdentifier(reference)
        Map<CrmPropertyConfig, Object> values = [:]
        List<CrmPropertyValue> result = CrmPropertyValue.createCriteria().list() {
            eq('ref', identifier)
            cfg {
                eq('tenantId', TenantUtils.tenant)
                order 'orderIndex', 'asc'
            }
        }
        for (value in result) {
            values[value.cfg] = value.getValue()
        }
        return values
    }

    def getValue(Object reference, String name) {
        getValueInternal(reference, name)?.value
    }

    CrmPropertyValue setValue(Object reference, String name, Object value) {
        def prop = getValueInternal(reference, name)

        if (value == null || value.toString().trim().length() == 0) {
            if (prop != null) {
                prop.delete() // Don't persist null values.
            }
            return null
        }

        if (prop == null) {
            def instance = crmCoreService.isDomainClass(reference) ? reference : crmCoreService.getReference(reference.toString())
            if (instance == null) {
                throw new RuntimeException("Domain instance [$reference] not found")
            }
            def config = getConfig(instance.getClass(), name)
            if (config == null) {
                throw new RuntimeException("No custom property [$name] configured for [${instance.getClass().getName()}]")
            }
            String identifier = crmCoreService.getReferenceIdentifier(instance)
            prop = new CrmPropertyValue(cfg: config, ref: identifier)
        }

        value = sanatize(value, prop.cfg)

        if (value != null) {
            bind(prop, value)
            prop.save()
        } else if (prop.id) {
            prop.delete() // Don't persist null values.
            return null
        }

        return prop
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

    private Object sanatize(Object value, CrmPropertyConfig cfg) {
        switch (cfg.type) {
            case CrmPropertyConfig.TYPE_NUMERIC:
                def matcher = NUMBER_PATTERN.matcher(value.toString())
                return matcher.find() ? matcher.group(1) : null
        }
        return value
    }

    private getCustomProperty(domainClass) {
        GrailsClassUtils.getStaticPropertyValue(domainClass.clazz, "dynamicProperties") ?: GrailsClassUtils.getStaticPropertyValue(domainClass.clazz, "customProperties")
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
                eq('entityName', GrailsNameUtils.getPropertyName(instance.getClass()))
                or {
                    eq('param', name)
                    ilike('name', name)
                }
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

    private Errors bind(CrmPropertyValue prop, Object value) {
        def config = prop.cfg
        def params = [:]
        if (config.isDate()) {
            params.dateValue = value
        } else if (config.isNumeric()) {
            params.numValue = value
        } else {
            params.stringValue = value
        }

        grailsWebDataBinder.bind(prop, params as SimpleMapDataBindingSource)

        return prop.errors
    }

    @Listener(namespace = '*', topic = 'bind')
    def bindData(EventMessage<Map> event) {
        def ns = event.namespace
        def values = event.data.params?.get('property')
        def bean = event.data.bean ?: event.data[ns]
        if (log.isDebugEnabled()) {
            log.debug "Binding user defined properties for $ns $values"
        }
        values.each { k, v ->
            try {
                def prop = setValue(bean, k, v)
                // prop can be null if it was removed, so we have to use safe navigation here.
                if (prop?.hasErrors()) {
                    bean.errors.reject('default.invalid.property.message', [k, bean.class, v].toArray(), "Custom property [{0}] of class [{1}] with value [{2}] does not pass custom validation")
                }
            } catch (Exception e) {
                log.debug(e.message)
                bean.errors.reject('default.invalid.property.message', [k, bean.class, v].toArray(), "Custom property [{0}] of class [{1}] with value [{2}] does not pass custom validation")
            }
        }
    }

    @Listener(namespace = "crmTenant", topic = "requestDelete")
    Map requestDeleteTenant(event) {
        def tenant = event.id
        def count = CrmPropertyConfig.countByTenantId(tenant)
        return count ? [namespace: "crmProperty", topic: "deleteTenant"] : null
    }

    @Listener(namespace = "crmProperty", topic = "deleteTenant")
    def deleteTenant(event) {
        def tenant = event.id

        // Delete all property values.
        def result = CrmPropertyValue.createCriteria().list() {
            cfg {
                eq('tenantId', tenant)
            }
        }
        result*.delete()

        // Delete all property configurations.
        result = CrmPropertyConfig.findAllByTenantId(tenant)
        int n = result.size()
        result*.delete()

        log.warn("Deleted $n custom properties in tenant $tenant")
    }

    @Listener(namespace = "*", topic = "deleted")
    def cleanupProperties(EventMessage<Map> event) {
        Map data = event.getData()
        if (data.id) {
            def ref = "${event.namespace}@${data.id}".toString()
            CrmPropertyValue.findAllByRef(ref)*.delete()
            if (log.isDebugEnabled()) {
                log.debug "Deleted all custom properties for $ref"
            }
        }
    }
}
