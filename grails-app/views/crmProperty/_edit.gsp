<r:require modules="datepicker"/>
<r:script>
    $(document).ready(function() {
        <crm:datepicker/>
    });
</r:script>

<div class="row-fluid">
    <g:each in="${result}" var="cfg">
        <crm:propertyField bean="${bean}" cfg="${cfg}"/>
    </g:each>
</div>