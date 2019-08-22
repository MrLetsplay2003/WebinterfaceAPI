package me.mrletsplay.webinterfaceapi.webinterface.config.setting;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

public interface AutoSettings {
	
	public default List<WebinterfaceSetting<?>> getSettings() {
		List<WebinterfaceSetting<?>> settings = new ArrayList<>();
		for(Field f : getClass().getDeclaredFields()) {
			if(f.isAnnotationPresent(AutoSetting.class)) {
				if(!Modifier.isStatic(f.getModifiers()) || !WebinterfaceSetting.class.isAssignableFrom(f.getType()))
					throw new IllegalStateException("Invalid @AutoSetting for field: " + f.getName());
				try {
					f.setAccessible(true);
					settings.add((WebinterfaceSetting<?>) f.get(null));
				} catch (IllegalArgumentException | IllegalAccessException e) {
					throw new IllegalStateException("Invalid @AutoSetting for field: " + f.getName(), e);
				}
			}
		}
		return settings;
	}

}
