function createAdminAccount() {
	let uEl = document.getElementById("admin-name");
	let pEl = document.getElementById("admin-password");
	let pEl2 = document.getElementById("admin-password-repeat");

	if(uEl.value.trim() === "" || pEl.value.trim() === "" || pEl2.value.trim() === "") {
		WebinterfaceToast.showErrorToast("You need to input both username and password");
		return;
	}
	
	if(!/^[a-zA-Z0-9-_.]{2,64}$/.test(uEl.value)) {
		WebinterfaceToast.showErrorToast("Username contains invalid characters or is too long/short");
		return;
	}
	
	if(pEl.value != pEl2.value) {
		WebinterfaceToast.showErrorToast("The passwords don't match");
		return;
	}

	let form = document.getElementById("setup-form");
	form.submit();
}

function configureAuth() {
	let discordAuth = document.getElementById("discord-auth");
	if(discordAuth.checked) {
		let discordClID = document.getElementById("discord-client-id");
		let discordClSecret = document.getElementById("discord-client-secret");
		if(discordClID.value.trim() === "" || discordClSecret.value.trim() === "") {
			WebinterfaceToast.showErrorToast("When enabling Discord auth, you need to set client id and client secret");
			return;
		}
	}
	
	let googleAuth = document.getElementById("google-auth");
	if(googleAuth.checked) {
		let googleClID = document.getElementById("google-client-id");
		let googleClSecret = document.getElementById("google-client-secret");
		if(googleClID.value.trim() === "" || googleClSecret.value.trim() === "") {
			WebinterfaceToast.showErrorToast("When enabling Google auth, you need to set client id and client secret");
			return;
		}
	}
	
	let githubAuth = document.getElementById("github-auth");
	if(githubAuth.checked) {
		let githubClID = document.getElementById("github-client-id");
		let githubClSecret = document.getElementById("github-client-secret");
		if(githubClID.value.trim() === "" || githubClSecret.value.trim() === "") {
			WebinterfaceToast.showErrorToast("When enabling GitHub auth, you need to set client id and client secret");
			return;
		}
	}
	
	let form = document.getElementById("setup-form");
	form.submit();
}

function configureHTTP() {
	let httpB = document.getElementById("http-bind");
	let httpH = document.getElementById("http-host");
	let httpP = document.getElementById("http-port");
	
	if(httpB.value.trim() === "" || httpH.value.trim() === "" || httpP.value.trim() === "") {
		WebinterfaceToast.showErrorToast("You need to set HTTP IP bind, host and port");
		return
	}
	
	if(!/^\d+$/.test(httpP.value)) {
		WebinterfaceToast.showErrorToast("Invalid port");
		return;
	}
	
	let enableHttps = document.getElementById("enable-https").checked;
	if(enableHttps) {
		let httpsB = document.getElementById("https-bind");
		let httpsH = document.getElementById("https-host");
		let httpsP = document.getElementById("https-port");
		
		if(httpsB.value.trim() === "" || httpsH.value.trim() === "" || httpsP.value.trim() === "") {
			WebinterfaceToast.showErrorToast("You need to set HTTPS IP bind, host and port");
			return
		}
		
		if(!/^\d+$/.test(httpsP.value)) {
			WebinterfaceToast.showErrorToast("Invalid port");
			return;
		}
		
		let certP = document.getElementById("https-cert-path");
		let certKP = document.getElementById("https-cert-key-path");
		
		if(certP.value.trim() === "" || certKP.value.trim() === "") {
			WebinterfaceToast.showErrorToast("You need to set HTTPS certificate path and certificate key path");
			return
		}
	}
	
	let form = document.getElementById("setup-form");
	form.submit();
}

function setupDone() {
	window.location.href = "/";
}
