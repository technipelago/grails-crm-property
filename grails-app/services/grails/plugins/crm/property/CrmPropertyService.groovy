package grails.plugins.crm.property

class CrmPropertyService {

    def crmCoreService

    CrmPropertyConfig createConfig(String label) {
        def cfg = new CrmPropertyConfig(label: label)
        cfg.save(failOnError: true)
        cfg
    }

    List<CrmPropertyValue> getValues(def reference) {
        String identifier = crmCoreService.getReferenceIdentifier(reference)
        def result = CrmPropertyValue.createCriteria().list() {
            eq('ref', identifier)
        }
        result
    }

    def getValue(CrmPropertyConfig cfg, def reference) {
        String identifier = crmCoreService.getReferenceIdentifier(reference)
        def result = CrmPropertyValue.createCriteria().get() {
            eq('cfg', cfg)
            eq('ref', identifier)
        }
        result
    }

    CrmPropertyValue setValue(CrmPropertyConfig cfg, def reference, def value) {
        String identifier = crmCoreService.getReferenceIdentifier(reference)
        def prop = new CrmPropertyValue(cfg: cfg, ref: identifier)
        bind(prop, value)
        prop.save()
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
