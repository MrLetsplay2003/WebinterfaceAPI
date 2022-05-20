package me.mrletsplay.webinterfaceapi.auth;

public interface CredentialsStorage {
	
	public void initialize();

	public void storeCredentials(String id, String plainCredentials);
	
	public boolean checkCredentials(String id, String plainCredentials);
	
}
