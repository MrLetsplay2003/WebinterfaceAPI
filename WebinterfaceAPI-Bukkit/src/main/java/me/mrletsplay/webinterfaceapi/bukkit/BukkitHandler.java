package me.mrletsplay.webinterfaceapi.bukkit;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import me.mrletsplay.mrcore.bukkitimpl.ui.StaticUIElement;
import me.mrletsplay.mrcore.bukkitimpl.ui.UI;
import me.mrletsplay.mrcore.bukkitimpl.ui.UIBuilder;
import me.mrletsplay.mrcore.bukkitimpl.ui.UIElement;
import me.mrletsplay.mrcore.bukkitimpl.ui.UILayout;
import me.mrletsplay.mrcore.bukkitimpl.ui.event.UIBuildEvent;
import me.mrletsplay.mrcore.misc.QuickMap;
import me.mrletsplay.webinterfaceapi.auth.Account;
import me.mrletsplay.webinterfaceapi.auth.AccountConnection;
import me.mrletsplay.webinterfaceapi.bukkit.command.CommandWebinterfaceAPI;
import me.mrletsplay.webinterfaceapi.page.action.ActionEvent;
import me.mrletsplay.webinterfaceapi.page.action.ActionHandler;
import me.mrletsplay.webinterfaceapi.page.action.ActionResponse;
import me.mrletsplay.webinterfaceapi.page.action.WebinterfaceHandler;
import me.mrletsplay.webinterfaceapi.session.Session;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.HoverEvent.Action;

public class BukkitHandler implements ActionHandler {

	private static final UI CONNECT_UI = new UIBuilder()
			.addElement("connect_text", new UIElement() {

				@Override
				public BaseComponent[] getLayout(UIBuildEvent e) {
					Account acc = (Account) e.getUIInstance().getProperty(WebinterfacePlugin.plugin, "account");
					return new ComponentBuilder("Connect to "
							+ acc.getName()
							+ "?").event(new HoverEvent(Action.SHOW_TEXT, CommandWebinterfaceAPI.createHoverText(acc))).create();
				}
			})
			.addElement("accept", new StaticUIElement("§a[Yes]")
					.setAction(uie -> {
						Player p = uie.getPlayer();
						Account acc = (Account) uie.getUIInstance().getProperty(WebinterfacePlugin.plugin, "account");
						AccountConnection con = new AccountConnection(
								WebinterfacePlugin.MINECRAFT_ACCOUNT_CONNECTION_NAME,
								p.getUniqueId().toString(),
								p.getName(),
								null,
								null);
						if(acc.getConnection(WebinterfacePlugin.MINECRAFT_ACCOUNT_CONNECTION_NAME) != null) {
							p.sendMessage("§cAlready connected");
							uie.getUIInstance().destroy();
							return;
						}
						acc.addConnection(con);
						p.sendMessage("§aConnected!");
						uie.getUIInstance().destroy();
					})
					.setHoverText("Connect to WIAPI"))
			.addElement("decline", new StaticUIElement("§c[No]")
					.setAction(uie -> {
						Player p = uie.getPlayer();
						p.sendMessage("§cDeclined");
						uie.getUIInstance().destroy();
					})
					.setHoverText("§cDon't connect to WIAPI"))
			.setLayout(UILayout.of(
					  "§8[§7+§8]----------------------[§7+§8]\n"
					+ " §r{connect_text}\n"
					+ " {accept} §8- {decline}\n"
					+ "§8[§7+§8]----------------------[§7+§8]"
					)).build();

	@WebinterfaceHandler(requestTarget = "bukkit", requestTypes = "connectMinecraftAccount")
	public static ActionResponse connectMCAccount(ActionEvent event) {
		String mcAccName = event.getData().getString("name");
		Player p = Bukkit.getPlayer(mcAccName);
		if(p == null) return ActionResponse.error("Invalid player");
		Account acc = Session.getCurrentSession().getAccount();
		if(acc.getConnection(WebinterfacePlugin.MINECRAFT_ACCOUNT_CONNECTION_NAME) != null)
			return ActionResponse.error("Account already connected");
		CONNECT_UI
			.getForPlayer(p, WebinterfacePlugin.plugin,
				new QuickMap<String, Object>().put("account", acc).makeHashMap())
			.sendToPlayer(p);
		return ActionResponse.success();
	}

}
