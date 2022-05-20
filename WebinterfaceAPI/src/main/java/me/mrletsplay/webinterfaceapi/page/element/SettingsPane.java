package me.mrletsplay.webinterfaceapi.page.element;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import me.mrletsplay.mrcore.json.JSONArray;
import me.mrletsplay.mrcore.misc.Complex;
import me.mrletsplay.mrcore.misc.NullableOptional;
import me.mrletsplay.webinterfaceapi.config.Config;
import me.mrletsplay.webinterfaceapi.config.setting.Setting;
import me.mrletsplay.webinterfaceapi.config.setting.SettingsCategory;
import me.mrletsplay.webinterfaceapi.page.SettingsPage;
import me.mrletsplay.webinterfaceapi.page.action.Action;
import me.mrletsplay.webinterfaceapi.page.action.ActionEvent;
import me.mrletsplay.webinterfaceapi.page.action.ActionResponse;
import me.mrletsplay.webinterfaceapi.page.action.ReloadPageAction;
import me.mrletsplay.webinterfaceapi.page.action.SendJSAction;
import me.mrletsplay.webinterfaceapi.page.action.value.ActionValue;
import me.mrletsplay.webinterfaceapi.page.element.layout.DefaultLayoutOption;
import me.mrletsplay.webinterfaceapi.page.element.layout.GridLayout;

public class SettingsPane extends Group {

	private Supplier<Config> config;
	private List<SettingsCategory> categories;
	private String
		requestTarget,
		requestMethod;

	public SettingsPane(Supplier<Config> config, List<SettingsCategory> settings, String requestTarget, String requestMethod) {
		this.config = config;
		this.categories = new ArrayList<>();
		this.requestTarget = requestTarget;
		this.requestMethod = requestMethod;

		addLayoutOptions(DefaultLayoutOption.FULL_WIDTH);
		getStyle().setProperty("grid-template-columns", "1fr");

		addSettings(settings);
	}

	public SettingsPane(Config config, List<SettingsCategory> settings, String requestTarget, String requestMethod) {
		this(() -> config, settings, requestTarget, requestMethod);
	}

	public void addSettings(List<SettingsCategory> categories) {
		categories.forEach(c -> {
			Heading h = new Heading(c.getName());
			h.setLevel(3);
			h.addLayoutOptions(DefaultLayoutOption.LEFTBOUND_TEXT);
			addElement(h);
			c.getSettings().forEach(this::addSetting);
			addElement(new VerticalSpacer("30px"));
		});
	}

