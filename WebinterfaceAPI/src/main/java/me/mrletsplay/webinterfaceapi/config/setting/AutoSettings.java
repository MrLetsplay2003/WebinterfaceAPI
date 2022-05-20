package me.mrletsplay.webinterfaceapi.config.setting;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public interface AutoSettings {
	
	public default List<Setting<?>> getSettings() {
		return getSettingsCategories().stream()
				.flatMap(c -> c.getSettings().stream())
				.collect(Collectors.toList());
	}
	
	public default List<SettingsCategory> getSettingsCategories() {
		List<SettingsCategory> categories = new ArrayList<>();
		for(Field f : getClass().getDeclaredFields()) {
			if(f.isAnnotationPresent(AutoSetting.class)) {
				if(!Modifier.isStatic(f.getModifiers()))
					throw new IllegalStateException("Invalid @AutoSetting for field: " + f.getName());
				
				try {
					f.setAccessible(true);
					Object v = f.get(null);
					
					if(Setting.class.isAssignableFrom(f.getType())) {
						Setting<?> s = (Setting<?>) v;
						SettingsCategory c = s.getCategory();
						if(!categories.contains(c)) categories.add(c);
						continue;
					}
					
					if(SettingsCategory.class.isAssignableFrom(f.getType())) {
						SettingsCategory c = (SettingsCategory) v;
						if(!categories.contains(c)) categories.add(c);
						continue;
					}
					
					throw new IllegalStateException("Invalid @AutoSetting for field: " + f.getName());
				} catch (IllegalArgumentException | IllegalAccessException e) {
					throw new IllegalStateException("Invalid @AutoSetting for field: " + f.getName(), e);
				}
			}
		}
		return categories;
	}
	
}
