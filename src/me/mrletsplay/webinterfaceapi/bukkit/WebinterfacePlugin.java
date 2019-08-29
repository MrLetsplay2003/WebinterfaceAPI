package me.mrletsplay.webinterfaceapi.bukkit;

import org.bukkit.plugin.java.JavaPlugin;

import me.mrletsplay.webinterfaceapi.webinterface.Webinterface;
import me.mrletsplay.webinterfaceapi.webinterface.page.WebinterfacePage;
import me.mrletsplay.webinterfaceapi.webinterface.page.WebinterfacePageSection;
import me.mrletsplay.webinterfaceapi.webinterface.page.action.SendJSAction;
import me.mrletsplay.webinterfaceapi.webinterface.page.action.value.ElementValue;
import me.mrletsplay.webinterfaceapi.webinterface.page.element.WebinterfaceInputField;

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
		
		WebinterfaceInputField f = new WebinterfaceInputField("MC name");
		f.setOnChangeAction(new SendJSAction("bukkit", "connectMCAccount", new ElementValue(f)));
		sets.addElement(f);
		
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
	
}
