package me.mrletsplay.webinterfaceapi.page;

import me.mrletsplay.mrcore.json.JSONArray;
import me.mrletsplay.mrcore.misc.Complex;
import me.mrletsplay.mrcore.misc.NullableOptional;
import me.mrletsplay.webinterfaceapi.config.Config;
import me.mrletsplay.webinterfaceapi.config.setting.Setting;
import me.mrletsplay.webinterfaceapi.page.action.ActionEvent;
import me.mrletsplay.webinterfaceapi.page.action.ActionResponse;
import me.mrletsplay.webinterfaceapi.page.element.SettingsPane;

public class SettingsPage extends Page {
	
	public SettingsPage(String name, String url, String permission, boolean hidden, SettingsPane settingsPane) {
		super(name, url, permission, hidden);
		
		PageSection sc2 = new PageSection();
		sc2.setSlimLayout(true);
		sc2.addTitle("Settings");
		sc2.addElement(settingsPane);
		
		addSection(sc2);
	}
	
	public SettingsPage(String name, String url, String permission, SettingsPane settingsPane) {
		this(name, url, permission, false, settingsPane);
	}
	
	public SettingsPage(String name, String url, boolean hidden, SettingsPane settingsPane) {
		this(name, url, null, hidden, settingsPane);
	}
	
	public SettingsPage(String name, String url, SettingsPane settingsPane) {
		this(name, url, null, settingsPane);
	}
	
	public static ActionResponse handleSetSettingRequest(Config config, ActionEvent event) {
		JSONArray keyAndValue = event.getRequestData().getJSONArray("value");
		Setting<?> set = config.getSetting(keyAndValue.getString(0));
		SettingsPage.setSetting(config, set, keyAndValue.get(1));
		return ActionResponse.success();
	}
	
	private static <T> void setSetting(Config config, Setting<T> setting, Object value) {
		config.setSetting(setting, setting.getType().cast(value, SettingsPage::jsonCast).get());
	}
	
	private static <T> NullableOptional<T> jsonCast(Object o, Class<T> typeClass, Complex<?> exactClass) {
		if(o == null) return NullableOptional.of(null);
		if(typeClass.isInstance(o)) return NullableOptional.of(typeClass.cast(o));
		if(Number.class.isAssignableFrom(typeClass)) {
			if(!(o instanceof Number)) return NullableOptional.empty();
			Number n = (Number) o;
			if(typeClass.equals(Integer.class)) {
				return NullableOptional.of(typeClass.cast(n.intValue()));
			}else if(typeClass.equals(Double.class)) {
				return NullableOptional.of(typeClass.cast(n.doubleValue()));
			}
		}
		return NullableOptional.empty();
	}
	
}
