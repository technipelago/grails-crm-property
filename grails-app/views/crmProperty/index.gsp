<%@ page import="grails.util.GrailsNameUtils" %>
<!DOCTYPE html>
<html>
<head>
    <meta name="layout" content="main">
    <title><g:message code="crmProperty.list.title"/></title>
</head>

<body>

<crm:header title="crmProperty.list.title"/>

<table class="table table-striped">
    <thead>
    <tr>
        <th></th>
    </tr>
    </thead>
    <tbody>
    <g:each in="${keys}" var="entity">
        <tr>

            <td>
                <h3><g:message code="${entity + '.label'}" default="${entity}"/></h3>
            </td>
        </tr>
        <g:each in="${configs[entity]}" var="cfg">
            <tr>
                <td>
                    <g:link action="edit" id="${cfg.id}">${cfg.name}</g:link>
                </td>
            </tr>
        </g:each>

        <tr>
            <td>
                <g:form class="form-actions btn-toolbar">
                    <input type="hidden" name="type" value="${entity}"/>
                    <crm:button type="link" group="true" action="create" visual="success" icon="icon-file icon-white"
                                label="crmProperty.button.create.label" permission="crmProperty:create"/>
                </g:form>
            </td>
        </tr>
    </g:each>
    </tbody>
</table>




</body>
</html>