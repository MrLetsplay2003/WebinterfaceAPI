/*jshint esversion: 6*/

class Webinterface {

	static call(target, method, data = {}) {
		console.log("call", target, method, data);
		return new Promise(function(resolve, reject) {
			$.ajax({
				url: "/_internal/call",
				method:"POST",
				contentType: "application/json",
				data: JSON.stringify({target: target, method: method, data: data}),
				timeout: 10000,
				cache: false,
				success: function(response, status) {
					let r = new WebinterfaceResponse(true, response, null);
					if(!r.isSuccess()) WebinterfaceUtils.notify("Error: " + r.getErrorMessage());
					resolve(r);
				},
				error: function(xhr, status, error) {
					WebinterfaceUtils.notify(error);
					resolve(new WebinterfaceResponse(false, null, "Request error: " + error));
				}
			});
		});
	}

}

class WebinterfaceUtils {

	static getElementAttributeById(elementId, attributeName) {
		return document.getElementById(elementId).getAttribute(attributeName);
	}

	static notify(message) {
		alert(message);
	}

}

class WebinterfaceResponse {

	constructor(success, response, errorMessage) {
		this.success = success;
		this.response = response;
		this.errorMessage = errorMessage;
	}

	isSuccess() {
		return this.success && this.response.success;
	}

	getRawResponse() {
		if(!this.isSuccess()) throw "Not a success";
		return this.response;
	}

	getData() {
		if(!this.isSuccess()) throw "Not a success";
		return this.response.data;
	}

	getErrorMessage() {
		if(this.isSuccess()) throw "Not an error";
		return this.errorMessage || this.response.message;
	}

}

function toggleSidebar() {
	$(".sidenav").toggle();
}

function toggleProfileOptions() {
	$(".profile-options").toggle();
}