package me.mrletsplay.webinterfaceapi.bukkit;

import org.bukkit.plugin.java.JavaPlugin;

import me.mrletsplay.webinterfaceapi.webinterface.Webinterface;

public class WebinterfacePlugin extends JavaPlugin {

	@Override
	public void onEnable() {
		MrCoreBukkitImpl.loadMrCore(this);
		Webinterface.setRootDirectory(getDataFolder());
		Webinterface.start();
		getLogger().info("WebinterfaceAPI loaded");
	}
	
	@Override
	public void onDisable() {
		Webinterface.shutdown();
		getLogger().info("Shut down Webinterface");
	}
	
}
