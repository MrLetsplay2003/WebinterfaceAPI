package me.mrletsplay.webinterfaceapi.util;

import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;

public class WebinterfaceUtils {
	
	public static final DateTimeFormatter HTTP_TIMESTAMP = DateTimeFormatter
			.ofPattern("EEE, dd MMM yyyy HH:mm:ss O")
			.withZone(ZoneOffset.UTC);
	
	public static String httpTimeStamp(TemporalAccessor temporal) {
		return HTTP_TIMESTAMP.format(temporal);
	}

}
