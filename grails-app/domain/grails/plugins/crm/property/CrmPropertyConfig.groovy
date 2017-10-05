package grails.plugins.crm.property

import grails.plugins.crm.core.CrmLookupEntity
import grails.plugins.crm.core.TenantUtils

class CrmPropertyConfig extends CrmLookupEntity {

    public static final int TYPE_STRING = 0
    public static final int TYPE_NUMERIC = 2
    public static final int TYPE_DATE = 4

    //int orderIndex -> Input field order
    //boolean enabled -> false means not visible for input
    //String name -> Input field label
    //String param -> source/config/integration parameter name
    //String icon -> not normally used?
    //String description -> help (hover) text

    String entityName
    int type = TYPE_STRING
    Integer minLength
    Integer maxLength

    static constraints = {
        entityName(maxSize: 80, nullable: false, unique: ['tenantId', 'param'])
        type(inList: [TYPE_STRING, TYPE_NUMERIC, TYPE_DATE])
        minLength(nullable: true)
        maxLength(nullable: true)
    }

    static mapping = {
        tenantId index: 'crm_dyn_prop_idx'
        entityName index: 'crm_dyn_prop_idx'
        name index: 'crm_dyn_prop_idx'
        sort 'orderIndex'
        cache usage: 'nonstrict-read-write'
    }

    static transients = ['date', 'numeric', 'text', 'typeName']

    transient boolean isDate() {
        this.type == TYPE_DATE
    }

    transient boolean isNumeric() {
        this.type == TYPE_NUMERIC
    }

    transient boolean isText() {
        this.type == TYPE_STRING
    }

    transient String getTypeName() {
        switch(this.type) {
            case TYPE_NUMERIC:
                return 'numeric'
            case TYPE_DATE:
                return 'date'
            default:
                return 'string'
        }
    }

    def beforeValidate() {
        if (orderIndex == 0) {
            def tenant = TenantUtils.getTenant()
            def mx
            withNewSession {
                mx = CrmPropertyConfig.createCriteria().get {
                    projections {
                        max "orderIndex"
                    }
                    eq('tenantId', tenant)
                    eq('entityName', this.entityName)
                }
            }
            orderIndex = mx ? mx + 1 : 1
        }
    }
}
