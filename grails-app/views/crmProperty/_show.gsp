<dl>
    <g:each in="${result}" var="cfg">
        <g:set var="dynamicValue" value="${bean.getDynamicProperty(cfg.param)}"/>
        <dt>${cfg.name}</dt>
        <dd>
            <crm:propertyValue bean="${bean}" cfg="${cfg}"/>
        </dd>
    </g:each>
</dl>