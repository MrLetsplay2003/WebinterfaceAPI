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
					if(p.getRequestTarget() == customHandler.requestTarget && p.getRequestMethod() == customHandler.requestMethod) {
						customHandler.handler(p);
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