package me.mrletsplay.webinterfaceapi;
import me.mrletsplay.mrcore.json.JSONObject;
import me.mrletsplay.simplehttpserver.http.websocket.WebSocketConnection;
import me.mrletsplay.webinterfaceapi.auth.Account;
import me.mrletsplay.webinterfaceapi.document.websocket.WebSocketData;
import me.mrletsplay.webinterfaceapi.page.SettingsPage;
import me.mrletsplay.webinterfaceapi.page.action.ActionEvent;
import me.mrletsplay.webinterfaceapi.page.action.ActionHandler;
import me.mrletsplay.webinterfaceapi.page.action.ActionResponse;
import me.mrletsplay.webinterfaceapi.page.action.WebinterfaceHandler;
import me.mrletsplay.webinterfaceapi.page.event.WebinterfaceEvent;

public class DefaultHandler implements ActionHandler {

	@WebinterfaceHandler(requestTarget = "webinterface", requestTypes = "restart", permission = DefaultPermissions.RESTART)
	public ActionResponse restart(ActionEvent event) {
		new Thread(() -> {
			Webinterface.getLogger().info("Restarting...");
			Webinterface.shutdown();
			Webinterface.start();
			Webinterface.getLogger().info("Done!");
		}, "Webinterface-Restart-Thread").start();
		return ActionResponse.success();
	}

	@WebinterfaceHandler(requestTarget = "webinterface", requestTypes = "shutdown", permission = DefaultPermissions.SHUTDOWN)
	public ActionResponse shutdown(ActionEvent event) {
		new Thread(() -> {
			Webinterface.getLogger().info("Shutting down...");
			Webinterface.shutdown();
		}, "Webinterface-Shutdown-Thread").start();
		return ActionResponse.success();
	}

	@WebinterfaceHandler(requestTarget = "webinterface", requestTypes = "setSetting", permission = DefaultPermissions.SETTINGS)
	public ActionResponse setSetting(ActionEvent event) {
		return SettingsPage.handleSetSettingRequest(Webinterface.getConfig(), event);
	}

	@WebinterfaceHandler(requestTarget = "webinterface", requestTypes = "setPermission", permission = DefaultPermissions.MODIFY_USERS)
	public ActionResponse setOP(ActionEvent event) {
		JSONObject val = event.getRequestData().getJSONObject("value");
		String accountID = val.getString("account");
		String perm = val.getString("permission");
		boolean b = val.getBoolean("value");
		Account acc = Webinterface.getAccountStorage().getAccountByID(accountID);
		if(acc == null) return ActionResponse.error("Account doesn't exist");
		if(event.getAccount().getID().equals(accountID)) return ActionResponse.error("You cannot change your own permissions");
		if(b && !acc.hasPermissionExactly(perm)) {
			acc.addPermission(perm);
		}else if(!b && acc.hasPermissionExactly(perm)) {
			acc.removePermission(perm);
		}
		return ActionResponse.success();
	}

	@WebinterfaceHandler(requestTarget = "webinterface", requestTypes = "addPermission", permission = DefaultPermissions.MODIFY_USERS)
	public ActionResponse addPermission(ActionEvent event) {
		JSONObject val = event.getRequestData().getJSONObject("value");
		String permission = val.getString("permission");
		String accountID = val.getString("account_id");
		Account acc = Webinterface.getAccountStorage().getAccountByID(accountID);
		if(acc == null) return ActionResponse.error("Account doesn't exist");
		acc.addPermission(permission);
		return ActionResponse.success();
	}

	@WebinterfaceHandler(requestTarget = "webinterface", requestTypes = "removePermission", permission = DefaultPermissions.MODIFY_USERS)
	public ActionResponse removePermission(ActionEvent event) {
		JSONObject val = event.getRequestData().getJSONObject("value");
		String permission = val.getString("permission");
		String accountID = val.getString("account_id");
		Account acc = Webinterface.getAccountStorage().getAccountByID(accountID);
		if(acc == null) return ActionResponse.error("Account doesn't exist");
		acc.removePermission(permission);
		return ActionResponse.success();
	}

	@WebinterfaceHandler(requestTarget = "webinterface", requestTypes = "deleteAccount", permission = DefaultPermissions.MODIFY_USERS)
	public ActionResponse deleteAccount(ActionEvent event) {
		String accountID = event.getRequestData().getString("value");
		Account acc = Webinterface.getAccountStorage().getAccountByID(accountID);
		if(acc == null) return ActionResponse.error("Account doesn't exist");
		Webinterface.getAccountStorage().deleteAccount(acc);
		return ActionResponse.success();
	}

	@WebinterfaceHandler(requestTarget = "webinterface", requestTypes = "removeAccountConnection")
	public ActionResponse removeAccountConnection(ActionEvent event) {
		Account account = event.getAccount();
		String authMethod = event.getRequestData().getString("value");
		if(account.getConnections().size() > 1) account.removeConnection(authMethod);
		return ActionResponse.success();
	}

	@WebinterfaceHandler(requestTarget = "webinterface", requestTypes = "subscribeToEvent")
	public ActionResponse subscribeToEvent(ActionEvent event) {
		Account account = event.getAccount();
		String target = event.getRequestData().getString("eventTarget");
		String name = event.getRequestData().getString("eventName");

		if(!event.isFromWebSocket()) return ActionResponse.error("Only allowed for WebSockets");

		WebinterfaceEvent e = Webinterface.getEvent(target, name);
		if(e == null) return ActionResponse.error("Event doesn't exist");

		if(e.getPermission() != null && !account.hasPermission(e.getPermission()))
			return ActionResponse.error("No permission");

		WebSocketConnection con = event.getWebSocketConnection();
		WebSocketData d = con.getAttachment();
		d.subscribe(e);

		return ActionResponse.success();
	}

}