	private void addSetting(Setting<?> setting) {
		PageElement el = null;
		ActionValue defaultValue = null;

		boolean oneLineLayout = false;

		if(setting.getType().equals(Complex.value(String.class))) {
			InputField in = new InputField(() -> {
				String v = (String) config.get().getSetting(setting);
				return v == null ? "(none)" : v;
			});
			in.setOnChangeAction(changeSettingAction(setting, ActionValue.elementValue(in)));
			el = in;
			defaultValue = ActionValue.string((String) setting.getDefaultValue());
		}else if(setting.getType().equals(Complex.value(Integer.class)) || setting.getType().equals(Complex.value(Double.class))) {
			InputField in = new InputField(() -> config.get().getSetting(setting).toString());
			in.setOnChangeAction(changeSettingAction(setting, () -> String.format(setting.getType().equals(Complex.value(Integer.class)) ? "parseInt(%s)" : "parseFloat(%s)", in.inputValue().toJavaScript())));
			el = in;
			defaultValue = () -> setting.getDefaultValue().toString();
		}else if(setting.getType().equals(Complex.value(Boolean.class))) {
			CheckBox in = new CheckBox(() -> (Boolean) config.get().getSetting(setting));
			in.addLayoutOptions(DefaultLayoutOption.LEFTBOUND_TEXT);
			in.setOnChangeAction(changeSettingAction(setting, in.checkedValue()));
			el = in;
			defaultValue = () -> setting.getDefaultValue().toString();
			oneLineLayout = true;
		}else if(setting.getType().equals(Complex.list(String.class))) {
			InputField in = new InputField(() -> ((List<?>) config.get().getSetting(setting)).stream().map(Object::toString).collect(Collectors.joining(", ")));
			in.setOnChangeAction(changeSettingAction(setting, () -> String.format("%s.split(\",\").map(x=>x.trim())", in.inputValue().toJavaScript())));
			el = in;
			defaultValue = ActionValue.array(Complex.list(String.class).cast(setting.getDefaultValue()).get().stream()
					.map(s -> ActionValue.string(s))
					.collect(Collectors.toList()));

//			@SuppressWarnings("unchecked")
//			ListAdapter<String> la = new BasicListAdapter<>(new ArrayList<>((List<String>) config.get().getSetting(setting)), i -> i);
//
//			WebinterfaceElementList<String> list = WebinterfaceElementList.<String>builder()
//					.items(la)
//					.elementFunction(s -> WebinterfaceText.builder().text(s).create())
//					.removable(true)
//					.create();
//
//			el = list;
//			defaultValue = new ArrayValue(Complex.list(String.class).cast(setting.getDefaultValue()).get().stream()
//					.map(s -> new StringValue(s))
//					.collect(Collectors.toList()));
		}else if(setting.getType().equals(Complex.list(Integer.class)) || setting.getType().equals(Complex.list(Double.class))) {
			InputField in = new InputField(() -> ((List<?>) config.get().getSetting(setting)).stream().map(Object::toString).collect(Collectors.joining(", ")));
			in.setOnChangeAction(changeSettingAction(setting, () -> String.format("%s.split(\",\").map(x=>x.trim()).map(" + (setting.getType().equals(Complex.list(Integer.class)) ? "x=>parseInt(x)" : "x=>parseFloat(x)") + ")", in.inputValue().toJavaScript())));
			el = in;
			defaultValue = ActionValue.array(((List<?>) setting.getDefaultValue()).stream()
					.map(s -> (ActionValue) () -> s.toString())
					.collect(Collectors.toList()));
		}else if(setting.getType().equals(Complex.list(Boolean.class))) {
			InputField in = new InputField(() -> ((List<?>) config.get().getSetting(setting)).stream().map(Object::toString).collect(Collectors.joining(", ")));
			in.setOnChangeAction(changeSettingAction(setting, () -> String.format("%s.split(\",\").map(x=>x.trim()).map(x=>x==\"true\")", in.inputValue().toJavaScript())));
			el = in;
			defaultValue = ActionValue.array(((List<?>) setting.getDefaultValue()).stream()
					.map(s -> (ActionValue) () -> s.toString())
					.collect(Collectors.toList()));
		}

		if(el == null || defaultValue == null) return;
		if(!oneLineLayout) el.addLayoutOptions(DefaultLayoutOption.NEW_LINE);

		Text t = new Text(setting.getFriendlyName() != null ? setting.getFriendlyName() : setting.getKey());
		t.addLayoutOptions(DefaultLayoutOption.LEFTBOUND_TEXT);
		if(!oneLineLayout) t.addLayoutOptions(DefaultLayoutOption.NEW_LINE);

		PageElement tEl = t;
		if(setting.getDescription() != null) {
			Group tGrp = new Group();
			tGrp.addLayoutOptions(new GridLayout("1fr"));
			tGrp.addElement(t);
			Text tDesc = new Text(setting.getDescription());
			tDesc.getStyle().setProperty("font-size", "0.8em");
			tDesc.getStyle().setProperty("color", "var(--theme-color-content-text-secondary)");
			tDesc.addLayoutOptions(DefaultLayoutOption.LEFTBOUND_TEXT);
			tGrp.addElement(tDesc);
			tEl = tGrp;
		}

		Button reset = new Button("X");
		reset.setOnClickAction(changeSettingAction(setting, defaultValue));

		Group grp = new Group();
		grp.getStyle().setProperty("grid-template-columns", oneLineLayout ? "min-content auto" : "auto 50px");
		grp.getMobileStyle().setProperty("grid-template-columns", oneLineLayout ? "min-content auto" : "auto 50px");

		if(oneLineLayout) {
			// No reset button, since this currently only exists for boolean settings
			grp.addElement(el);
			grp.addElement(tEl);
		}else {
			grp.addElement(tEl);
			grp.addElement(el);
			grp.addElement(reset);
		}

		addElement(grp);
	}

	private Action changeSettingAction(Setting<?> setting, ActionValue value) {
		return SendJSAction.of(requestTarget, requestMethod, ActionValue.array(
				ActionValue.string(setting.getKey()),
				value
			)).onSuccess(ReloadPageAction.delayed(100));
	}

	public List<SettingsCategory> getSettingsCategories() {
		return categories;
	}

	/**
	 * @deprecated Use {@link SettingsPage#handleSetSettingRequest(Config, ActionEvent)} instead
	 * @param config
	 * @param event
	 * @return
	 */
	@Deprecated
	public static ActionResponse handleSetSettingRequest(Config config, ActionEvent event) {
		JSONArray keyAndValue = event.getRequestData().getJSONArray("value");
		Setting<?> set = config.getSetting(keyAndValue.getString(0));
		SettingsPane.setSetting(config, set, keyAndValue.get(1));
		return ActionResponse.success();
	}

	@Deprecated
	private static <T> void setSetting(Config config, Setting<T> setting, Object value) {
		config.setSetting(setting, setting.getType().cast(value, SettingsPane::jsonCast).get());
	}

	@Deprecated
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
