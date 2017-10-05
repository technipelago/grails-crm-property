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

    def propertyValue = { attrs, body ->
        def bean = attrs.bean
        def cfg = attrs.cfg
        def value = crmPropertyService.getValue(bean, cfg.param)
        if (cfg.isDate()) {
            value = formatDate(type: 'date', date: value)
        } else if (cfg.isNumeric()) {
            value = formatNumber(number: value)
        } else {
            value = value.toString()
        }
        out << value
    }

}
