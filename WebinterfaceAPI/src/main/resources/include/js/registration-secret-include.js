function register(event) {
	event.preventDefault();
	
	let sEl = document.getElementById("secret-input");
	console.log(sEl.value);

	if(sEl.value.trim() === "") {
		WebinterfaceToast.showErrorToast("You need to input the secret");
		return;
	}

	let form = document.getElementById("login-form");
	form.submit();
}
