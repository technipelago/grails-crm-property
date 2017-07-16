package grails.plugins.crm.property

class CrmPropertyConfig {
    String label

    static constraints = {
        label(maxSize: 80, nullable: false)
    }
}
