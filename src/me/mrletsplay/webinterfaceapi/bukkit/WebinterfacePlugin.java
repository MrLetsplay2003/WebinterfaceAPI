package me.mrletsplay.webinterfaceapi.bukkit;

import org.bukkit.OfflinePlayer;
import org.bukkit.plugin.java.JavaPlugin;

import me.mrletsplay.webinterfaceapi.bukkit.command.CommandWebinterfaceAPI;
import me.mrletsplay.webinterfaceapi.bukkit.page.BukkitPage;
import me.mrletsplay.webinterfaceapi.webinterface.Webinterface;
import me.mrletsplay.webinterfaceapi.webinterface.auth.WebinterfaceAccount;
import me.mrletsplay.webinterfaceapi.webinterface.auth.WebinterfaceAccountConnection;
import me.mrletsplay.webinterfaceapi.webinterface.page.WebinterfacePageCategory;

public class WebinterfacePlugin extends JavaPlugin {
	
	public static final String
		MINECRAFT_ACCOUNT_CONNECTION_NAME = "minecraft";

	public static JavaPlugin pl;
	
	@Override
	public void onEnable() {
		pl = this;
		MrCoreBukkitImpl.loadMrCore(this);
		
		getCommand("webinterfaceapi").setExecutor(new CommandWebinterfaceAPI());
		Webinterface.setRootDirectory(getDataFolder());
		
		WebinterfacePageCategory cat = Webinterface.createCategory("Bukkit");
		cat.addPage(new BukkitPage());
		
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
