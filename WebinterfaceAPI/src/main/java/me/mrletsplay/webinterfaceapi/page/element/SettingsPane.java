package me.mrletsplay.webinterfaceapi.page.element;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import me.mrletsplay.mrcore.misc.Complex;
import me.mrletsplay.mrcore.misc.NullableOptional;
import me.mrletsplay.webinterfaceapi.config.Config;
import me.mrletsplay.webinterfaceapi.config.setting.Setting;
import me.mrletsplay.webinterfaceapi.config.setting.SettingsCategory;
import me.mrletsplay.webinterfaceapi.page.action.Action;
import me.mrletsplay.webinterfaceapi.page.action.ActionEvent;
import me.mrletsplay.webinterfaceapi.page.action.ActionResponse;
import me.mrletsplay.webinterfaceapi.page.action.SendJSAction;
import me.mrletsplay.webinterfaceapi.page.action.ShowToastAction;
import me.mrletsplay.webinterfaceapi.page.action.value.ActionValue;
import me.mrletsplay.webinterfaceapi.page.element.layout.DefaultLayoutOption;
import me.mrletsplay.webinterfaceapi.page.element.layout.GridLayout;
import me.mrletsplay.webinterfaceapi.page.element.list.BasicListAdapter;
import me.mrletsplay.webinterfaceapi.page.element.list.ListAdapter;
import me.mrletsplay.webinterfaceapi.page.element.list.WebinterfaceElementList;

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
			InputField in = new InputField();
			in.setInitialValue(() -> {
				String v = (String) config.get().getSetting(setting);
				return v == null ? "" : v;
			});
			in.setOnChangeAction(changeSettingAction(setting, ActionValue.elementValue(in)));
			el = in;
			defaultValue = ActionValue.string((String) setting.getDefaultValue());
		}else if(setting.getType().equals(Complex.value(Integer.class)) || setting.getType().equals(Complex.value(Double.class))) {
			boolean isInt = setting.getType().equals(Complex.value(Integer.class));
			NumberField in = new NumberField(() -> String.valueOf(config.get().getSetting(setting)));
			in.setInitialValue(() -> {
				String v = String.valueOf(config.get().getSetting(setting));
				return v == null ? "" : v;
			});
			in.setAllowFloats(!isInt);
			in.setOnChangeAction(changeSettingAction(setting, in.inputValue()));
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
//			InputField in = new InputField(() -> ((List<?>) config.get().getSetting(setting)).stream().map(Object::toString).collect(Collectors.joining(", ")));
//			in.setOnChangeAction(changeSettingAction(setting, () -> String.format("%s.split(\",\").map(x=>x.trim())", in.inputValue().toJavaScript())));
//			el = in;
//			defaultValue = ActionValue.array(Complex.list(String.class).cast(setting.getDefaultValue()).get().stream()
//					.map(s -> ActionValue.string(s))
//					.collect(Collectors.toList()));

			@SuppressWarnings("unchecked")
			ListAdapter<String> la = new BasicListAdapter<>(new ArrayList<>((List<String>) config.get().getSetting(setting)), i -> i);

			WebinterfaceElementList<String> list = WebinterfaceElementList.<String>builder()
				.items(la)
				.elementFunction(s -> Text.builder().text(s).leftboundText().create())
				.removable(true)
				.updateHandler("E", "E") // FIXME: Update handler
				.create();

			el = list;
			defaultValue = ActionValue.array(Complex.list(String.class).cast(setting.getDefaultValue()).get().stream()
				.map(s -> ActionValue.string(s))
				.collect(Collectors.toList()));
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

		Button reset = Button.builder()
			.icon("mdi:undo")
			.onClick(changeSettingAction(setting, defaultValue))
			.create();

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
		return SendJSAction.of(requestTarget, requestMethod, ActionValue.object()
				.put("setting", ActionValue.string(setting.getKey()))
				.put("value", value)
			).onSuccess(ShowToastAction.info(ActionValue.string("Changed " + setting.getFriendlyName() + " to ").plus(value)));
	}

	public List<SettingsCategory> getSettingsCategories() {
		return categories;
	}

	public static ActionResponse handleSetSettingRequest(Config config, ActionEvent event) {
		String str = event.getData().optString("setting").orElse(null);
		if(str == null) return ActionResponse.error("Need setting");
		Setting<?> set = config.getSetting(str);
		if(set == null) return ActionResponse.error("Invalid setting");
		SettingsPane.setSetting(config, set, event.getData().get("value"));
		return ActionResponse.success();
	}

	private static <T> void setSetting(Config config, Setting<T> setting, Object value) {
		config.setSetting(setting, setting.getType().cast(value, SettingsPane::jsonCast).get());
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
