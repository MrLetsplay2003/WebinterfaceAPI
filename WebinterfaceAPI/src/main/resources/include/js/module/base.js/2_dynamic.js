function convertTemplateString(templateObject, templateString) {
	return templateString.replaceAll(/\$\{([a-zA-Z0-9\?\:_]+)\}/g, (match, expr, offset, string) => {
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

function updateElement(destination, source) {
	let attrsSet = [];
	for(let i = 0; i < source.attributes.length; i++) {
		let a = source.attributes[i];
		if(a.name == "data-template") continue; // Ignore template attribute
		if(destination.getAttribute(a.name) != a.value) destination.setAttribute(a.name, a.value);
		attrsSet.push(a.name);
	}

	for(let i = 0; i < destination.attributes.length; i++) {
		let attr = destination.attributes[i];
		if(attr.name == "data-template" || attr.name == "data-elementid" || attrsSet.includes(attr.name)) continue;
		destination.removeAttribute(attr.name);
		i--;
	}

	for(let i = 0; i < source.childNodes.length; i++) {
		let n = destination.childNodes[i];

		switch(n.nodeType) {
			case Node.TEXT_NODE:
				n.nodeValue = source.childNodes[i].nodeValue;
				break;
			case Node.ELEMENT_NODE:
				updateElement(n, source.childNodes[i]);
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
	
	let items = response.getData().items;
	element.setAttribute("data-listItems", JSON.stringify(items));

	loadDynamicChildren(element, items, temp);
	
	for(let child of element.children) {
		updateButtons(child);
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

	loadDynamicChildren(element, response.getData().elements, temp);
}

function loadDynamicChildren(element, children, template) {
	let newIDs = children.map(el => el._id);

	// Remove all elements that no longer exist
	for(let c of element.children) {
		if(newIDs.includes(c.getAttribute("data-elementId"))) continue; // Element still exists
		c.remove();
	}

	// Update existing elements
	for(let i = 0; i < element.children.length; i++) {
		let oldEl = element.children[i];
		let elData = children.find(e => e._id == oldEl.getAttribute("data-elementId"));
		let newEl = template.content.firstChild.cloneNode(true);
		convertTemplateElement(elData, newEl);
		updateElement(oldEl, newEl);
	}

	// Reorder elements and insert new ones
	for(let i = 0; i < newIDs.length; i++) {
		if(element.children[i] != null && element.children[i].getAttribute("data-elementId") == newIDs[i]) continue; // Correct element in the correct place

		// Search for existing element
		let el = null;
		for(let c of element.children) {
			if(c.getAttribute("data-elementId") == newIDs[i]) {
				el = c;
				break;
			}
		}

		if(el == null) {
			el = template.content.firstChild.cloneNode(true);
			el.setAttribute("data-elementId", children[i]._id);
			convertTemplateElement(children[i], el);
		}

		element.insertBefore(el, element.children[i + 1]);
	}
}

function updateButtons(element) {
	if(element.previousElementSibling == null) {
		element.getElementsByClassName("list-button-up")[0].setAttribute("disabled", "");
	}else {
		element.getElementsByClassName("list-button-up")[0].removeAttribute("disabled");
	}
	
	if(element.nextElementSibling == null) {
		element.getElementsByClassName("list-button-down")[0].setAttribute("disabled", "");
	}else {
		element.getElementsByClassName("list-button-down")[0].removeAttribute("disabled");
	}
}

function dynamicListOnChange(element, additionalInfo) {
	console.log("List has changed", element, additionalInfo);
	
	let onChange = element.getAttribute("data-onChange");
	if(onChange != null) eval(onChange);
}

async function dynamicListElementSwap(element, other) {
	if(other == null) return;
	
	let list = element.parentElement;
	let listItems = JSON.parse(list.getAttribute("data-listItems"));
	
	let elIdx = Array.prototype.indexOf.call(list.children, element);
	let otherIdx = Array.prototype.indexOf.call(list.children, other);
	
	let tmp = listItems[otherIdx];
	listItems[otherIdx] = listItems[elIdx];
	listItems[elIdx] = tmp;
	list.setAttribute("data-listItems", JSON.stringify(listItems));
	
	let moveOther = element.nextElementSibling == other;
	
	list.insertBefore(element, other);
	if(moveOther) list.insertBefore(other, element);
	
	updateButtons(element);
	updateButtons(other);
	dynamicListOnChange(list, {action: "swap", item1: elIdx, item2: otherIdx});
}

async function dynamicListElementRemove(element) {
	let list = element.parentElement;
	let listItems = JSON.parse(list.getAttribute("data-listItems"));
	
	let elIdx = Array.prototype.indexOf.call(list.children, element);
	
	listItems.splice(elIdx, 1);
	list.setAttribute("data-listItems", JSON.stringify(listItems));
	
	let prev = element.previousElementSibling;
	let next = element.nextElementSibling;
	element.remove();
	
	if(prev != null) updateButtons(prev);
	if(next != null) updateButtons(next);
	
	dynamicListOnChange(list, {action: "remove", item: elIdx});
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
