package me.mrletsplay.webinterfaceapi.page.action;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import me.mrletsplay.webinterfaceapi.Webinterface;

public interface ActionHandler {

	public default ActionResponse handle(ActionEvent event) {
		for(Method m : getClass().getDeclaredMethods()) {
			if(m.isAnnotationPresent(WebinterfaceHandler.class)) {
				WebinterfaceHandler wh = m.getAnnotation(WebinterfaceHandler.class);
				if(wh.requestTarget().equalsIgnoreCase(event.getRequestTarget()) && Arrays.binarySearch(wh.requestTypes(), event.getRequestMethod()) >= 0) {
					if(!Arrays.equals(m.getParameterTypes(), new Class[] {ActionEvent.class})
						|| !m.getReturnType().equals(ActionResponse.class)) {
						throw new UnsupportedOperationException("Illegal request handler: " + m);
					}

					if(!wh.permission().isEmpty() && !event.getAccount().hasPermission(wh.permission())) {
						return ActionResponse.error("No permission");
					}

					try {
						ActionResponse response = (ActionResponse) m.invoke(this, event);
						return response;
					}catch(InvocationTargetException | IllegalAccessException | IllegalArgumentException e2) {
						e2.printStackTrace();
						Webinterface.getLogger().debug("Malformed request", e2);
						return ActionResponse.error("Malformed request");
					}
				}
			}
		}
		return null;
	}

	public default Set<String> getPermissions() {
		Set<String> perms = new HashSet<>();
		for(Method m : getClass().getDeclaredMethods()) {
			if(m.isAnnotationPresent(WebinterfaceHandler.class)) {
				WebinterfaceHandler wh = m.getAnnotation(WebinterfaceHandler.class);
				String perm = wh.permission();
				if(!perm.isEmpty()) perms.add(perm);
			}
		}
		return perms;
	}

}
