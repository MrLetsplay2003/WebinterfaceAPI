package me.mrletsplay.webinterfaceapi.auth;

public interface CredentialsStorage {

	public void initialize();

	public void storeCredentials(String authMethod, String id, String plainCredentials);

	public void deleteCredentials(String authMethod, String id);

	public boolean checkCredentials(String authMethod, String id, String plainCredentials);

}
