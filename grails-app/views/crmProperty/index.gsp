<%@ page import="grails.util.GrailsNameUtils" %>
<!DOCTYPE html>
<html>
<head>
    <meta name="layout" content="main">
    <title><g:message code="crmProperty.index.title"/></title>
</head>

<body>

<crm:header title="crmProperty.index.title"/>

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
                <g:form>
                    <input type="hidden" name="type" value="${entity}"/>

                    <div class="btn-group">
                        <button class="btn btn-success dropdown-toggle" data-toggle="dropdown">
                            <g:message code="crmProperty.button.create.label"/>
                            <span class="caret"></span>
                        </button>
                        <ul class="dropdown-menu">
                            <li>
                                <g:link action="create" params="${[entity: entity, type: 'string']}">
                                    <g:message code="crmProperty.create.string.label"/>
                                </g:link>
                            </li>
                            <li>
                                <g:link action="create" params="${[entity: entity, type: 'number']}">
                                    <g:message code="crmProperty.create.number.label"/>
                                </g:link>
                            </li>
                            <li>
                                <g:link action="create" params="${[entity: entity, type: 'date']}">
                                    <g:message code="crmProperty.create.date.label"/>
                                </g:link>
                            </li>
                            <li>
                                <g:link action="create" params="${[entity: entity, type: 'checkbox']}">
                                    <g:message code="crmProperty.create.checkbox.label"/>
                                </g:link>
                            </li>
                            <li>
                                <g:link action="create" params="${[entity: entity, type: 'radio']}">
                                    <g:message code="crmProperty.create.radio.label"/>
                                </g:link>
                            </li>
                        </ul>
                    </div>
                </g:form>
            </td>
        </tr>
    </g:each>
    </tbody>
</table>




</body>
</html>