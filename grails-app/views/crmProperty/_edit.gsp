<div class="row-fluid">
    <g:each in="${result}" var="cfg">
        <crm:propertyField bean="${bean}" cfg="${cfg}"/>
    </g:each>
</div>