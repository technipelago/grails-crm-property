<dl class="crm-property-list">
    <g:each in="${result}" var="cfg">
        <g:set var="dynamicValue" value="${bean.getCustomProperty(cfg.param)}"/>
        <g:if test="${dynamicValue}">
            <dt>${cfg.name}</dt>
            <dd data-property="${cfg.param}">
                <crm:propertyValue bean="${bean}" cfg="${cfg}"/>
            </dd>
        </g:if>
    </g:each>
</dl>