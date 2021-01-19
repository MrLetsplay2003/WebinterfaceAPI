class WebinterfaceBaseActions {

    static setValue(element, event, parameters) {
        document.getElementById(parameters.element).value = eval(parameters.value);
    }

    static sendJS(element, event, parameters) {
        Webinterface.call(parameters.requestTarget, parameters.requestMethod, {value: parameters.value == null ? null : eval(parameters.value)});
    }

    static reloadPage(element, event, parameters) {
        let c = () => {
            if(parameters.forceReload) {
                window.location.href = window.location.href;
            }else {
                window.location.reload();
            }
        }

        if(parameters.delay == 0) {
            c();
        }else {
            setTimeout(c, parameters.delay);
        }
    }

    static redirect(element, event, parameters) {
        window.location.href = eval(parameters.url);
    }

    static multiAction(element, event, parameters) {
        for(let a of parameters.actions) {
            eval(a.name)(element, event, a.parameters);
        }
    }

    static confirm(element, event, parameters) {
        if(confirm("Are you sure?")) {
            eval(parameters.actionName)(parameters.actionParameters);
        }
    }

}