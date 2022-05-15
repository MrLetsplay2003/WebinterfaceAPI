package me.mrletsplay.webinterfaceapi.bukkit;

import org.bukkit.OfflinePlayer;
import org.bukkit.plugin.java.JavaPlugin;

import me.mrletsplay.webinterfaceapi.bukkit.command.CommandWebinterfaceAPI;
import me.mrletsplay.webinterfaceapi.bukkit.page.BukkitPage;
import me.mrletsplay.webinterfaceapi.bukkit.page.BukkitSettingsPage;
import me.mrletsplay.webinterfaceapi.webinterface.Webinterface;
import me.mrletsplay.webinterfaceapi.webinterface.auth.Account;
import me.mrletsplay.webinterfaceapi.webinterface.auth.AccountConnection;
import me.mrletsplay.webinterfaceapi.webinterface.page.PageCategory;

public class WebinterfacePlugin extends JavaPlugin {
	
	public static final String
		MINECRAFT_ACCOUNT_CONNECTION_NAME = "minecraft";

	public static JavaPlugin plugin;
	
	@Override
	public void onEnable() {
		plugin = this;
		
		getCommand("webinterfaceapi").setExecutor(new CommandWebinterfaceAPI());
		
		Webinterface.setRootDirectory(getDataFolder());
		
		PageCategory cat = Webinterface.createCategory("Bukkit");
		cat.addPage(new BukkitPage());
		cat.addPage(new BukkitSettingsPage());
		
		Webinterface.registerActionHandler(new BukkitHandler());
		
		Webinterface.start();
		Webinterface.getConfig().registerSettings(BukkitSettings.INSTANCE);
		
		getLogger().info("WebinterfaceAPI loaded");
	}
	
	@Override
	public void onDisable() {
		Webinterface.shutdown();
		getLogger().info("Shut down Webinterface");
	}
	
	public static AccountConnection getConnectedMinecraftAccount(Account account) {
		return account.getConnection(MINECRAFT_ACCOUNT_CONNECTION_NAME);
	}
	
	public static Account getConnectedWIAPIAccountByID(String playerUUID) {
		return Webinterface.getAccountStorage().getAccounts().stream()
				.filter(acc -> {
					AccountConnection con = getConnectedMinecraftAccount(acc);
					return con != null && con.getUserID().equals(playerUUID);
				})
				.findFirst().orElse(null);
	}
	
	public static Account getConnectedWIAPIAccount(OfflinePlayer player) {
		return getConnectedWIAPIAccountByID(player.getUniqueId().toString());
	}
	
}
