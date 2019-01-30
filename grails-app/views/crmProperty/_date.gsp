<%@ page import="grails.plugins.crm.core.DateUtils" %>
<div class="control-group">
    <label class="control-label">
        ${label}
    </label>
    <g:set var="dateValue" value="${(value instanceof Date) ? value : (value ? DateUtils.parseDateSafe(value) : null)}"/>
    <div class="controls">
        <div class="input-append date"
             data-date="${formatDate(type: 'date', date: (dateValue ?: new Date()))}">
            <g:textField name="property.${name}" class="span11" size="10"
                         value="${formatDate(type: 'date', date: dateValue)}" title="${help}"/><span
                class="add-on"><i class="icon-th"></i></span>
        </div>
    </div>
</div>
