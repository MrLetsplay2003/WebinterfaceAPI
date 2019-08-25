package me.mrletsplay.webinterfaceapi.server.impl;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import me.mrletsplay.webinterfaceapi.server.Server;
import me.mrletsplay.webinterfaceapi.server.ServerException;
import me.mrletsplay.webinterfaceapi.server.connection.Connection;
import me.mrletsplay.webinterfaceapi.server.connection.ConnectionAcceptor;

public abstract class AbstractServer implements Server {

	private int port;
	private ServerSocket socket;
	private ConnectionAcceptor acceptor;
	private ExecutorService executor;
	
	public AbstractServer(int port) {
		this.port = port;
		this.executor = Executors.newCachedThreadPool(); // TODO: executor error handling
	}
	
	@Override
	public void start() {
		try {
			this.socket = new ServerSocket(port);
			socket.setSoTimeout(1000);
			executor.execute(() -> {
				while(!executor.isShutdown()) {
					try {
						acceptConnection();
					}catch(SocketTimeoutException ignored) {
					}catch(Exception e) {
						e.printStackTrace(); // TODO: ?
					}
				}
			});
		} catch (IOException e) {
			throw new ServerException("Error while starting server", e);
		}
	}
	
	private void acceptConnection() throws IOException {
		Socket s = socket.accept();
		if(executor.isShutdown()) return;
		Connection con = acceptor.createConnection(this, s);
		acceptor.accept(con);
	}
	
	@Override
	public boolean isRunning() {
		return socket != null && !socket.isClosed();
	}

	@Override
	public void setConnectionAcceptor(ConnectionAcceptor acceptor) throws IllegalStateException {
		if(isRunning()) throw new IllegalStateException("Server is running");
		this.acceptor = acceptor;
	}

	@Override
	public ConnectionAcceptor getConnectionAcceptor() {
		return acceptor;
	}
	
	@Override
	public int getPort() {
		return port;
	}
	
	@Override
	public void setExecutor(ExecutorService executor) throws IllegalStateException {
		if(isRunning()) throw new IllegalStateException("Server is running");
		this.executor = executor;
	}
	
	@Override
	public ExecutorService getExecutor() {
		return executor;
	}

	@Override
	public void shutdown() {
		try {
			executor.shutdown();
			try {
				if(!executor.awaitTermination(5L, TimeUnit.SECONDS)) executor.shutdownNow();
			}catch(InterruptedException e) {
				throw new ServerException("Error while stopping executor", e);
			}
			acceptor.closeAllConnections();
			if(socket != null) socket.close();
		} catch (IOException e) {
			throw new ServerException("Error while stopping server", e);
		}
	}

}
