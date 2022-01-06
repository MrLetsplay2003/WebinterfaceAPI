class Webinterface {

	static customHandlers = [];
	static webSocket = null;
	static packetQueue = [];

	static callOld(target, method, data = {}, suppressAlert = false) {
		return new Promise(function(resolve, reject) {
			$.ajax({
				url: "/_internal/call",
				method: "POST",
				contentType: "application/json",
				data: JSON.stringify({target: target, method: method, data: data}),
				timeout: 120000,
				cache: false,
				success: function(response, status) {
					let r = new WebinterfaceResponse(response.success, response.data, response.message);
					if(!r.isSuccess() && !suppressAlert) WebinterfaceToast.showErrorToast("Error: " + r.getErrorMessage());
					resolve(r);
				},
				error: function(xhr, status, error) {
					if(!suppressAlert) WebinterfaceToast.showErrorToast("Request error: " + error);
					resolve(new WebinterfaceResponse(false, null, "Request error: " + error));
				}
			});
		});
	}

	static async connect() {
		Webinterface.packetQueue = [];
		return new Promise(async(resolve, reject) => {
			Webinterface.webSocket = new WebSocket(window.location.protocol.replace("http", "ws") + "//" + window.location.hostname + ":" + window.location.port + "/_internal/ws");

			Webinterface.webSocket.onopen = function(event) {
				resolve(event);
				Webinterface.webSocket.send(Packet.of(null, null, {sessionID: getCookie("wi_sessid")}).serialize());
			};
			
			Webinterface.webSocket.onerror = function(event) {
				reject(event);
			};

			Webinterface.webSocket.onclose = function(event) {
				WebinterfaceToast.showErrorToast("Connection closed: " + event.code + (event.reason != "" ? " (" + event.reason + ")" : ""));
				Webinterface.webSocket = null;
			};

			Webinterface.webSocket.onmessage = function(event) {
				let packet = Packet.deserialize(JSON.parse(event.data));
				if(packet.getReferrerID() != null) {
					for(let idx in Webinterface.packetQueue) {
						let p = Webinterface.packetQueue[idx];
						if(p.packet.getID() == packet.getReferrerID()) {
							p.resolve(packet);
							Webinterface.packetQueue.splice(idx, 1);
							return;
						}
					}
				}

				for(let customHandler of Webinterface.customHandlers) {
					if(packet.getRequestTarget() == customHandler.requestTarget && packet.getRequestMethod() == customHandler.requestMethod) {
						customHandler.handler(packet);
					}
				}
			}
		});
	}

	static call(target, method, data = {}, suppressAlert = false) {
		return new Promise((resolve, reject) => {
			Webinterface.sendPacket(Packet.of(target, method, data)).then(r => {
				if(!r.isSuccess() && !suppressAlert) WebinterfaceToast.showErrorToast("Error: " + r.getErrorMessage());
				resolve(new WebinterfaceResponse(r.isSuccess(), r.getData(), r.getErrorMessage()));
			});
		});
	}

	static subscribe(target, eventName, handler) {
		Webinterface.customHandlers.push({requestTarget: target, requestMethod: eventName, handler: handler});
		Webinterface.sendPacket(Packet.of("webinterface", "subscribeToEvent", {eventTarget: target, eventName: eventName})).then(r => {
			if(!r.isSuccess()) WebinterfaceToast.showErrorToast("Failed to subscribe to event: " + r.getErrorMessage());
		});
	}

	static sendPacket(packet) {
		return new Promise(async(resolve, _) => {
			if(Webinterface.webSocket == null) {
				await Webinterface.connect();
			}

			Webinterface.packetQueue.push({packet: packet, resolve: resolve});
			Webinterface.webSocket.send(packet.serialize());
		});
	}

	static registerHandler(target, method, handler) {
		Webinterface.customHandlers.push({requestTarget: target, requestMethod: method, handler: handler});
	}

}

class Packet {

