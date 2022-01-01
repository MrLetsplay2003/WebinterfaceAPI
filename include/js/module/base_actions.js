class WebinterfaceBaseActions {

	static setValue(element, event, parameters) {
		document.getElementById(parameters.element).value = parameters.value;
	}

	static async sendJS(element, event, parameters) {
		let response = await Webinterface.call(parameters.requestTarget, parameters.requestMethod, {value: parameters.value == null ? null : parameters.value});
		if(response.isSuccess() && parameters.onSuccess != null) {
			parameters.onSuccess.action(element, event, parameters.onSuccess.parameters);
		}else if(!response.isSuccess() && parameters.onError != null) {
			parameters.onError.action(element, event, parameters.onError.parameters);
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
			a.action(element, event, a.parameters);
		}
	}

    static confirm(element, event, parameters) {
		if(confirm("Are you sure?")) {
			parameters.action(element, event, parameters.actionParameters);
		}
	}
	
	static showLoadingScreen(element, event, parameters) {
		document.getElementById("loading-box").style.display = "flex";
	}

	static hideLoadingScreen(element, event, parameters) {
		document.getElementById("loading-box").style.display = "none";
	}

	static async updateUpdateableElement(element, event, parameters) {
		let orig = document.getElementById(parameters.element);

		// Create template
		let temp = document.createElement("template");
		temp.innerHTML = orig.getAttribute("data-template");

		// Load new data into template
		let requestTarget = orig.getAttribute("data-updateRequestTarget");
		let requestMethod = orig.getAttribute("data-updateRequestMethod");
		let response = await Webinterface.call(requestTarget, requestMethod, null, false);
		if(!response.isSuccess()) {
			WebinterfaceToast.showErrorToast("Failed to load template object");
			return;
		}
		convertTemplateElement(response.getData(), temp.content.firstChild);

		// Replace old element with new element
		orig.innerHTML = "";
		orig.appendChild(temp.content);
	}

}
