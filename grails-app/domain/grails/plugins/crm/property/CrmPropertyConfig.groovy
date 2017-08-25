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

    String clazz
    int type = TYPE_STRING

    static constraints = {
        clazz(maxSize: 80, nullable: false, unique: ['tenantId', 'param'])
        type(inList: [TYPE_STRING, TYPE_NUMERIC, TYPE_DATE])
    }

    static mapping = {
        tenantId index: 'crm_dyn_prop_idx'
        clazz index: 'crm_dyn_prop_idx'
        name index: 'crm_dyn_prop_idx'
        sort 'orderIndex'
        cache usage: 'nonstrict-read-write'
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
                    eq('clazz', this.clazz)
                }
            }
            orderIndex = mx ? mx + 1 : 1
        }
    }
}
