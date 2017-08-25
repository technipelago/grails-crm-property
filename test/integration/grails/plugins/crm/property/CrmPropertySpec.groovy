package grails.plugins.crm.property

class CrmPropertySpec extends grails.test.spock.IntegrationSpec {

    def crmPropertyService

    def "test string value"() {
        given:
        def cfg = crmPropertyService.createConfig(TestEntity, "Test")

        when:
        def entity = new TestEntity(name: "Test").save(failOnError: true)
        def value = crmPropertyService.setValue(entity, "test", "foo")

        then:
        value != null
        value.value == "foo"
    }

    def "test numeric value"() {
        given:
        def cfg = crmPropertyService.createConfig(TestEntity, "Test")

        when:
        def entity = new TestEntity(name: "Test").save(failOnError: true)
        def value = crmPropertyService.setValue(entity, "test", 42)

        then:
        value != null
        value.value == 42
    }

    def "test date value"() {
        given:
        def cfg = crmPropertyService.createConfig(TestEntity, "Test")

        when:
        def entity = new TestEntity(name: "Test").save(failOnError: true)
        def date = new Date()
        def value = crmPropertyService.setValue(entity, "test", date)

        then:
        value != null
        value.value == date
    }

    def "test domain methods"() {
        given:
        def cfg = crmPropertyService.createConfig(TestEntity, "Test")

        when:
        def entity = new TestEntity(name: "Test").save(failOnError: true)
        def value = entity.setDynamicProperty("test", "foo")

        then:
        value != null
        value.value == "foo"
        entity.getDynamicProperty("test") == "foo"
    }
}
