package grails.plugins.crm.property

class CrmPropertyValue {
    String ref
    String stringValue
    Double numValue
    Date dateValue

    CrmPropertyConfig cfg

    static constraints = {
        ref(maxSize: 80, nullable: false)
        stringValue(maxSize: 255, nullable: true)
        numValue(nullable: true)
        dateValue(nullable: true)
    }

    static transients = ['value']

    transient Object getValue() {
        if (stringValue != null) {
            return stringValue
        }
        if (numValue != null) {
            return numValue
        }
        return dateValue
    }
}
