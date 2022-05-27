class WebinterfaceBaseActions {

	static setValue(parameters) {
		document.getElementById(parameters.element).value = parameters.value;
	}

	static async sendJS(parameters) {
		let response = await Webinterface.call(parameters.requestTarget, parameters.requestMethod, parameters.value == null ? {} : parameters.value);
		if(response.isSuccess() && parameters.onSuccess != null) {
			parameters.onSuccess.action(parameters.onSuccess.parameters);
		}else if(!response.isSuccess() && parameters.onError != null) {
			parameters.onError.action(parameters.onError.parameters);
		}
	}

	static reloadPage(parameters) {
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

	static redirect(parameters) {
		window.location.href = parameters.url;
	}

	static multiAction(parameters) {
		for(let a of parameters.actions) {
			a.action(a.parameters);
		}
	}

	static confirm(parameters) {
		if(confirm("Are you sure?")) {
			parameters.action(parameters.actionParameters);
		}
	}
	
	static showLoadingScreen(parameters) {
		document.getElementById("loading-box").style.display = "flex";
	}

	static hideLoadingScreen(parameters) {
		document.getElementById("loading-box").style.display = "none";
	}

	static async updateElement(parameters) {
		let elID = parameters.element;
		
		if(elID != null) {
			let el = document.getElementById(parameters.element);
			if(el.classList.contains("updateable-element")) {
				loadUpdateableElement(el);
			}else if(el.classList.contains("dynamic-list")) {
				loadList(el);
			}else if(el.classList.contains("dynamic-group")) {
				loadGroup(el);
			}
		}else {
			loadUpdateableElements();
		}
	}

	static addValue(parameters) {
		listAddItem(document.getElementById(parameters.element), parameters.value);
	}
	
	static showToast(parameters) {
		WebinterfaceToast.showToast(parameters.message, parameters.error);
	}
	
	static async submitUpload(parameters) {
		let el = document.getElementById(parameters.element);
		let response = await new Promise(function(resolve, reject) {
			$.ajax({
				url: "/_internal/fileupload?target=" + encodeURIComponent(el.getAttribute("data-requestTarget")) + "&method=" + encodeURIComponent(el.getAttribute("data-requestMethod")),
				method: "POST",
				contentType: false,
				data: new FormData(el),
				processData: false,
				timeout: 120000,
				cache: false,
				success: function(response, status) {
					let r = new WebinterfaceResponse(response.success, response.data, response.message);
					if(!r.isSuccess()) WebinterfaceToast.showErrorToast("Error: " + r.getErrorMessage());
					resolve(r);
				},
				error: function(xhr, status, error) {
					WebinterfaceToast.showErrorToast("Request error: " + error);
					resolve(new WebinterfaceResponse(false, null, "Request error: " + error));
				}
			});
		});
		
		if(response.isSuccess() && parameters.onSuccess != null) {
			parameters.onSuccess.action(parameters.onSuccess.parameters);
		}else if(!response.isSuccess() && parameters.onError != null) {
			parameters.onError.action(parameters.onError.parameters);
		}
	}

}
