package grails.plugins.crm.property

class CrmPropertyTagLib {

    static namespace = "crm"

    def crmPropertyService

    def propertyField = { attrs, body ->
        def bean = attrs.bean
        def cfg = attrs.cfg
        def template
        if (cfg.isDate()) {
            template = '/crmProperty/date'
        } else if (cfg.isNumeric()) {
            template = '/crmProperty/number'
        } else {
            template = '/crmProperty/text'
        }
        out << g.render(template: template, model: [cfg: cfg, label: cfg.name, name: cfg.param, value: crmPropertyService.getValue(bean, cfg.param)])
    }

}
