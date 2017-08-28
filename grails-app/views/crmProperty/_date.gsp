<div class="control-group">
    <label class="control-label">
        ${label}
    </label>

    <div class="controls">
        <div class="input-append date"
             data-date="${formatDate(type: 'date', date: (value ?: new Date()))}">
            <g:textField name="${name}" class="span11" size="10"
                         value="${formatDate(type: 'date', date: value)}"/><span
                class="add-on"><i class="icon-th"></i></span>
        </div>
    </div>
</div>