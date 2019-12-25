package me.mrletsplay.webinterfaceapi.webinterface;
import me.mrletsplay.mrcore.json.JSONArray;
import me.mrletsplay.mrcore.json.JSONObject;
import me.mrletsplay.mrcore.misc.Complex;
import me.mrletsplay.mrcore.misc.NullableOptional;
import me.mrletsplay.webinterfaceapi.webinterface.auth.WebinterfaceAccount;
import me.mrletsplay.webinterfaceapi.webinterface.config.WebinterfaceConfig;
import me.mrletsplay.webinterfaceapi.webinterface.config.setting.WebinterfaceSetting;
import me.mrletsplay.webinterfaceapi.webinterface.page.action.WebinterfaceActionHandler;
import me.mrletsplay.webinterfaceapi.webinterface.page.action.WebinterfaceHandler;
import me.mrletsplay.webinterfaceapi.webinterface.page.action.WebinterfaceRequestEvent;
import me.mrletsplay.webinterfaceapi.webinterface.page.action.WebinterfaceResponse;

public class DefaultHandler implements WebinterfaceActionHandler {
	
	@WebinterfaceHandler(requestTarget = "webinterface", requestTypes = "restart")
	public WebinterfaceResponse lol(WebinterfaceRequestEvent event) {
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

	@WebinterfaceHandler(requestTarget = "webinterface", requestTypes = "setSetting")
	public WebinterfaceResponse setSetting(WebinterfaceRequestEvent event) {
		if(!event.getAccount().hasPermission(DefaultPermissions.SETTINGS))
			return WebinterfaceResponse.error("No permission");
		JSONArray keyAndValue = event.getRequestData().getJSONArray("value");
		WebinterfaceConfig cfg = Webinterface.getConfig();
		WebinterfaceSetting<?> set = cfg.getSetting(keyAndValue.getString(0));
		setSetting(set, keyAndValue.get(1));
		return WebinterfaceResponse.success();
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
	
	private <T> void setSetting(WebinterfaceSetting<T> setting, Object value) {
		Webinterface.getConfig().setSetting(setting, setting.getType().cast(value, this::jsonCast).get());
	}
	
	private <T> NullableOptional<T> jsonCast(Object o, Class<T> typeClass, Complex<?> exactClass) {
		if(o == null) return NullableOptional.of(null);
		if(typeClass.isInstance(o)) return NullableOptional.of(typeClass.cast(o));
		if(Number.class.isAssignableFrom(typeClass)) {
			if(!(o instanceof Number)) return NullableOptional.empty();
			Number n = (Number) o;
			if(typeClass.equals(Integer.class)) {
				return NullableOptional.of(typeClass.cast(n.intValue()));
			}else if(typeClass.equals(Double.class)) {
				return NullableOptional.of(typeClass.cast(n.doubleValue()));
			}
		}
		return NullableOptional.empty();
	}
	
	
}