	constructor(id, referrerID, requestTarget, requestMethod, success, errorMessage, data) {
		this.id = id;
		this.referrerID = referrerID;
		this.requestTarget = requestTarget;
		this.requestMethod = requestMethod;
		this.success = success;
		this.errorMessage = errorMessage;
		this.data = data;
	}

	getID() {
		return this.id;
	}

	getReferrerID() {
		return this.referrerID;
	}

	getRequestTarget() {
		return this.requestTarget;
	}

	getRequestMethod() {
		return this.requestMethod;
	}

	isSuccess() {
		return this.success;
	}

	getErrorMessage() {
		return this.errorMessage;
	}

	getData() {
		return this.data;
	}

	serialize() {
		return JSON.stringify(this);
	}

	static deserialize(rawPacket) {
		return new Packet(rawPacket.id, rawPacket.referrerID, rawPacket.requestTarget, rawPacket.requestMethod, rawPacket.success, rawPacket.errorMessage, rawPacket.data);
	}

	static of(requestTarget, requestMethod, data) {
		//return new Packet(Packet.randomID(), null, true, botIdentifier, guildID, requestMethod, data, null); TODO
		return new Packet(Packet.randomID(), null, requestTarget, requestMethod, true, null, data);
	}

	static randomID() {
		return ([1e7]+-1e3+-4e3+-8e3+-1e11).replace(/[018]/g, c => (c ^ crypto.getRandomValues(new Uint8Array(1))[0] & 15 >> c / 4).toString(16));
	}

}

class WebinterfaceResponse {

	constructor(success, data, errorMessage) {
		this.success = success;
		this.data = data;
		this.errorMessage = errorMessage;
	}

	isSuccess() {
		return this.success;
	}

	getData() {
		if(!this.isSuccess()) throw "Not a success";
		return this.data;
	}

	getErrorMessage() {
		if(this.isSuccess()) throw "Not an error";
		return this.errorMessage;
	}

}

function toggleSidebar() {
	$(".sidenav").toggle();
}

function toggleProfileOptions() {
	$(".profile-options").toggle();
}

function getCookie(cname) {
	let name = cname + "=";
	let decodedCookie = decodeURIComponent(document.cookie);
	let ca = decodedCookie.split(';');
	for(let i = 0; i <ca.length; i++) {
		let c = ca[i];
		while (c.charAt(0) == ' ') {
			c = c.substring(1);
		}
		if (c.indexOf(name) == 0) {
			return c.substring(name.length, c.length);
		}
	}
	return "";
}

function convertTemplateString(templateObject, templateString) {
	return templateString.replace(/\$\{([a-zA-Z0-9\?\:_]+)\}/, (match, expr, offset, string) => {
		if(expr.includes("?")) {
			let condExpr = expr.split("?");
			let cond = condExpr[0];
			let ops = condExpr[1].split(":");
			return templateObject[cond] ? ops[0] : ops[1];
		}else {
			return templateObject[expr];
		}
	});
}

function convertTemplateElement(templateObject, templateElement) {
	if(templateElement.hasAttributes()) {
		let newAttrs = {};

		for(let i = 0; i < templateElement.attributes.length; i++) {
			let a = templateElement.attributes[i];
			if(a.name == "data-template") {
				newAttrs[a.name] = a.value;
				continue; // Ignore template attribute
			}
			let attrName = convertTemplateString(templateObject, a.name);
			if(attrName.length == 0) continue;
			newAttrs[attrName] = convertTemplateString(templateObject, a.value);
		}

		while(templateElement.attributes.length > 0) {
			templateElement.removeAttribute(templateElement.attributes[0].name);
		}

		for(let k in newAttrs) {
			templateElement.setAttribute(k, newAttrs[k]);
		}
	}

	for(let i = 0; i < templateElement.childNodes.length; i++) {
		let n = templateElement.childNodes[i];

		switch(n.nodeType) {
			case Node.TEXT_NODE:
				n.nodeValue = convertTemplateString(templateObject, n.nodeValue);
				break;
			case Node.ELEMENT_NODE:
				convertTemplateElement(templateObject, n);
				break;
		}
	}
}

