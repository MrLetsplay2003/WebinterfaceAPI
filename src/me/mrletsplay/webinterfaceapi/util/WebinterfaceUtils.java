package me.mrletsplay.webinterfaceapi.util;

import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.Random;

public class WebinterfaceUtils {
	
	public static final DateTimeFormatter HTTP_TIMESTAMP = DateTimeFormatter
			.ofPattern("EEE, dd MMM yyyy HH:mm:ss O")
			.withZone(ZoneOffset.UTC);
	
	public static String httpTimeStamp(TemporalAccessor temporal) {
		return HTTP_TIMESTAMP.format(temporal);
	}
	
	public static String randomID(int length) {
		char[] chars = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
		Random r = new Random();
		StringBuilder b = new StringBuilder();
		for(int i = 0; i < length; i++) {
			b.append(chars[r.nextInt(chars.length)]);
		}
		return b.toString();
	}

}
