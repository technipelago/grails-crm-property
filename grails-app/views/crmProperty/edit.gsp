<!DOCTYPE html>
<html>
<head>
    <meta name="layout" content="main">
    <g:set var="entityName" value="${bean.entityName}"/>
    <title><g:message code="crmProperty.edit.title" args="${[bean, entityName]}"/></title>
</head>

<body>

<crm:header title="crmProperty.edit.title" subtitle="${message(code: entityName + '.label', default: entityName)}"
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

<g:form action="edit">

    <input type="hidden" name="id" value="${bean.id}"/>

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
        <label class="checkbox">
            <g:checkBox name="enabled" value="true" checked="${bean.enabled}"/>
            <g:message code="crmPropertyConfig.enabled.label"/>
        </label>
    </div>

    <g:if test="${bean.isText()}">
        <div class="control-group">
            <label class="control-label">
                <g:message code="crmPropertyConfig.text.minLength.label"/>
            </label>

            <div class="controls">
                <g:textField name="minLength" value="${bean.minLength}" class="span2"/>
            </div>
        </div>

        <div class="control-group">
            <label class="control-label">
                <g:message code="crmPropertyConfig.text.maxLength.label"/>
            </label>

            <div class="controls">
                <g:textField name="maxLength" value="${bean.maxLength}" class="span2"/>
            </div>
        </div>
    </g:if>

    <div class="form-actions">
        <crm:button visual="warning" icon="icon-ok icon-white" label="crmProperty.button.update.label"/>
        <crm:button type="link" action="index" icon="icon-remove" label="crmProperty.button.back.label"/>
    </div>
</g:form>

</body>
</html>