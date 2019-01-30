<r:require module="datepicker"/>
<r:script>
    $(document).ready(function() {
        <crm:datepicker/>
    });
</r:script>

<div class="row-fluid">
    <g:each in="${result}" var="cfg">
        <crm:propertyField bean="${bean}" cfg="${cfg}" value="${values ? (values[cfg.param] ?: null) : null}"/>
    </g:each>

    <div class="form-actions">
        <crm:button visual="primary" icon="icon-search icon-white" label="default.button.search.label"/>
    </div>
</div>
