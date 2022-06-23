function setupConfirm() {
	let data = {};
	for(let el of document.getElementsByClassName("setup-element")) {
		let type = el.getAttribute("data-type");
		console.log(type);
		
		switch(type) {
			case "string":
			case "password":
				data[el.getAttribute("data-name")] = el.value;
				break;
			case "boolean":
				data[el.getAttribute("data-name")] = el.checked;
				break;
			case "integer":
				data[el.getAttribute("data-name")] = parseInt(el.value);
				break;
			case "double":
				data[el.getAttribute("data-name")] = parseFloat(el.value);
				break;
		}
	}
	
	$.ajax({
		url: "/setup/submit",
		type: "POST",
		data: JSON.stringify(data),
		contentType: "application/json",
		dataType: "text/plain",
		statusCode: {
			400: function(res) {
				WebinterfaceToast.showErrorToast(res.responseText);
			},
			200: function() {
				window.location.reload();
			}
		}
	});
}
