

class WebinterfaceToast {

    static getElementAttributeById(elementId, attributeName) {
		return document.getElementById(elementId).getAttribute(attributeName);
	}

	static showInfoToast(message) {
		WebinterfaceToast.showToast(message, false);
	}

	static showErrorToast(message) {
		WebinterfaceToast.showToast(message, true);
	}

	static showToast(message, isError = true) {
		WebinterfaceToast.wiData.alerts.push({
			msg: message,
			isError: isError
		});
	}

	static updateToasts() {
		if(WebinterfaceToast.wiData.alertWaiting) return;
		if(WebinterfaceToast.wiData.alerts.length == 0) return;
		WebinterfaceToast.wiData.alertWaiting = true;
		let alert = WebinterfaceToast.wiData.alerts.shift();
		let alertBox = $("#alert-box");

		alertBox.text(alert.msg);
		
		if(alert.isError) {
			alertBox.css({backgroundColor: "#e63d00"});
		}else {
			alertBox.css({backgroundColor: "#0eb348"});
		}

		alertBox.animate({
			opacity: 1,
			bottom: 0
		}, 500);
		
		alertBox.delay(2500).animate({
			bottom: "-100%"
		}, 500, function() {
			// Finished
			alertBox.css({bottom: "-100%"});
			WebinterfaceToast.wiData.alertWaiting = false;
		});
	}

}

WebinterfaceToast.wiData = {
	alerts: [],
	alertWaiting: false
};

setInterval(function() {
    WebinterfaceToast.updateToasts();
}, 500);
