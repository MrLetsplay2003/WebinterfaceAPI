package me.mrletsplay.webinterfaceapi.bukkit;

import org.bukkit.plugin.java.JavaPlugin;

import me.mrletsplay.webinterfaceapi.webinterface.Webinterface;
import me.mrletsplay.webinterfaceapi.webinterface.page.WebinterfacePage;
import me.mrletsplay.webinterfaceapi.webinterface.page.WebinterfacePageSection;

public class WebinterfacePlugin extends JavaPlugin {

	@Override
	public void onEnable() {
		MrCoreBukkitImpl.loadMrCore(this);
		Webinterface.setRootDirectory(getDataFolder());
		
		WebinterfacePage pg = new WebinterfacePage("Bukkit", "/bukkit");
		
		WebinterfacePageSection sets = new WebinterfacePageSection();
		sets.addTitle("Settings");
		
		Webinterface.registerPage(pg);
		
		Webinterface.start();
		getLogger().info("WebinterfaceAPI loaded");
	}
	
	@Override
	public void onDisable() {
		Webinterface.shutdown();
		getLogger().info("Shut down Webinterface");
	}
	
}
