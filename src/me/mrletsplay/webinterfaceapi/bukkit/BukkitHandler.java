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
import me.mrletsplay.webinterfaceapi.webinterface.auth.WebinterfaceAccount;
import me.mrletsplay.webinterfaceapi.webinterface.auth.WebinterfaceAccountConnection;
import me.mrletsplay.webinterfaceapi.webinterface.page.action.WebinterfaceActionHandler;
import me.mrletsplay.webinterfaceapi.webinterface.page.action.WebinterfaceHandler;
import me.mrletsplay.webinterfaceapi.webinterface.page.action.WebinterfaceRequestEvent;
import me.mrletsplay.webinterfaceapi.webinterface.page.action.WebinterfaceResponse;
import me.mrletsplay.webinterfaceapi.webinterface.session.WebinterfaceSession;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ComponentBuilder;

public class BukkitHandler implements WebinterfaceActionHandler {
	
	private static final UI CONNECT_UI = new UIBuilder()
			.addElement("connect_text", new UIElement() {
				
				@Override
				public BaseComponent[] getLayout(UIBuildEvent e) {
					System.out.println(e.getUIInstance().getProperties());
					return new ComponentBuilder("Connect to "
							+ ((WebinterfaceAccount) e.getUIInstance().getProperty(WebinterfacePlugin.pl, "account")).getEmail()
							+ "?").create();
				}
			})
			.addElement("accept", new StaticUIElement("§a[Yes]")
					.setAction(uie -> {
						Player p = uie.getPlayer();
						WebinterfaceAccount acc = (WebinterfaceAccount) uie.getUIInstance().getProperty(WebinterfacePlugin.pl, "account");
						WebinterfaceAccountConnection con = new WebinterfaceAccountConnection(
								WebinterfacePlugin.MINECRAFT_ACCOUNT_CONNECTION_NAME,
								p.getUniqueId().toString(),
								p.getName(),
								null,
								null);
						if(acc.getConnection(WebinterfacePlugin.MINECRAFT_ACCOUNT_CONNECTION_NAME) != null) {
							p.sendMessage("§cAlready connected");
							return;
						}
						acc.addConnection(con);
						p.sendMessage("§aConnected!");
					})
					.setHoverText("Connect to WIAPI"))
			.addElement("decline", new StaticUIElement("§c[No]")
					.setAction(uie -> {})
					.setHoverText("§cDon't connect to WIAPI"))
			.setLayout(new UILayout()
					.addElement("connect_text")
					.newLine()
					.addElement("accept")
					.addText(" §8| ")
					.addElement("decline")).build();

	@WebinterfaceHandler(requestTarget = "bukkit", requestTypes = "connectMCAccount")
	public static WebinterfaceResponse connectMCAccount(WebinterfaceRequestEvent event) {
		String mcAccName = event.getRequestData().getString("value");
		Player p = Bukkit.getPlayer(mcAccName);
		if(p == null) return WebinterfaceResponse.error("Invalid player");
		WebinterfaceAccount acc = WebinterfaceSession.getCurrentSession().getAccount();
		if(acc.getConnection(WebinterfacePlugin.MINECRAFT_ACCOUNT_CONNECTION_NAME) != null)
			return WebinterfaceResponse.error("Account already connected");
		CONNECT_UI
			.getForPlayer(p, WebinterfacePlugin.pl,
				new QuickMap<String, Object>().put("account", acc).makeHashMap())
			.sendToPlayer(p);
		return WebinterfaceResponse.success();
	}
	
}
