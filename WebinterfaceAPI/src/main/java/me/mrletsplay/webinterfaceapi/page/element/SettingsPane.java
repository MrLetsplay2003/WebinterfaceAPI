package me.mrletsplay.webinterfaceapi.page.element;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import me.mrletsplay.mrcore.json.JSONArray;
import me.mrletsplay.mrcore.misc.Complex;
import me.mrletsplay.mrcore.misc.NullableOptional;
import me.mrletsplay.webinterfaceapi.config.Config;
import me.mrletsplay.webinterfaceapi.config.setting.Setting;
import me.mrletsplay.webinterfaceapi.config.setting.SettingsCategory;
import me.mrletsplay.webinterfaceapi.config.setting.impl.BooleanListSetting;
import me.mrletsplay.webinterfaceapi.config.setting.impl.BooleanSetting;
import me.mrletsplay.webinterfaceapi.config.setting.impl.DoubleListSetting;
import me.mrletsplay.webinterfaceapi.config.setting.impl.DoubleSetting;
import me.mrletsplay.webinterfaceapi.config.setting.impl.IntListSetting;
import me.mrletsplay.webinterfaceapi.config.setting.impl.IntSetting;
import me.mrletsplay.webinterfaceapi.config.setting.impl.StringListSetting;
import me.mrletsplay.webinterfaceapi.config.setting.impl.StringSetting;
import me.mrletsplay.webinterfaceapi.page.action.Action;
import me.mrletsplay.webinterfaceapi.page.action.ActionEvent;
import me.mrletsplay.webinterfaceapi.page.action.ActionResponse;
import me.mrletsplay.webinterfaceapi.page.action.AddValueAction;
import me.mrletsplay.webinterfaceapi.page.action.MultiAction;
import me.mrletsplay.webinterfaceapi.page.action.SendJSAction;
import me.mrletsplay.webinterfaceapi.page.action.SetValueAction;
import me.mrletsplay.webinterfaceapi.page.action.ShowToastAction;
import me.mrletsplay.webinterfaceapi.page.action.ValidateElementsAction;
import me.mrletsplay.webinterfaceapi.page.action.value.ActionValue;
import me.mrletsplay.webinterfaceapi.page.element.Select.Option;
import me.mrletsplay.webinterfaceapi.page.element.layout.DefaultLayoutOption;
import me.mrletsplay.webinterfaceapi.page.element.layout.Grid;
import me.mrletsplay.webinterfaceapi.page.element.list.DoubleList;
import me.mrletsplay.webinterfaceapi.page.element.list.ElementList;
import me.mrletsplay.webinterfaceapi.page.element.list.IntegerList;
import me.mrletsplay.webinterfaceapi.page.element.list.StringList;

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
		Action resetAction = null;

		boolean oneLineLayout = false;

		if(setting instanceof StringSetting) {
			StringSetting s = (StringSetting) setting;
			defaultValue = ActionValue.string((String) setting.getDefaultValue());

			if(s.getAllowedValues() == null) {
				if(!s.isPassword()) {
					InputField in = new InputField();
					in.setInitialValue(() -> {
						String v = config.get().getSetting(s);
						return v == null ? "" : v;
					});
					in.setPlaceholder(s.getFriendlyName());
					in.setOnChangeAction(changeSettingAction(setting, ActionValue.elementValue(in)));
					el = in;
					resetAction = SetValueAction.of(in, defaultValue).triggerUpdate(false);
				}else {
					PasswordField in = new PasswordField();
					in.setShowInitialValue(() -> {
						String v = config.get().getSetting(s);
						return v != null;
					});
					in.setPlaceholder(s.getFriendlyName());
					in.setOnChangeAction(changeSettingAction(setting, ActionValue.elementValue(in), true));
					el = in;
					resetAction = SetValueAction.of(in, defaultValue).triggerUpdate(false);
				}
			}else {
				Select sel = new Select();
				sel.setOptions(() -> {
					List<Option> ops = new ArrayList<>();
					for(String str : s.getAllowedValues()) {
						ops.add(new Option(str, str, Objects.equals(config.get().getSetting(s), str), true));
					}
					return ops;
				});
				sel.setOnChangeAction(changeSettingAction(setting, ActionValue.elementValue(sel)));
				el = sel;
				resetAction = SetValueAction.of(sel, defaultValue).triggerUpdate(false);
			}
		}else if(setting instanceof IntSetting) {
			IntSetting s = (IntSetting) setting;
			defaultValue = () -> setting.getDefaultValue().toString();

			if(s.getAllowedValues() == null) {
				NumberField in = new NumberField(() -> String.valueOf(config.get().getSetting(setting)));
				in.setInitialValue(() -> {
					Integer v = config.get().getSetting(s);
					return v == null ? null : v.doubleValue();
				});
				in.setPlaceholder(s.getFriendlyName());

				if(s.getMin() != null) in.setMin(s.getMin().doubleValue());
				if(s.getMax() != null) in.setMax(s.getMax().doubleValue());

				in.setOnChangeAction(ValidateElementsAction.of(in).onSuccess(changeSettingAction(setting, in.inputValue())));
				el = in;
				resetAction = SetValueAction.of(in, defaultValue).triggerUpdate(false);
			}else {
				Select sel = new Select();
				sel.setOptions(() -> {
					List<Option> ops = new ArrayList<>();
					for(Integer val : s.getAllowedValues()) {
						ops.add(new Option(String.valueOf(val), String.valueOf(val), Objects.equals(config.get().getSetting(s), val), true));
					}
					return ops;
				});
				sel.setOnChangeAction(changeSettingAction(setting, ActionValue.elementValue(sel).asInt()));
				el = sel;
				resetAction = SetValueAction.of(sel, defaultValue).triggerUpdate(false);
			}
		}else if(setting instanceof DoubleSetting) {
			DoubleSetting s = (DoubleSetting) setting;

			NumberField in = new NumberField(() -> String.valueOf(config.get().getSetting(setting)));
			in.setInitialValue(() -> config.get().getSetting(s));
			in.setPlaceholder(s.getFriendlyName());

			if(s.getMin() != null) in.setMin(s.getMin());
			if(s.getMax() != null) in.setMax(s.getMax());

			in.setAllowedDecimals(2);
			in.setOnChangeAction(ValidateElementsAction.of(in).onSuccess(changeSettingAction(setting, in.inputValue())));
			el = in;
			defaultValue = () -> setting.getDefaultValue().toString();

			resetAction = SetValueAction.of(in, defaultValue).triggerUpdate(false);
		}else if(setting instanceof BooleanSetting) {
			CheckBox in = new CheckBox(() -> (Boolean) config.get().getSetting(setting));
			in.addLayoutOptions(DefaultLayoutOption.LEFTBOUND_TEXT);
			in.setOnChangeAction(changeSettingAction(setting, in.checkedValue()));
			el = in;
			defaultValue = () -> setting.getDefaultValue().toString();
			oneLineLayout = true;

			resetAction = SetValueAction.of(in, defaultValue);
		}else if(setting instanceof StringListSetting) {
			StringListSetting s = (StringListSetting) setting;

			Group grp = new Group();

			ElementList<?> list = StringList.builder()
				.initialItems(() -> config.get().getSetting(s))
				.removable(true)
				.onChange(l -> changeSettingAction(setting, l.itemsValue()))
				.fullWidth()
				.create();

			grp.addElement(list);

			InputField field = InputField.builder()
				.withLayoutOptions(DefaultLayoutOption.FULL_NOT_LAST_COLUMN)
				.placeholder("Add value")
				.create();
			grp.addElement(field);

			Button btn = Button.builder()
				.text("Add")
				.onClick(MultiAction.of(AddValueAction.of(list, field.inputValue()), SetValueAction.of(field, ActionValue.nullValue())))
				.create();
			grp.addElement(btn);

			defaultValue = ActionValue.array(s.getDefaultValue().stream()
				.map(str -> ActionValue.string(str))
				.collect(Collectors.toList()));

			el = grp;

			resetAction = SetValueAction.of(list, defaultValue).triggerUpdate(false);
		}else if(setting instanceof IntListSetting) {
			IntListSetting s = (IntListSetting) setting;

			Group grp = new Group();

			ElementList<?> list = IntegerList.builder()
				.initialItems(() -> config.get().getSetting(s))
				.removable(true)
				.onChange(l -> changeSettingAction(setting, l.itemsValue()))
				.fullWidth()
				.create();

			grp.addElement(list);

			NumberField field = NumberField.builder()
				.withLayoutOptions(DefaultLayoutOption.FULL_NOT_LAST_COLUMN)
				.placeholder("Add value")
				.create();
			grp.addElement(field);

			Button btn = Button.builder()
				.text("Add")
				.onClick(MultiAction.of(AddValueAction.of(list, field.inputValue()), SetValueAction.of(field, ActionValue.nullValue())))
				.create();
			grp.addElement(btn);

			defaultValue = ActionValue.array(s.getDefaultValue().stream()
				.map(i -> ActionValue.integer(i))
				.collect(Collectors.toList()));

			el = grp;

			resetAction = SetValueAction.of(list, defaultValue).triggerUpdate(false);
		}else if(setting instanceof DoubleListSetting) {
			DoubleListSetting s = (DoubleListSetting) setting;

			Group grp = new Group();

			DoubleList list = DoubleList.builder()
				.initialItems(() -> config.get().getSetting(s))
				.removable(true)
				.onChange(l -> changeSettingAction(setting, l.itemsValue()))
				.fullWidth()
				.create();

			grp.addElement(list);

			NumberField field = NumberField.builder()
				.withLayoutOptions(DefaultLayoutOption.FULL_NOT_LAST_COLUMN)
				.allowedDecimals(2)
				.placeholder("Add value")
				.create();
			grp.addElement(field);

			Button btn = Button.builder()
				.text("Add")
				.onClick(MultiAction.of(AddValueAction.of(list, field.inputValue()), SetValueAction.of(field, ActionValue.nullValue())))
				.create();
			grp.addElement(btn);

			defaultValue = ActionValue.array(s.getDefaultValue().stream()
				.map(d -> ActionValue.decimal(d))
				.collect(Collectors.toList()));

			el = grp;

			resetAction = SetValueAction.of(list, defaultValue).triggerUpdate(false);
		}else if(setting instanceof BooleanListSetting) {
			InputField in = new InputField(() -> ((List<?>) config.get().getSetting(setting)).stream().map(Object::toString).collect(Collectors.joining(", ")));
			in.setOnChangeAction(changeSettingAction(setting, () -> String.format("%s.split(\",\").map(x=>x.trim()).map(x=>x==\"true\")", in.inputValue().toJavaScript())));
			el = in;

			defaultValue = ActionValue.array(((List<?>) setting.getDefaultValue()).stream()
				.map(s -> ActionValue.bool((boolean) s))
				.collect(Collectors.toList()));

			resetAction = SetValueAction.of(in, () -> ((List<?>)setting.getDefaultValue()).stream().map(s -> s.toString()).collect(Collectors.joining(","))).triggerUpdate(false);
		}else {
			throw new UnsupportedOperationException("Unsupported setting type: " + setting.getClass().getName());
		}

		if(el == null || defaultValue == null) return;
		if(!oneLineLayout) el.addLayoutOptions(DefaultLayoutOption.NEW_LINE);

		Text t = new Text(setting.getFriendlyName() != null ? setting.getFriendlyName() : setting.getKey());
		t.addLayoutOptions(DefaultLayoutOption.LEFTBOUND_TEXT);
		if(!oneLineLayout) t.addLayoutOptions(DefaultLayoutOption.NEW_LINE);

		PageElement tEl = t;
		if(setting.getDescription() != null) {
			Group tGrp = new Group();
			tGrp.setGrid(new Grid().setColumns("1fr"));
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
			.onClick(MultiAction.of(resetAction, changeSettingAction(setting, defaultValue)))
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

	private Action changeSettingAction(Setting<?> setting, ActionValue value, boolean hide) {
		return SendJSAction.of(requestTarget, requestMethod, ActionValue.object()
				.put("setting", ActionValue.string(setting.getKey()))
				.put("value", value)
			).onSuccess(ShowToastAction.info(ActionValue.string("Changed " + setting.getFriendlyName() + " to ").plus(hide ? ActionValue.string("***") : value)));
	}

	private Action changeSettingAction(Setting<?> setting, ActionValue value) {
		return changeSettingAction(setting, value, false);
	}

	public List<SettingsCategory> getSettingsCategories() {
		return categories;
	}

	public static ActionResponse handleSetSettingRequest(Config config, ActionEvent event) {
		String str = event.getData().optString("setting").orElse(null);
		if(str == null) return ActionResponse.error("Need setting");
		Setting<?> set = config.getSetting(str);
		if(set == null) return ActionResponse.error("Invalid setting");
		String err = setSetting(config, set, event.getData().get("value"));
		if(err != null) return ActionResponse.error(err);
		return ActionResponse.success();
	}

	private static <T> String setSetting(Config config, Setting<T> setting, Object value) {
		NullableOptional<T> opt = setting.getType().cast(value, SettingsPane::jsonCast);
		if(!opt.isPresent()) return "Invalid setting type";
		T t = opt.get();
		if(setting.getAllowedValues() != null && !setting.getAllowedValues().contains(t)) return "Invalid value";
		config.setSetting(setting, t);
		return null;
	}

	private static <T> NullableOptional<T> jsonCast(Object o, Class<T> typeClass, Complex<?> exactClass) {
		if(o == null) return NullableOptional.of(null);
		if(typeClass.isInstance(o)) return NullableOptional.of(typeClass.cast(o));

		if(typeClass == List.class && o instanceof JSONArray) {
			return NullableOptional.of(typeClass.cast(((JSONArray) o).toList()));
		}

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
