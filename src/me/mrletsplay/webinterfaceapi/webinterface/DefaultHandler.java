package me.mrletsplay.webinterfaceapi.webinterface;
import me.mrletsplay.mrcore.json.JSONObject;
import me.mrletsplay.webinterfaceapi.webinterface.auth.WebinterfaceAccount;
import me.mrletsplay.webinterfaceapi.webinterface.page.WebinterfaceSettingsPage;
import me.mrletsplay.webinterfaceapi.webinterface.page.action.WebinterfaceActionHandler;
import me.mrletsplay.webinterfaceapi.webinterface.page.action.WebinterfaceHandler;
import me.mrletsplay.webinterfaceapi.webinterface.page.action.WebinterfaceRequestEvent;
import me.mrletsplay.webinterfaceapi.webinterface.page.action.WebinterfaceResponse;

public class DefaultHandler implements WebinterfaceActionHandler {
	
	@WebinterfaceHandler(requestTarget = "webinterface", requestTypes = "restart")
	public WebinterfaceResponse restart(WebinterfaceRequestEvent event) {
		if(!event.getAccount().hasPermission(DefaultPermissions.RESTART))
			return WebinterfaceResponse.error("No permission");
		new Thread(() -> {
			Webinterface.getLogger().info("Restarting...");
			Webinterface.shutdown();
			Webinterface.start();
			Webinterface.getLogger().info("Done!");
		}, "Webinterface-Restart-Thread").start();
		return WebinterfaceResponse.success();
	}
	
	@WebinterfaceHandler(requestTarget = "webinterface", requestTypes = "shutdown")
	public WebinterfaceResponse shutdown(WebinterfaceRequestEvent event) {
		if(!event.getAccount().hasPermission(DefaultPermissions.SHUTDOWN))
			return WebinterfaceResponse.error("No permission");
		new Thread(() -> {
			Webinterface.getLogger().info("Shutting down...");
			Webinterface.shutdown();
		}, "Webinterface-Shutdown-Thread").start();
		return WebinterfaceResponse.success();
	}

	@WebinterfaceHandler(requestTarget = "webinterface", requestTypes = "setSetting")
	public WebinterfaceResponse setSetting(WebinterfaceRequestEvent event) {
		if(!event.getAccount().hasPermission(DefaultPermissions.SETTINGS))
			return WebinterfaceResponse.error("No permission");
		return WebinterfaceSettingsPage.handleSetSettingRequest(Webinterface.getConfig(), event);
	}
	
	@WebinterfaceHandler(requestTarget = "webinterface", requestTypes = "setOP")
	public WebinterfaceResponse setOP(WebinterfaceRequestEvent event) {
		if(!event.getAccount().hasPermission(DefaultPermissions.MODIFY_USERS))
			return WebinterfaceResponse.error("No permission");
		JSONObject val = event.getRequestData().getJSONObject("value");
		boolean b = val.getBoolean("value");
		String accountID = val.getString("account_id");
		WebinterfaceAccount acc = Webinterface.getAccountStorage().getAccountByID(accountID);
		if(acc == null) return WebinterfaceResponse.error("Account doesn't exist");
		if(b && !acc.hasPermission("*")) {
			acc.addPermission("*");
		}else if(!b) {
			acc.removePermission("*");
		}
		return WebinterfaceResponse.success();
	}
	
	@WebinterfaceHandler(requestTarget = "webinterface", requestTypes = "addPermission")
	public WebinterfaceResponse addPermission(WebinterfaceRequestEvent event) {
		if(!event.getAccount().hasPermission(DefaultPermissions.MODIFY_USERS))
			return WebinterfaceResponse.error("No permission");
		JSONObject val = event.getRequestData().getJSONObject("value");
		String permission = val.getString("permission");
		String accountID = val.getString("account_id");
		WebinterfaceAccount acc = Webinterface.getAccountStorage().getAccountByID(accountID);
		if(acc == null) return WebinterfaceResponse.error("Account doesn't exist");
		acc.addPermission(permission);
		return WebinterfaceResponse.success();
	}
	
	@WebinterfaceHandler(requestTarget = "webinterface", requestTypes = "removePermission")
	public WebinterfaceResponse removePermission(WebinterfaceRequestEvent event) {
		if(!event.getAccount().hasPermission(DefaultPermissions.MODIFY_USERS))
			return WebinterfaceResponse.error("No permission");
		JSONObject val = event.getRequestData().getJSONObject("value");
		String permission = val.getString("permission");
		String accountID = val.getString("account_id");
		WebinterfaceAccount acc = Webinterface.getAccountStorage().getAccountByID(accountID);
		if(acc == null) return WebinterfaceResponse.error("Account doesn't exist");
		acc.removePermission(permission);
		return WebinterfaceResponse.success();
	}
	
	@WebinterfaceHandler(requestTarget = "webinterface", requestTypes = "deleteAccount")
	public WebinterfaceResponse deleteAccount(WebinterfaceRequestEvent event) {
		if(!event.getAccount().hasPermission(DefaultPermissions.MODIFY_USERS))
			return WebinterfaceResponse.error("No permission");
		String accountID = event.getRequestData().getString("value");
		WebinterfaceAccount acc = Webinterface.getAccountStorage().getAccountByID(accountID);
		if(acc == null) return WebinterfaceResponse.error("Account doesn't exist");
		Webinterface.getAccountStorage().deleteAccount(acc);
		return WebinterfaceResponse.success();
	}
	
	@WebinterfaceHandler(requestTarget = "webinterface", requestTypes = "removeAccountConnection")
	public WebinterfaceResponse removeAccountConnection(WebinterfaceRequestEvent event) {
		WebinterfaceAccount account = event.getAccount();
		String authMethod = event.getRequestData().getString("value");
		if(account.getConnections().size() > 1) account.removeConnection(authMethod);
		return WebinterfaceResponse.success();
	}
	
}
