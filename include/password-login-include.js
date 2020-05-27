async function login() {
	let uEl = document.getElementById("username-input");
	let pEl = document.getElementById("password-input");

	if(uEl.value.isEmpty() || pEl.value.isEmpty()) {
		WebinterfaceUtils.notifyError("You need to input both username and password");
		return;
	}

	await Webinterface.call("webinterface", "passwordLogin", {username: uEl.value, password: pEl.value});
}