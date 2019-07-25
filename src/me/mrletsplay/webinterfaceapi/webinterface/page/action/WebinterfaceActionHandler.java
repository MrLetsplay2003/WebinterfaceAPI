package me.mrletsplay.webinterfaceapi.webinterface.page.action;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;

public interface WebinterfaceActionHandler {

	public default WebinterfaceResponse handle(WebinterfaceRequestEvent event) {
		for(Method m : getClass().getDeclaredMethods()) {
			if(m.isAnnotationPresent(WebinterfaceHandler.class)) {
				WebinterfaceHandler wh = m.getAnnotation(WebinterfaceHandler.class);
				if(wh.requestTarget().equalsIgnoreCase(event.getRequestTarget()) && Arrays.binarySearch(wh.requestTypes(), event.getRequestMethod()) >= 0) {
					if(!Arrays.equals(m.getParameterTypes(), new Class[] {WebinterfaceRequestEvent.class}) ||
							!m.getReturnType().equals(WebinterfaceResponse.class)) {
						throw new UnsupportedOperationException("Illegal request handler: " + m);
					}
					try {
						WebinterfaceResponse response = (WebinterfaceResponse) m.invoke(this, event);
						return response;
					}catch(InvocationTargetException | IllegalAccessException | IllegalArgumentException e2) {
						e2.printStackTrace();
						return WebinterfaceResponse.error("Malformed request");
					}
				}
			}
		}
		return null;
	}
	
}
