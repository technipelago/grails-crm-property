<dl>
    <g:each in="${result}" var="cfg">
        <dt>${cfg.name}</dt>
        <dd>
            ${bean.getDynamicProperty(cfg.param)}&nbsp;
        </dd>
    </g:each>
</dl>