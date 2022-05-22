package me.mrletsplay.webinterfaceapi.page;

import me.mrletsplay.webinterfaceapi.config.Config;
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

	/**
	 * An alias for {@link SettingsPane#handleSetSettingRequest(Config, ActionEvent)}
	 * @param config
	 * @param event
	 * @return
	 */
	public static ActionResponse handleSetSettingRequest(Config config, ActionEvent event) {
		return SettingsPane.handleSetSettingRequest(config, event);
	}

}
