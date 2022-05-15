package me.mrletsplay.webinterfaceapi.bukkit.page;

import java.util.Optional;
import java.util.UUID;

import org.bukkit.Bukkit;

import me.clip.placeholderapi.PlaceholderAPI;
import me.mrletsplay.webinterfaceapi.bukkit.BukkitSettings;
import me.mrletsplay.webinterfaceapi.bukkit.WebinterfacePlugin;
import me.mrletsplay.webinterfaceapi.webinterface.Webinterface;
import me.mrletsplay.webinterfaceapi.webinterface.auth.Account;
import me.mrletsplay.webinterfaceapi.webinterface.auth.AccountConnection;
import me.mrletsplay.webinterfaceapi.webinterface.page.Page;
import me.mrletsplay.webinterfaceapi.webinterface.page.PageSection;
import me.mrletsplay.webinterfaceapi.webinterface.page.action.SendJSAction;
import me.mrletsplay.webinterfaceapi.webinterface.page.element.Group;
import me.mrletsplay.webinterfaceapi.webinterface.page.element.Image;
import me.mrletsplay.webinterfaceapi.webinterface.page.element.InputField;
import me.mrletsplay.webinterfaceapi.webinterface.page.element.Text;
import me.mrletsplay.webinterfaceapi.webinterface.page.element.TitleText;
import me.mrletsplay.webinterfaceapi.webinterface.page.element.layout.DefaultLayoutOption;
import me.mrletsplay.webinterfaceapi.webinterface.page.element.layout.GridLayout;
import me.mrletsplay.webinterfaceapi.webinterface.session.Session;

public class BukkitPage extends Page {

	public BukkitPage() {
		super("Home", "/bukkit/home");

		dynamic(() -> {
			Account acc = Session.getCurrentSession().getAccount();
			AccountConnection mcAcc = acc.getConnection(WebinterfacePlugin.MINECRAFT_ACCOUNT_CONNECTION_NAME);

			if(mcAcc == null) return Optional.empty();

			PageSection yourAccount = new PageSection();
			yourAccount.setSlimLayout(true);
			yourAccount.addLayoutOptions(DefaultLayoutOption.FULL_WIDTH);
			yourAccount.addTitle("Your Profile");

			yourAccount.addElement(Image.builder()
					.src("https://cravatar.eu/helmavatar/" + mcAcc.getUserName() + "/128.png")
					.alt("Avatar")
					.width("128px")
					.height("128px")
					.create());

			Group g = new Group();
			TitleText tt = new TitleText(mcAcc.getUserName());
			tt.getStyle().setProperty("font-size", "26px");
			g.addElement(tt);

			if(Webinterface.getConfig().getSetting(BukkitSettings.SHOW_STATS)) {
				if(!Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
					Webinterface.getLogger().warn("Statistics are enabled on Bukkit Home page but PlaceholderAPI is missing");
				}else {
					Group stats = new Group();
					stats.addLayoutOptions(new GridLayout("auto", "auto"));

					for(String el : Webinterface.getConfig().getSetting(BukkitSettings.STATS_ELEMENTS)) {
						String[] spl = el.split(":");
						if(spl.length != 2) {
							Webinterface.getLogger().warn("Invalid statistics element for Bukkit Home page configured in config.yml: \"" + el + "\"");
							continue;
						}

						stats.addElement(TitleText.builder().text(spl[0]).leftboundText().create());
						stats.addElement(Text.builder().text(PlaceholderAPI.setPlaceholders(Bukkit.getOfflinePlayer(UUID.fromString(mcAcc.getUserID())), spl[1])).leftboundText().create());
					}
					g.addElement(stats);
				}
			}

			yourAccount.addElement(g);
			return Optional.of(yourAccount);
		});

		PageSection sets = new PageSection();
		sets.addTitle("Settings");
		sets.addLayoutOptions(DefaultLayoutOption.FULL_WIDTH);

		sets.dynamic(els -> {
			Account acc = Session.getCurrentSession().getAccount();
			AccountConnection mcAcc = acc.getConnection(WebinterfacePlugin.MINECRAFT_ACCOUNT_CONNECTION_NAME);

			if(mcAcc != null) {
				els.add(Text.builder()
						.text("Connected Minecraft account")
						.create());

				els.add(Text.builder()
						.text(mcAcc.getUserName() + " (" + mcAcc.getUserID() + ")")
						.withLayoutOptions(DefaultLayoutOption.SECOND_TO_LAST_COLUMN)
						.create());
			}else {
				els.add(Text.builder()
						.text("Connect to Minecraft account")
						.create());

				els.add(InputField.builder()
						.placeholder("Minecraft name")
						.onChange(f -> SendJSAction.of("bukkit", "connectMinecraftAccount", f.inputValue()))
						.create());
			}
		});

		addSection(sets);
	}

}
