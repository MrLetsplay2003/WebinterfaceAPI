import java.sql.Driver;

open module me.mrletsplay.webinterfaceapi {
	exports me.mrletsplay.webinterfaceapi;
	exports me.mrletsplay.webinterfaceapi.auth.impl;
	exports me.mrletsplay.webinterfaceapi.config.setting;
	exports me.mrletsplay.webinterfaceapi.config.setting.impl;
	exports me.mrletsplay.webinterfaceapi.page.element.builder;
	exports me.mrletsplay.webinterfaceapi.auth;
	exports me.mrletsplay.webinterfaceapi.exception;
	exports me.mrletsplay.webinterfaceapi.document;
	exports me.mrletsplay.webinterfaceapi.page.element;
	exports me.mrletsplay.webinterfaceapi.page.element.layout;
	exports me.mrletsplay.webinterfaceapi.document.websocket;
	exports me.mrletsplay.webinterfaceapi.page.event;
	exports me.mrletsplay.webinterfaceapi.page.dynamic;
	exports me.mrletsplay.webinterfaceapi.page.element.list;
	exports me.mrletsplay.webinterfaceapi.page.action;
	exports me.mrletsplay.webinterfaceapi.page.impl;
	exports me.mrletsplay.webinterfaceapi.js;
	exports me.mrletsplay.webinterfaceapi.config;
	exports me.mrletsplay.webinterfaceapi.session;
	exports me.mrletsplay.webinterfaceapi.markdown;
	exports me.mrletsplay.webinterfaceapi.page.action.value;
	exports me.mrletsplay.webinterfaceapi.util;
	exports me.mrletsplay.webinterfaceapi.page;
	exports me.mrletsplay.webinterfaceapi.page.data;
	exports me.mrletsplay.webinterfaceapi.context;
	exports me.mrletsplay.webinterfaceapi.setup;
	exports me.mrletsplay.webinterfaceapi.setup.impl;
	exports me.mrletsplay.webinterfaceapi.sql;

	requires transitive me.mrletsplay.mrcore;
	requires transitive simplehttpserver;
	requires transitive org.slf4j;
	requires transitive org.commonmark;

	requires org.apache.commons.text;
	requires java.sql;
	requires commons.dbcp2;
	requires java.management;

	uses Driver;
}