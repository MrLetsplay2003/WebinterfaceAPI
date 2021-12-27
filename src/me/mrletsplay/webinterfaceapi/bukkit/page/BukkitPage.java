package me.mrletsplay.webinterfaceapi.bukkit.page;

import java.util.Optional;
import java.util.UUID;

import org.bukkit.Bukkit;

import me.clip.placeholderapi.PlaceholderAPI;
import me.mrletsplay.webinterfaceapi.bukkit.BukkitSettings;
import me.mrletsplay.webinterfaceapi.bukkit.WebinterfacePlugin;
import me.mrletsplay.webinterfaceapi.webinterface.Webinterface;
import me.mrletsplay.webinterfaceapi.webinterface.auth.WebinterfaceAccount;
import me.mrletsplay.webinterfaceapi.webinterface.auth.WebinterfaceAccountConnection;
import me.mrletsplay.webinterfaceapi.webinterface.page.WebinterfacePage;
import me.mrletsplay.webinterfaceapi.webinterface.page.WebinterfacePageSection;
import me.mrletsplay.webinterfaceapi.webinterface.page.action.SendJSAction;
import me.mrletsplay.webinterfaceapi.webinterface.page.action.value.ElementValue;
import me.mrletsplay.webinterfaceapi.webinterface.page.element.WebinterfaceElementGroup;
import me.mrletsplay.webinterfaceapi.webinterface.page.element.WebinterfaceImage;
import me.mrletsplay.webinterfaceapi.webinterface.page.element.WebinterfaceInputField;
import me.mrletsplay.webinterfaceapi.webinterface.page.element.WebinterfaceText;
import me.mrletsplay.webinterfaceapi.webinterface.page.element.WebinterfaceTitleText;
import me.mrletsplay.webinterfaceapi.webinterface.page.element.layout.DefaultLayoutOption;
import me.mrletsplay.webinterfaceapi.webinterface.page.element.layout.GridLayout;
import me.mrletsplay.webinterfaceapi.webinterface.session.WebinterfaceSession;

public class BukkitPage extends WebinterfacePage {

	public BukkitPage() {
		super("Home", "/bukkit/home");
		
		dynamic(() -> {
			WebinterfaceAccount acc = WebinterfaceSession.getCurrentSession().getAccount();
			WebinterfaceAccountConnection mcAcc = acc.getConnection(WebinterfacePlugin.MINECRAFT_ACCOUNT_CONNECTION_NAME);
			
			if(mcAcc == null) return Optional.empty();
			
			WebinterfacePageSection yourAccount = new WebinterfacePageSection();
			yourAccount.setSlimLayout(true);
			yourAccount.addLayoutOptions(DefaultLayoutOption.FULL_WIDTH);
			yourAccount.addTitle("Your Profile");
			
			yourAccount.addElement(WebinterfaceImage.builder()
					.src("https://cravatar.eu/helmavatar/" + mcAcc.getUserName() + "/128.png")
					.alt("Avatar")
					.width("128px")
					.height("128px")
					.create());
			
			WebinterfaceElementGroup g = new WebinterfaceElementGroup(); 
			WebinterfaceTitleText tt = new WebinterfaceTitleText(mcAcc.getUserName());
			tt.getStyle().setProperty("font-size", "26px");
			g.addElement(tt);
			
			if(Webinterface.getConfig().getSetting(BukkitSettings.SHOW_STATS)) {
				if(!Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
					Webinterface.getLogger().warn("Statistics are enabled on Bukkit Home page but PlaceholderAPI is missing");
				}else {
					WebinterfaceElementGroup stats = new WebinterfaceElementGroup();
					stats.addLayoutOptions(new GridLayout("auto", "auto"));
					
					for(String el : Webinterface.getConfig().getSetting(BukkitSettings.STATS_ELEMENTS)) {
						String[] spl = el.split(":");
						if(spl.length != 2) {
							Webinterface.getLogger().warn("Invalid statistics element for Bukkit Home page configured in config.yml: \"" + el + "\"");
							continue;
						}
						
						stats.addElement(WebinterfaceTitleText.builder().text(spl[0]).leftboundText().create());
						stats.addElement(WebinterfaceText.builder().text(PlaceholderAPI.setPlaceholders(Bukkit.getOfflinePlayer(UUID.fromString(mcAcc.getUserID())), spl[1])).leftboundText().create());
					}
					g.addElement(stats);
				}
			}
			
			yourAccount.addElement(g);
			return Optional.of(yourAccount);
		});
		
		WebinterfacePageSection sets = new WebinterfacePageSection();
		sets.addTitle("Settings");
		sets.addLayoutOptions(DefaultLayoutOption.FULL_WIDTH);
		
		sets.dynamic(els -> {
			WebinterfaceAccount acc = WebinterfaceSession.getCurrentSession().getAccount();
			WebinterfaceAccountConnection mcAcc = acc.getConnection(WebinterfacePlugin.MINECRAFT_ACCOUNT_CONNECTION_NAME);
			
			if(mcAcc != null) {
				els.add(WebinterfaceText.builder()
						.text("Connected Minecraft account")
						.create());
				
				els.add(WebinterfaceText.builder()
						.text(mcAcc.getUserName() + " (" + mcAcc.getUserID() + ")")
						.withLayoutOptions(DefaultLayoutOption.SECOND_TO_LAST_COLUMN)
						.create());
			}else {
				els.add(WebinterfaceText.builder()
						.text("Connect to Minecraft account")
						.create());
				
				els.add(WebinterfaceInputField.builder()
						.placeholder("Minecraft name")
						.onChange(f -> new SendJSAction("bukkit", "connectMinecraftAccount", new ElementValue(f)))
						.create());
			}
		});
		
		addSection(sets);
	}

}
