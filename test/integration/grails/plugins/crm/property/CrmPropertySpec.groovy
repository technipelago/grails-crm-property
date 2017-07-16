package grails.plugins.crm.property

class CrmPropertySpec extends grails.test.spock.IntegrationSpec {

    def crmPropertyService

    def "test string value"() {
        when:
        def entity = new TestEntity(name: "Test").save(failOnError: true)
        def cfg = crmPropertyService.createConfig("Test")
        def value = crmPropertyService.setValue(cfg, entity, "foo")

        then:
        value != null
        value.value == "foo"
    }

    def "test numeric value"() {
        when:
        def entity = new TestEntity(name: "Test").save(failOnError: true)
        def cfg = crmPropertyService.createConfig("Test")
        def value = crmPropertyService.setValue(cfg, entity, 42)

        then:
        value != null
        value.value == 42
    }

    def "test date value"() {
        when:
        def entity = new TestEntity(name: "Test").save(failOnError: true)
        def cfg = crmPropertyService.createConfig("Test")
        def date = new Date()
        def value = crmPropertyService.setValue(cfg, entity, date)

        then:
        value != null
        value.value == date
    }
}
