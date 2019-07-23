/*jshint esversion: 6*/

class Webinterface {

	static call(target, method, data = {}) {
		console.log("call", target, method, data);
		return new Promise(function(resolve, reject) {
			$.ajax({
				url: "/_internal/call",
				method:"POST",
				data: data,
				timeout: 10000,
				cache: false,
				success: function(response, status) {
					try {
						let obj = JSON.parse(response);
						resolve(new WebinterfaceResponse(true, obj, null));
					}catch(e) {
						resolve(new WebinterfaceResponse(false, null, "Exception: " + e));
					}
				},
				error: function(xhr, status, error) {
					resolve(new WebinterfaceResponse(false, null, "Request error: " + error));
				}
			});
		});
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
		return this.errorMessage || this.response.errorMessage;
	}

}