async function loadUpdateableElement(element) {
	// Create template
	let temp = document.createElement("template");
	temp.innerHTML = element.getAttribute("data-template");

	// Load new data into template
	let requestTarget = element.getAttribute("data-dataRequestTarget");
	let requestMethod = element.getAttribute("data-dataRequestMethod");

	let response = await Webinterface.call(requestTarget, requestMethod, null, false);
	if(!response.isSuccess()) {
		WebinterfaceToast.showErrorToast("Failed to load template object");
		return;
	}

	convertTemplateElement(response.getData(), temp.content.firstChild);

	// Replace old element with new element
	element.innerHTML = "";
	element.appendChild(temp.content);
}

async function loadDynamicList(element) {
	// Create template
	let temp = document.createElement("template");
	temp.innerHTML = element.getAttribute("data-template");

	// Load new data into template
	let requestTarget = element.getAttribute("data-dataRequestTarget");
	let requestMethod = element.getAttribute("data-dataRequestMethod");

	let response = await Webinterface.call(requestTarget, requestMethod, null, false);
	if(!response.isSuccess()) {
		WebinterfaceToast.showErrorToast("Failed to load template object");
		return;
	}

	element.innerHTML = "";
	for(let o of response.getData().elements) {
		let el = temp.content.firstChild.cloneNode(true);
		convertTemplateElement(o, el);
		element.appendChild(el);
	}
}

async function loadDynamicGroup(element) {
	// Create template
	let temp = document.createElement("template");
	temp.innerHTML = element.getAttribute("data-template");

	// Load new data into template
	let requestTarget = element.getAttribute("data-dataRequestTarget");
	let requestMethod = element.getAttribute("data-dataRequestMethod");

	let response = await Webinterface.call(requestTarget, requestMethod, null, false);
	if(!response.isSuccess()) {
		WebinterfaceToast.showErrorToast("Failed to load template object");
		return;
	}

	element.innerHTML = "";
	for(let o of response.getData().elements) {
		let el = temp.content.firstChild.cloneNode(true);
		convertTemplateElement(o, el);
		element.appendChild(el);
	}
}

async function dynamicListElementUp(element) {
	let params = dynamicListElementParams(element.parentElement);
	await WebinterfaceBaseActions.sendJS({
		requestTarget: params.requestTarget,
		requestMethod: params.requestMethod,
		value: {
			action: "swap",
			item1: params.id,
			item2: params.before
		}
	});
	loadDynamicList(element.parentElement.parentElement);
}

async function dynamicListElementDown(element) {
	let params = dynamicListElementParams(element.parentElement);
	await WebinterfaceBaseActions.sendJS({
		requestTarget: params.requestTarget,
		requestMethod: params.requestMethod,
		value: {
			action: "swap",
			item1: params.id,
			item2: params.after
		}
	});
	loadDynamicList(element.parentElement.parentElement);
}

async function dynamicListElementRemove(element) {
	let params = dynamicListElementParams(element.parentElement);
	await WebinterfaceBaseActions.sendJS({
		requestTarget: params.requestTarget,
		requestMethod: params.requestMethod,
		value: {
			action: "remove",
			item: params.id
		}
	});
	loadDynamicList(element.parentElement.parentElement);
}

function dynamicListElementParams(element) {
	return {
		requestTarget: element.parentElement.getAttribute("data-updateRequestTarget"),
		requestMethod: element.parentElement.getAttribute("data-updateRequestMethod"),
		id: element.getAttribute("data-elementId"),
		before: element.getAttribute("data-elementBefore"),
		after: element.getAttribute("data-elementAfter")
	};
}

async function loadUpdateableElements() {
	for(let el of document.getElementsByClassName("updateable-element")) {
		await loadUpdateableElement(el);
	}
	for(let el of document.getElementsByClassName("dynamic-list")) {
		await loadDynamicList(el);
	}
	for(let el of document.getElementsByClassName("dynamic-group")) {
		await loadDynamicGroup(el);
	}
}

loadUpdateableElements();
console.log("Loaded!");