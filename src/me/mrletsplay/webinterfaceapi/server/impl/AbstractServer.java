package me.mrletsplay.webinterfaceapi.server.impl;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

import me.mrletsplay.webinterfaceapi.server.Server;
import me.mrletsplay.webinterfaceapi.server.ServerException;
import me.mrletsplay.webinterfaceapi.server.connection.Connection;
import me.mrletsplay.webinterfaceapi.server.connection.ConnectionAcceptor;

public abstract class AbstractServer implements Server {
	
	private static final Logger LOGGER = Logger.getLogger(AbstractServer.class.getPackage().getName());

	private AbstractServerConfiguration configuration;
	private ServerSocket socket;
	private ConnectionAcceptor acceptor;
	private ExecutorService executor;
	
	public AbstractServer(AbstractServerConfiguration configuration) {
		this.configuration = configuration;
	}
	
	protected ServerSocket createSocket() throws UnknownHostException, IOException {
		return new ServerSocket(configuration.getPort(), 50, InetAddress.getByName(configuration.getHost()));
	}
	
	@Override
	public void start() {
		try {
			socket = createSocket();
			socket.setSoTimeout(1000);
			executor = Executors.newCachedThreadPool();
			executor.execute(() -> {
				while(!executor.isShutdown()) {
					try {
						acceptConnection();
					}catch(SocketTimeoutException ignored) {
					}catch(Exception e) {
						AbstractServer.getLogger().log(Level.FINE, "Error while accepting connection", e);
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

	/**
	 * @see #getConfiguration()
	 * @return The configured host of this server
	 */
	@Deprecated
	@Override
	public String getHost() {
		return configuration.getHost();
	}
	
	/**
	 * @see #getConfiguration()
	 * @return The configured port of this server
	 */
	@Deprecated
	@Override
	public int getPort() {
		return configuration.getPort();
	}

	public AbstractServerConfiguration getConfiguration() {
		return configuration;
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
	
	public static Logger getLogger() {
		return LOGGER;
	}

}
