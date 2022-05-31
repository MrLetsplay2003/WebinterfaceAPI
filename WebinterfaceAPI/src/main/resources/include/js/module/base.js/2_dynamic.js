async function invokeDataHandler(dataHandler) {
	let handler = Function("return (" + dataHandler + ")")();

	let response = await Webinterface.call(handler.requestTarget, handler.requestMethod, handler.data, false);
	if(!response.isSuccess()) {
		WebinterfaceToast.showErrorToast("Failed to load element data");
		return null;
	}

	let data = response.getData();
	if(handler.key != null) data = data[handler.key];
	return data;
}

function convertTemplateString(templateObject, templateString) {
	return templateString.replaceAll(/\$\{([a-zA-Z0-9\?\:_]+)\}/g, (match, expr, offset, string) => {
		if(expr == "this") return templateObject;
		
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
	if(destination.classList.contains("iconify")) return; // Updating breaks Iconify icons
	
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
	if(!element.hasAttribute("data-dataHandler")) return;
	
	let data = await invokeDataHandler(element.getAttribute("data-dataHandler"));
	if(data == null) return;
	
	convertTemplateElement(data, temp.content.firstChild);

	// Replace old element with new element
	element.innerHTML = "";
	element.appendChild(temp.content);
}

async function loadList(element) {
	// Create template
	let temp = document.createElement("template");
	temp.innerHTML = element.getAttribute("data-template");

	let items = [];
	if(element.hasAttribute("data-dataHandler")) {
		// Load new data into template
		items = await invokeDataHandler(element.getAttribute("data-dataHandler"));
		if(items == null) return;
		if(!Array.isArray(items)) console.log("Data handler returned non-array type", items);
		element.setAttribute("data-listItems", JSON.stringify(items));
	}else {
		items = listGetItems(element);
	}

	loadChildren(element, items, temp);
	
	for(let child of element.children) {
		listUpdateButtons(child);
	}
}

async function loadGroup(element) {
	// Create template
	let temp = document.createElement("template");
	temp.innerHTML = element.getAttribute("data-template");

	// Load new data into template
	if(!element.hasAttribute("data-dataHandler")) return;
	
	let elements = await invokeDataHandler(element.getAttribute("data-dataHandler"));
	if(elements == null) return;
	

	loadChildren(element, elements, temp);
}

function loadChildren(element, children, template) {
	while(element.children.length > children.length) {
		element.children[element.children.length - 1].remove();
	}
	
	for(let i = 0; i < children.length; i++) {
		let oldEl = element.children[i];
		let elData = children[i];
		let newEl = template.content.firstChild.cloneNode(true);
		convertTemplateElement(elData, newEl);
		
		if(oldEl != null) {
			updateElement(oldEl, newEl);
		}else {
			element.appendChild(newEl);
		}
	}
}

function listGetItems(element) {
	return JSON.parse(element.getAttribute("data-listItems"));
}

function listGetItemsByID(elementID) {
	return listGetItems(document.getElementById(elementID));
}

function listUpdateButtons(element) {
	let upBtn = element.getElementsByClassName("list-button-up")[0];
	if(upBtn != null) {
		if(element.previousElementSibling == null) {
			upBtn.setAttribute("disabled", "");
		}else {
			upBtn.removeAttribute("disabled");
		}
	}
	
	let downBtn = element.getElementsByClassName("list-button-down")[0];
	if(downBtn != null) {
		if(element.nextElementSibling == null) {
			downBtn.setAttribute("disabled", "");
		}else {
			downBtn.removeAttribute("disabled");
		}
	}
}

function listOnChange(element, additionalInfo) {
	let onChange = element.getAttribute("data-onChange");
	if(onChange != null) eval(onChange);
}

function listSwapItems(element, other) {
	if(other == null) return;
	
	let list = element.parentElement;
	let listItems = listGetItems(list);
	
	let elIdx = Array.prototype.indexOf.call(list.children, element);
	let otherIdx = Array.prototype.indexOf.call(list.children, other);
	
	let tmp = listItems[otherIdx];
	listItems[otherIdx] = listItems[elIdx];
	listItems[elIdx] = tmp;
	list.setAttribute("data-listItems", JSON.stringify(listItems));
	
	let moveOther = element.nextElementSibling == other;
	
	list.insertBefore(element, other);
	if(moveOther) list.insertBefore(other, element);
	
	listUpdateButtons(element);
	listUpdateButtons(other);
	listOnChange(list, {action: "swap", item1: elIdx, item2: otherIdx});
}

function listRemoveItem(element) {
	let list = element.parentElement;
	let listItems = listGetItems(list);
	
	let elIdx = Array.prototype.indexOf.call(list.children, element);
	
	listItems.splice(elIdx, 1);
	list.setAttribute("data-listItems", JSON.stringify(listItems));
	
	let prev = element.previousElementSibling;
	let next = element.nextElementSibling;
	element.remove();
	
	if(prev != null) listUpdateButtons(prev);
	if(next != null) listUpdateButtons(next);
	
	listOnChange(list, {action: "remove", item: elIdx});
}

function listAddItem(element, data, triggerUpdate = true) {
	let listItems = listGetItems(element);
	
	listItems.push(data);
	element.setAttribute("data-listItems", JSON.stringify(listItems));
	
	// Create template
	let temp = document.createElement("template");
	temp.innerHTML = element.getAttribute("data-template");
	
	let newEl = temp.content.firstChild.cloneNode(true);
	convertTemplateElement(data, newEl);
	
	element.appendChild(newEl);
	
	let prev = newEl.previousElementSibling;
	if(prev != null) listUpdateButtons(prev);
	listUpdateButtons(newEl);
	
	if(triggerUpdate) listOnChange(element, {action: "add", item: data});
}

function listSetItems(element, items, triggerUpdate = true) {
	element.setAttribute("data-listItems", JSON.stringify(items));
	
	let temp = document.createElement("template");
	temp.innerHTML = element.getAttribute("data-template");
	
	loadChildren(element, items, temp);
	
	for(let child of element.children) {
		listUpdateButtons(child);
	}
	
	if(triggerUpdate) listOnChange(element, {action: "set", items: items});
}

async function loadUpdateableElements() {
	for(let el of document.getElementsByClassName("updateable-element")) {
		await loadUpdateableElement(el);
	}
	for(let el of document.getElementsByClassName("dynamic-list")) {
		await loadList(el);
	}
	for(let el of document.getElementsByClassName("dynamic-group")) {
		await loadGroup(el);
	}
}

loadUpdateableElements();
console.log("Loaded!");
