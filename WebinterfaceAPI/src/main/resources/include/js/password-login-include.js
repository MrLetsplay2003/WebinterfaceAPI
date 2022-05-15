if(window.location.search.length > 1) {
	let b64 = window.location.search.substr(1);
	try {
		WebinterfaceToast.showErrorToast(atob(b64));
	}catch(e) {}
}

function login(register) {
	let uEl = document.getElementById("username-input");
	let pEl = document.getElementById("password-input");

	if(uEl.value.trim() === "" || pEl.value.trim() === "") {
		WebinterfaceToast.showErrorToast("You need to input both username and password");
		return;
	}

	document.getElementById("register-input").checked = register;

	let form = document.getElementById("login-form");
	form.submit();
}
