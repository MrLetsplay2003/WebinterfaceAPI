package me.mrletsplay.webinterfaceapi.server.connection;

import java.net.Socket;
import java.util.Collection;

import me.mrletsplay.webinterfaceapi.server.Server;
import me.mrletsplay.webinterfaceapi.server.ServerException;
import me.mrletsplay.webinterfaceapi.webinterface.Webinterface;

public interface ConnectionAcceptor {
	
	public Connection createConnection(Server server, Socket socket);

	public void accept(Connection connection);
	
	public Collection<? extends Connection> getActiveConnections();
	
	public default void closeAllConnections() {
		for(Connection c : getActiveConnections()) {
			try {
				c.close();
			} catch(ServerException e) {
				Webinterface.getLogger().debug("Error while closing connection", e);
			}
		}
	}
	
}
