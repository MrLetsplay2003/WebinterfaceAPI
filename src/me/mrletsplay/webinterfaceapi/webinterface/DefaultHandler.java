package me.mrletsplay.webinterfaceapi.webinterface;
import me.mrletsplay.mrcore.json.JSONObject;
import me.mrletsplay.webinterfaceapi.webinterface.auth.WebinterfaceAccount;
import me.mrletsplay.webinterfaceapi.webinterface.config.DefaultSettings;
import me.mrletsplay.webinterfaceapi.webinterface.page.action.WebinterfaceActionHandler;
import me.mrletsplay.webinterfaceapi.webinterface.page.action.WebinterfaceHandler;
import me.mrletsplay.webinterfaceapi.webinterface.page.action.WebinterfaceRequestEvent;
import me.mrletsplay.webinterfaceapi.webinterface.page.action.WebinterfaceResponse;
import me.mrletsplay.webinterfaceapi.webinterface.page.element.WebinterfaceSettingsPane;

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
		return WebinterfaceSettingsPane.handleSetSettingRequest(Webinterface.getConfig(), event);
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
	
	@WebinterfaceHandler(requestTarget = "webinterface", requestTypes = "passwordLogin")
	public WebinterfaceResponse passwordLogin(WebinterfaceRequestEvent event) {
		if(!Webinterface.getConfig().getSetting(DefaultSettings.ENABLE_PASSWORD_AUTH))
			return WebinterfaceResponse.error("Password login is disabled");
		String username = event.getRequestData().getString("username");
		String password = event.getRequestData().getString("password");
		WebinterfaceAccount acc = Webinterface.getAccountStorage().getAccountByConnectionSpecificID("password", username);
		if(acc == null) return WebinterfaceResponse.error("Invalid username");
		if(!Webinterface.getCredentialsStorage().checkCredentials(username, password)) return WebinterfaceResponse.error("Invalid password");
		// NONBETA: Send token to be used to log in
		return WebinterfaceResponse.success();
	}
	
}
