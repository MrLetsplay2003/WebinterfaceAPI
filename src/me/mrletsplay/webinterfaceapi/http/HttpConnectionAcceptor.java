package me.mrletsplay.webinterfaceapi.http;

import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import me.mrletsplay.webinterfaceapi.server.Server;
import me.mrletsplay.webinterfaceapi.server.connection.Connection;
import me.mrletsplay.webinterfaceapi.server.connection.ConnectionAcceptor;

public class HttpConnectionAcceptor implements ConnectionAcceptor {

	private List<HttpConnection> connections;
	
	public HttpConnectionAcceptor() {
		this.connections = new ArrayList<>();
	}
	
	@Override
	public HttpConnection createConnection(Server server, Socket socket) {
		return new HttpConnection((HttpServer) server, socket);
	}

	@Override
	public void accept(Connection connection) {
		HttpConnection con = (HttpConnection) connection;
		connections.add(con);
		con.startRecieving();
	}

	@Override
	public List<HttpConnection> getActiveConnections() {
		return connections;
	}

}
