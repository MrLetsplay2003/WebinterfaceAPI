package me.mrletsplay.webinterfaceapi.setup.impl;

import me.mrletsplay.mrcore.json.JSONObject;
import me.mrletsplay.webinterfaceapi.Webinterface;
import me.mrletsplay.webinterfaceapi.auth.Account;
import me.mrletsplay.webinterfaceapi.auth.AccountConnection;
import me.mrletsplay.webinterfaceapi.auth.impl.PasswordAuth;
import me.mrletsplay.webinterfaceapi.setup.SetupStep;

public class AccountSetupStep extends SetupStep {

	public AccountSetupStep() {
		super("admin-account", "Create an Administrator account");
		setDescription("This account has all permissions by default and can be used to make further changes to the configuration");

		addString("username", "Username", null);
		addPassword("password", "Password", null);
		addPassword("password-repeat", "Repeat Password", null);
	}

	@Override
	public String callback(JSONObject data) {
		String
		username = data.getString("username").trim(),
		password = data.getString("password").trim(),
		passwordR = data.getString("password-repeat").trim();

	if(username.isEmpty()) return "Username must be set";
	if(!PasswordAuth.isValidUsername(username)) return "Username contains invalid characters or is too long/short";
	if(password.isEmpty()) return "Password must be set";
	if(!password.equals(passwordR)) return "Passwords don't match";

	Account acc = Webinterface.getAccountStorage().getAccountByConnectionSpecificID(PasswordAuth.ID, username, true);
	if(acc != null) return "Account already exists";

	Webinterface.getCredentialsStorage().storeCredentials(username, password);
	AccountConnection con = new AccountConnection(PasswordAuth.ID, username, username, null, null);
	acc = Webinterface.getAccountStorage().createAccount(con);
	acc.addPermission("*");

	return null;
}

}
