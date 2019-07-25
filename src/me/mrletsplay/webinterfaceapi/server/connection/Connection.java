package me.mrletsplay.webinterfaceapi.server.connection;

import java.io.IOException;
import java.net.Socket;

import me.mrletsplay.webinterfaceapi.server.Server;
import me.mrletsplay.webinterfaceapi.server.ServerException;

public interface Connection {
	
	public void startRecieving();
	
	public Server getServer();
	
	public Socket getSocket();
	
	public default void close() {
		try {
			getSocket().close();
		} catch (IOException e) {
			throw new ServerException("Error while closing the connection", e);
		}
	}
	
	public default boolean isSocketAlive() {
		return !getSocket().isClosed() && getSocket().isConnected() && getSocket().isBound() && !getSocket().isInputShutdown() && !getSocket().isOutputShutdown();
	}

}
