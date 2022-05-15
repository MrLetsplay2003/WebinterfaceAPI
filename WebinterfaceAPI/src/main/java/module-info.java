open module me.mrletsplay.webinterfaceapi {
	exports me.mrletsplay.webinterfaceapi.webinterface.auth.impl;
	exports me.mrletsplay.webinterfaceapi.webinterface.config.setting;
	exports me.mrletsplay.webinterfaceapi.webinterface.config.setting.impl;
	exports me.mrletsplay.webinterfaceapi.webinterface.page.element.builder;
	exports me.mrletsplay.webinterfaceapi.webinterface;
	exports me.mrletsplay.webinterfaceapi.webinterface.auth;
	exports me.mrletsplay.webinterfaceapi.exception;
	exports me.mrletsplay.webinterfaceapi.webinterface.document;
	exports me.mrletsplay.webinterfaceapi.webinterface.page.element;
	exports me.mrletsplay.webinterfaceapi.webinterface.page.element.layout;
	exports me.mrletsplay.webinterfaceapi.webinterface.document.websocket;
	exports me.mrletsplay.webinterfaceapi.webinterface.page.event;
	exports me.mrletsplay.webinterfaceapi.webinterface.page.dynamic;
	exports me.mrletsplay.webinterfaceapi.webinterface.page.element.list;
	exports me.mrletsplay.webinterfaceapi.webinterface.page.action;
	exports me.mrletsplay.webinterfaceapi.webinterface.page.impl;
	exports me.mrletsplay.webinterfaceapi.webinterface.js;
	exports me.mrletsplay.webinterfaceapi.webinterface.config;
	exports me.mrletsplay.webinterfaceapi.webinterface.session;
	exports me.mrletsplay.webinterfaceapi.webinterface.markdown;
	exports me.mrletsplay.webinterfaceapi.webinterface.page.action.value;
	exports me.mrletsplay.webinterfaceapi.util;
	exports me.mrletsplay.webinterfaceapi.webinterface.page;

	requires transitive me.mrletsplay.mrcore;
	requires transitive simplehttpserver;
	requires transitive org.slf4j;
	requires transitive org.commonmark;

	requires org.apache.commons.text;
}