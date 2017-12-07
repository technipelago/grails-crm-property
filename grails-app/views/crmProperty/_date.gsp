<div class="control-group">
    <label class="control-label">
        ${label}
    </label>

    <div class="controls">
        <div class="input-append date"
             data-date="${formatDate(type: 'date', date: (value ?: new Date()))}">
            <g:textField name="property.${name}" class="span11" size="10"
                         value="${formatDate(type: 'date', date: value)}" title="${help}"/><span
                class="add-on"><i class="icon-th"></i></span>
        </div>
    </div>
</div>