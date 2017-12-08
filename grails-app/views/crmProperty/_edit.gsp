<r:require module="datepicker"/>
<r:script>
    $(document).ready(function() {
        <crm:datepicker/>
    });
</r:script>

<g:set var="usedKeys" value="${values.collect{it.key.param}}"/>
<g:set var="visibleProperties" value="${result.findAll { it.enabled || usedKeys.contains(it.param)}}"/>

<div class="row-fluid">
    <g:each in="${visibleProperties}" var="cfg">
        <crm:propertyField bean="${bean}" cfg="${cfg}"/>
    </g:each>
</div>