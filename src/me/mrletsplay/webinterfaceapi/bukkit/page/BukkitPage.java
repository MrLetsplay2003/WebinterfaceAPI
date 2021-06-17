package me.mrletsplay.webinterfaceapi.bukkit.page;

import java.util.ArrayList;
import java.util.List;
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
import me.mrletsplay.webinterfaceapi.webinterface.page.element.WebinterfacePageElement;
import me.mrletsplay.webinterfaceapi.webinterface.page.element.WebinterfaceText;
import me.mrletsplay.webinterfaceapi.webinterface.page.element.WebinterfaceTitleText;
import me.mrletsplay.webinterfaceapi.webinterface.page.element.layout.DefaultLayoutProperty;
import me.mrletsplay.webinterfaceapi.webinterface.page.element.layout.GridLayout;
import me.mrletsplay.webinterfaceapi.webinterface.session.WebinterfaceSession;

public class BukkitPage extends WebinterfacePage {

	public BukkitPage() {
		super("Home", "/bukkit/home");
		getContainerStyle().setProperty("max-width", "900px");
		
		addDynamicSections(() -> {
			List<WebinterfacePageSection> secs = new ArrayList<>();
			
			WebinterfaceAccount acc = WebinterfaceSession.getCurrentSession().getAccount();
			WebinterfaceAccountConnection mcAcc = acc.getConnection(WebinterfacePlugin.MINECRAFT_ACCOUNT_CONNECTION_NAME);
			
			if(mcAcc != null) {
				WebinterfacePageSection yourAccount = new WebinterfacePageSection();
				yourAccount.addInnerLayoutProperties(DefaultLayoutProperty.FULL_WIDTH);
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
						stats.addInnerLayoutProperties(new GridLayout("auto", "auto"));
						
						for(String el : Webinterface.getConfig().getSetting(BukkitSettings.STATS_ELEMENTS)) {
							String[] spl = el.split(":");
							if(spl.length != 2) {
								Webinterface.getLogger().warn("Invalid statistics element for Bukkit Home page configured in config.yml: \"" + el + "\"");
								continue;
							}
							
							stats.addElement(WebinterfaceTitleText.builder().text(spl[0]).leftbound().create());
							stats.addElement(WebinterfaceText.builder().text(PlaceholderAPI.setPlaceholders(Bukkit.getOfflinePlayer(UUID.fromString(mcAcc.getUserID())), spl[1])).leftbound().create());
						}
						g.addElement(stats);
					}
				}
				
				yourAccount.addElement(g);
				
				secs.add(yourAccount);
			}
			
			return secs;
		});
		
		WebinterfacePageSection sets = new WebinterfacePageSection();
		sets.addTitle("Settings");
		sets.addInnerLayoutProperties(DefaultLayoutProperty.FULL_WIDTH);
		
		sets.addDynamicElements(() -> {
			List<WebinterfacePageElement> els = new ArrayList<>();
			
			WebinterfaceAccount acc = WebinterfaceSession.getCurrentSession().getAccount();
			WebinterfaceAccountConnection mcAcc = acc.getConnection(WebinterfacePlugin.MINECRAFT_ACCOUNT_CONNECTION_NAME);
			
			if(mcAcc != null) {
				WebinterfaceText txt = new WebinterfaceText("Connected Minecraft account");
				txt.addLayoutProperties(DefaultLayoutProperty.CENTER_VERTICALLY);
				els.add(txt);
				
				WebinterfaceText txt2 = new WebinterfaceText(mcAcc.getUserName() + " (" + mcAcc.getUserID() + ")");
				txt2.addLayoutProperties(DefaultLayoutProperty.SECOND_TO_LAST_COLUMN);
				els.add(txt2);
			}else {
				WebinterfaceText txt = new WebinterfaceText("Connect to Minecraft account");
				txt.addLayoutProperties(DefaultLayoutProperty.CENTER_VERTICALLY);
				els.add(txt);
				
				WebinterfaceInputField f = new WebinterfaceInputField("Minecraft name");
				f.setOnChangeAction(new SendJSAction("bukkit", "connectMinecraftAccount", new ElementValue(f)));
				els.add(f);
			}
			return els;
		});
		
		addSection(sets);
	}

}
