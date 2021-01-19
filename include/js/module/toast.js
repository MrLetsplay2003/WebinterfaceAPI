let wiData = {
	alerts: [],
	alertWaiting: false
};

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
		wiData.alerts.push({
			msg: message,
			isError: isError
		});
	}

	static updateToasts() {
		if(wiData.alertWaiting) return;
		if(wiData.alerts.length == 0) return;
		wiData.alertWaiting = true;
		let alert = wiData.alerts.shift();
		let alertBox = $("#alert-box");

		alertBox.html(alert.msg);
		
		if(alert.isError) {
			alertBox.css({backgroundColor: "#e63d00"});
		}else {
			alertBox.css({backgroundColor: "#11CC52"});
		}

		alertBox.animate({
			opacity: 1,
			bottom: 0
		}, 500, function() {
			// Finished
		});
		
		alertBox.delay(2500).animate({
			//opacity: 0,
			bottom: "-100%"
		}, 500, function() {
			// Finished
			alertBox.css({bottom: "-100%"});
			wiData.alertWaiting = false;
		});
	}

}

setInterval(function() {
    WebinterfaceToast.updateToasts();
}, 1000);