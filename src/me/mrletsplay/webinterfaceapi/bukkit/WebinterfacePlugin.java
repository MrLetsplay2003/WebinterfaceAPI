package me.mrletsplay.webinterfaceapi.bukkit;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.OfflinePlayer;
import org.bukkit.plugin.java.JavaPlugin;

import me.mrletsplay.webinterfaceapi.webinterface.Webinterface;
import me.mrletsplay.webinterfaceapi.webinterface.auth.WebinterfaceAccount;
import me.mrletsplay.webinterfaceapi.webinterface.auth.WebinterfaceAccountConnection;
import me.mrletsplay.webinterfaceapi.webinterface.page.WebinterfacePage;
import me.mrletsplay.webinterfaceapi.webinterface.page.WebinterfacePageSection;
import me.mrletsplay.webinterfaceapi.webinterface.page.action.SendJSAction;
import me.mrletsplay.webinterfaceapi.webinterface.page.action.value.ElementValue;
import me.mrletsplay.webinterfaceapi.webinterface.page.element.ElementLayout;
import me.mrletsplay.webinterfaceapi.webinterface.page.element.WebinterfaceInputField;
import me.mrletsplay.webinterfaceapi.webinterface.page.element.WebinterfacePageElement;
import me.mrletsplay.webinterfaceapi.webinterface.page.element.WebinterfaceText;
import me.mrletsplay.webinterfaceapi.webinterface.session.WebinterfaceSession;

public class WebinterfacePlugin extends JavaPlugin {
	
	public static final String
		MINECRAFT_ACCOUNT_CONNECTION_NAME = "minecraft";

	public static JavaPlugin pl;
	
	@Override
	public void onEnable() {
		pl = this;
		MrCoreBukkitImpl.loadMrCore(this);
		Webinterface.setRootDirectory(getDataFolder());
		
		WebinterfacePage pg = new WebinterfacePage("Bukkit", "/bukkit");
		
		WebinterfacePageSection sets = new WebinterfacePageSection();
		sets.addTitle("Settings");
		
		sets.addDynamicElements(() -> {
			List<WebinterfacePageElement> els = new ArrayList<>();
			
			WebinterfaceAccount acc = WebinterfaceSession.getCurrentSession().getAccount();
			WebinterfaceAccountConnection mcAcc = acc.getConnection(MINECRAFT_ACCOUNT_CONNECTION_NAME);
			
			if(mcAcc != null) {
				WebinterfaceText txt = new WebinterfaceText("Connected Minecraft account");
				txt.addLayouts(ElementLayout.CENTER_VERTICALLY);
				els.add(txt);
				
				WebinterfaceText txt2 = new WebinterfaceText(mcAcc.getUserName() + " (" + mcAcc.getUserID() + ")");
				txt2.addLayouts(ElementLayout.SECOND_TO_LAST_COLUMN);
				els.add(txt2);
			}else {
				WebinterfaceText txt = new WebinterfaceText("Connect to Minecraft account");
				txt.addLayouts(ElementLayout.CENTER_VERTICALLY);
				els.add(txt);
				
				WebinterfaceInputField f = new WebinterfaceInputField("MC name");
				f.setOnChangeAction(new SendJSAction("bukkit", "connectMCAccount", new ElementValue(f)));
				els.add(f);
			}
			return els;
		});
		
		pg.addSection(sets);
		Webinterface.registerPage(pg);
		
		Webinterface.registerActionHandler(new BukkitHandler());
		
		Webinterface.start();
		getLogger().info("WebinterfaceAPI loaded");
	}
	
	@Override
	public void onDisable() {
		Webinterface.shutdown();
		getLogger().info("Shut down Webinterface");
	}
	
	public static WebinterfaceAccountConnection getConnectedMinecraftAccount(WebinterfaceAccount account) {
		return account.getConnection(MINECRAFT_ACCOUNT_CONNECTION_NAME);
	}
	
	public static WebinterfaceAccount getConnectedWIAPIAccountByID(String playerUUID) {
		return Webinterface.getAccountStorage().getAccounts().stream()
				.filter(acc -> {
					WebinterfaceAccountConnection con = getConnectedMinecraftAccount(acc);
					return con != null && con.getUserID().equals(playerUUID);
				})
				.findFirst().orElse(null);
	}
	
	public static WebinterfaceAccount getConnectedWIAPIAccount(OfflinePlayer player) {
		return getConnectedWIAPIAccountByID(player.getUniqueId().toString());
	}
	
}
