package me.mrletsplay.webinterfaceapi.util;

import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.Random;

import me.mrletsplay.simplehttpserver.dom.html.HtmlElement;

public class WebinterfaceUtils {

	public static final DateTimeFormatter HTTP_TIMESTAMP = DateTimeFormatter
		.ofPattern("EEE, dd MMM yyyy HH:mm:ss O")
		.withZone(ZoneOffset.UTC);

	private static final Random RANDOM = new Random();

	public static String httpTimeStamp(TemporalAccessor temporal) {
		return HTTP_TIMESTAMP.format(temporal);
	}

	public static String randomID(int length) {
		char[] chars = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
		StringBuilder b = new StringBuilder();
		for(int i = 0; i < length; i++) {
			b.append(chars[RANDOM.nextInt(chars.length)]);
		}
		return b.toString();
	}

	public static HtmlElement iconifyIcon(String icon) {
		HtmlElement iconSpan = new HtmlElement("span");
		iconSpan.addClass("iconify");
		iconSpan.setAttribute("data-icon", icon);
		return iconSpan;
	}

}
