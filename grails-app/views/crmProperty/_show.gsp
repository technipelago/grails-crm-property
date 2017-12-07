<r:script>
    $(document).ready(function () {
        $('a.crm-show-disabled').click(function (ev) {
            ev.preventDefault();
            $('#disabled-properties').toggleClass('hidden');
        });
    });
</r:script>

<g:set var="enabledProperties" value="${result.findAll { it.enabled }}"/>
<g:set var="disabledProperties" value="${result.findAll { !it.enabled }}"/>

<g:set var="columns1"
       value="${enabledProperties.collate((int) (result.size() / 2 + 0.5))}"/>

<div class="row-fluid">
    <g:each in="${columns1}" var="col">
        <div class="span6">
            <dl class="crm-property-list">
                <g:each in="${col}" var="cfg">
                    <g:if test="${bean.getCustomProperty(cfg.param)}">
                        <dt>${cfg.name}</dt>
                        <dd data-property="${cfg.param}" title="${cfg.description}">
                            <crm:propertyValue bean="${bean}" cfg="${cfg}"/>
                        </dd>
                    </g:if>
                </g:each>
            </dl>
        </div>
    </g:each>
</div>

<g:if test="${disabledProperties}">

    <g:set var="columns2"
           value="${disabledProperties.collate((int) (result.size() / 2 + 0.5))}"/>

    <hr/>

    <a href="#" class="crm-show-disabled"><g:message code="crmProperty.show.disabled.label"
                                                     args="${[disabledProperties.size()]}"/></a>

    <div id="disabled-properties" class="row-fluid hidden">
        <g:each in="${columns2}" var="col">
            <div class="span6">
                <dl class="crm-property-list">
                    <g:each in="${col}" var="cfg">
                        <g:if test="${bean.getCustomProperty(cfg.param)}">
                            <dt>${cfg.name}</dt>
                            <dd data-property="${cfg.param}" title="${cfg.description}">
                                <crm:propertyValue bean="${bean}" cfg="${cfg}"/>
                            </dd>
                        </g:if>
                    </g:each>
                </dl>
            </div>
        </g:each>
    </div>
</g:if>