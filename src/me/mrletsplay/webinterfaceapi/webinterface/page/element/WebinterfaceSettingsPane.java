package me.mrletsplay.webinterfaceapi.webinterface.page.element;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import me.mrletsplay.mrcore.misc.Complex;
import me.mrletsplay.webinterfaceapi.webinterface.Webinterface;
import me.mrletsplay.webinterfaceapi.webinterface.config.setting.WebinterfaceSetting;
import me.mrletsplay.webinterfaceapi.webinterface.page.action.MultiAction;
import me.mrletsplay.webinterfaceapi.webinterface.page.action.ReloadPageAction;
import me.mrletsplay.webinterfaceapi.webinterface.page.action.SendJSAction;
import me.mrletsplay.webinterfaceapi.webinterface.page.action.value.ArrayValue;
import me.mrletsplay.webinterfaceapi.webinterface.page.action.value.CheckboxValue;
import me.mrletsplay.webinterfaceapi.webinterface.page.action.value.ElementValue;
import me.mrletsplay.webinterfaceapi.webinterface.page.action.value.StringValue;
import me.mrletsplay.webinterfaceapi.webinterface.page.action.value.WebinterfaceActionValue;
import me.mrletsplay.webinterfaceapi.webinterface.page.action.value.WrapperValue;

public class WebinterfaceSettingsPane extends WebinterfaceElementGroup {
	
	private List<WebinterfaceSetting<?>> settings;
	
	public WebinterfaceSettingsPane(List<WebinterfaceSetting<?>> settings) {
		this.settings = new ArrayList<>();
		addLayouts(ElementLayout.FULL_WIDTH);
		addSettings(settings);
	}
	
	public void addSettings(List<WebinterfaceSetting<?>> settings) {
		List<WebinterfaceSetting<?>> sL = new ArrayList<>(settings);
		Collections.sort(sL, Comparator.comparing(WebinterfaceSetting::getKey));
		sL.forEach(this::addSetting);
	}
	
	public void addSetting(WebinterfaceSetting<?> setting) {
		settings.add(setting);
		WebinterfaceText t = new WebinterfaceText(setting.getKey());
		t.addLayouts(ElementLayout.CENTER_VERTICALLY);
		
		WebinterfacePageElement el = null;
		
		if(setting.getType().equals(Complex.value(String.class))) {
			WebinterfaceInputField in = new WebinterfaceInputField(() -> Webinterface.getConfig().getSetting(setting).toString());
			in.setOnChangeAction(changeSettingAction(setting, new ElementValue(in)));
			el = in;
		}else if(setting.getType().equals(Complex.value(Integer.class)) || setting.getType().equals(Complex.value(Double.class))) {
			WebinterfaceInputField in = new WebinterfaceInputField(() -> Webinterface.getConfig().getSetting(setting).toString());
			in.setOnChangeAction(changeSettingAction(setting, new WrapperValue(new ElementValue(in), setting.getType().equals(Complex.value(Integer.class)) ? "parseInt(%s)" : "parseFloat(%s)")));
			el = in;
		}else if(setting.getType().equals(Complex.value(Boolean.class))) {
			WebinterfaceCheckBox in = new WebinterfaceCheckBox(() -> (Boolean) Webinterface.getConfig().getSetting(setting));
			in.setOnChangeAction(changeSettingAction(setting, new CheckboxValue(in)));
			el = in;
		}else if(setting.getType().equals(Complex.list(String.class))) {
			WebinterfaceInputField in = new WebinterfaceInputField(() -> ((List<?>) Webinterface.getConfig().getSetting(setting)).stream().map(Object::toString).collect(Collectors.joining(", ")));
			in.setOnChangeAction(changeSettingAction(setting, new WrapperValue(new ElementValue(in), "%s.split(\",\").map(x=>x.trim())")));
			el = in;
		}else if(setting.getType().equals(Complex.list(Integer.class)) || setting.getType().equals(Complex.list(Double.class))) {
			WebinterfaceInputField in = new WebinterfaceInputField(() -> ((List<?>) Webinterface.getConfig().getSetting(setting)).stream().map(Object::toString).collect(Collectors.joining(", ")));
			in.setOnChangeAction(changeSettingAction(setting, new WrapperValue(new ElementValue(in), "%s.split(\",\").map(x=>x.trim()).map(" + (setting.getType().equals(Complex.list(Integer.class)) ? "x=>parseInt(x)" : "x=>parseFloat(x)") + ")")));
			el = in;
		}else if(setting.getType().equals(Complex.list(Boolean.class))) {
			WebinterfaceInputField in = new WebinterfaceInputField(() -> ((List<?>) Webinterface.getConfig().getSetting(setting)).stream().map(Object::toString).collect(Collectors.joining(", ")));
			in.setOnChangeAction(changeSettingAction(setting, new WrapperValue(new ElementValue(in), "%s.split(\",\").map(x=>x.trim()).map(x=>x==\"true\")")));
			el = in;
		}
		
		if(el == null) return;
		el.addLayouts(ElementLayout.SECOND_TO_LAST_COLUMN);
		addElement(t);
		addElement(el);
	}
	
	private MultiAction changeSettingAction(WebinterfaceSetting<?> setting, WebinterfaceActionValue value) {
		return new MultiAction(new SendJSAction("webinterface", "setSetting", new ArrayValue(
				new StringValue(setting.getKey()),
				value
			)),
			new ReloadPageAction());
	}
	
	public List<WebinterfaceSetting<?>> getSettings() {
		return settings;
	}
	
}
