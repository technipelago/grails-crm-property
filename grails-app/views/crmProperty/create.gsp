<!DOCTYPE html>
<html>
<head>
    <meta name="layout" content="main">
    <g:set var="entityName" value="${bean.entityName}"/>
    <title><g:message code="crmProperty.create.title"/></title>
</head>

<body>

<crm:header title="crmProperty.create.title" subtitle="${message(code: entityName + '.label', default: entityName)}"
            args="${[bean, entityName]}"/>

<g:hasErrors bean="${bean}">
    <crm:alert class="alert-error">
        <ul>
            <g:eachError bean="${bean}" var="error">
                <li <g:if test="${error in org.springframework.validation.FieldError}">data-field-id="${error.field}"</g:if>><g:message
                        error="${error}"/></li>
            </g:eachError>
        </ul>
    </crm:alert>
</g:hasErrors>

<g:form action="create">

    <input type="hidden" name="entity" value="${entityName}"/>
    <input type="hidden" name="type" value="${bean.typeName}"/>

    <div class="control-group">
        <label class="control-label">
            <g:message code="crmPropertyConfig.name.label"/>
        </label>

        <div class="controls">
            <g:textField name="name" value="${bean.name}" class="span6"/>
        </div>
    </div>

    <div class="control-group">
        <label class="control-label">
            <g:message code="crmPropertyConfig.description.label"/>
        </label>

        <div class="controls">
            <g:textArea name="description" value="${bean.description}" rows="3" class="span6"/>
        </div>
    </div>

    <div class="control-group">
        <label class="control-label">
            <g:message code="crmPropertyConfig.param.label"/>
        </label>

        <div class="controls">
            <g:textField name="param" value="${bean.param}" class="span6"/>
        </div>
    </div>

    <div class="control-group">
        <label class="control-label">
            <g:message code="crmPropertyConfig.orderIndex.label"/>
        </label>

        <div class="controls">
            <g:textField name="orderIndex" value="${bean.orderIndex}" class="span2"/>
        </div>
    </div>

    <div class="control-group">
        <label class="control-label">
            <g:message code="crmPropertyConfig.text.minLength.label"/>
        </label>

        <div class="controls">
            <g:textField name="minLength" value="" class="span2"/>
        </div>
    </div>

    <div class="control-group">
        <label class="control-label">
            <g:message code="crmPropertyConfig.text.maxLength.label"/>
        </label>

        <div class="controls">
            <g:textField name="maxLength" value="" class="span2"/>
        </div>
    </div>

    <div class="form-actions">
        <crm:button visual="success" icon="icon-ok icon-white" label="crmProperty.button.save.label"/>
        <crm:button type="link" action="index" icon="icon-remove" label="crmProperty.button.back.label"/>
    </div>
</g:form>

</body>
</html>