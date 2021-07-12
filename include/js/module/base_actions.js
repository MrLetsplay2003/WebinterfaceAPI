class WebinterfaceBaseActions {

	static setValue(element, event, parameters) {
		document.getElementById(parameters.element).value = parameters.value;
	}

	static async sendJS(element, event, parameters) {
		let response = await Webinterface.call(parameters.requestTarget, parameters.requestMethod, {value: parameters.value == null ? null : parameters.value});
		if(response.isSuccess() && parameters.onSuccess != null) {
			parameters.onSuccess.action(parameters.onSuccess.parameters);
		}else if(!response.isSuccess() && parameters.onError != null) {
			parameters.onError.action(parameters.onError.parameters);
		}
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
		window.location.href = parameters.url;
	}

    static multiAction(element, event, parameters) {
		for(let a of parameters.actions) {
			console.log(a);
			a.action(element, event, a.parameters);
		}
	}

    static confirm(element, event, parameters) {
		if(confirm("Are you sure?")) {
			parameters.action(element, event, parameters.actionParameters);
		}
	}

}
