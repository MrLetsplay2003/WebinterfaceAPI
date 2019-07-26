package me.mrletsplay.webinterfaceapi.bukkit;

import org.bukkit.plugin.java.JavaPlugin;

import me.mrletsplay.mrcore.main.MrCoreServiceRegistry;
import me.mrletsplay.webinterfaceapi.webinterface.Webinterface;

public class WebinterfacePlugin extends JavaPlugin {

	@Override
	public void onEnable() {
		Webinterface.start();
		MrCoreServiceRegistry.registerService("webinterface", this);
	}
	
}
