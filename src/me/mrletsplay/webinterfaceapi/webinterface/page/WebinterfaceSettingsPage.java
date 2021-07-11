package me.mrletsplay.webinterfaceapi.webinterface.page;

import me.mrletsplay.mrcore.json.JSONArray;
import me.mrletsplay.mrcore.misc.Complex;
import me.mrletsplay.mrcore.misc.NullableOptional;
import me.mrletsplay.webinterfaceapi.webinterface.config.WebinterfaceConfig;
import me.mrletsplay.webinterfaceapi.webinterface.config.setting.WebinterfaceSetting;
import me.mrletsplay.webinterfaceapi.webinterface.page.action.WebinterfaceRequestEvent;
import me.mrletsplay.webinterfaceapi.webinterface.page.action.WebinterfaceResponse;
import me.mrletsplay.webinterfaceapi.webinterface.page.element.WebinterfaceSettingsPane;

public class WebinterfaceSettingsPage extends WebinterfacePage {
	
	public WebinterfaceSettingsPage(String name, String url, String permission, boolean hidden, WebinterfaceSettingsPane settingsPane) {
		super(name, url, permission, hidden);
		getContainerStyle().setProperty("max-width", "900px");
		
		WebinterfacePageSection sc2 = new WebinterfacePageSection();
		sc2.addTitle("Settings");
		sc2.addElement(settingsPane);
		
		addSection(sc2);
	}
	
	public WebinterfaceSettingsPage(String name, String url, String permission, WebinterfaceSettingsPane settingsPane) {
		this(name, url, permission, false, settingsPane);
	}
	
	public WebinterfaceSettingsPage(String name, String url, boolean hidden, WebinterfaceSettingsPane settingsPane) {
		this(name, url, null, hidden, settingsPane);
	}
	
	public WebinterfaceSettingsPage(String name, String url, WebinterfaceSettingsPane settingsPane) {
		this(name, url, null, settingsPane);
	}
	
	public static WebinterfaceResponse handleSetSettingRequest(WebinterfaceConfig config, WebinterfaceRequestEvent event) {
		JSONArray keyAndValue = event.getRequestData().getJSONArray("value");
		WebinterfaceSetting<?> set = config.getSetting(keyAndValue.getString(0));
		WebinterfaceSettingsPage.setSetting(config, set, keyAndValue.get(1));
		return WebinterfaceResponse.success();
	}
	
	private static <T> void setSetting(WebinterfaceConfig config, WebinterfaceSetting<T> setting, Object value) {
		config.setSetting(setting, setting.getType().cast(value, WebinterfaceSettingsPage::jsonCast).get());
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
