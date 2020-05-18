package me.mrletsplay.webinterfaceapi.server;

import java.util.concurrent.ExecutorService;

import me.mrletsplay.webinterfaceapi.server.connection.ConnectionAcceptor;

public interface Server {

	public void start();
	
	public boolean isRunning();
	
	public void setConnectionAcceptor(ConnectionAcceptor acceptor) throws IllegalStateException;
	
	public ConnectionAcceptor getConnectionAcceptor();
	
	public String getHost();
	
	public int getPort();
	
	public void setExecutor(ExecutorService executor) throws IllegalStateException;
	
	public ExecutorService getExecutor();
	
	public void shutdown();
	
}
