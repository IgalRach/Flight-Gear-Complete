package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;

public class MySerialServer implements Server {

	volatile boolean stop;
	int port;
	ClientHandler ch;

	public MySerialServer(int port, ClientHandler ch) {
		this.port = port;
		this.ch = ch;
		stop = false;
	}

	public MySerialServer(int port) {
		this.port = port;
		stop = false;
	}

	public void setClientHandler(ClientHandler ch) {
		this.ch = ch;
	}

	@Override
	public void start() {
		new Thread(() -> {
			try {
				runServer();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}).start();
	}

	@Override
	public void stop() {
		this.stop = true;
	}

	private void runServer() throws Exception {
		ServerSocket server = new ServerSocket(port);
		server.setSoTimeout(5000000);
		while (!stop) {
			try {
				Socket aClient = server.accept();
				try {
					System.out.println("Established connection with client");
					while (aClient.isConnected()) {
						ch.handleClient(aClient.getInputStream(), aClient.getOutputStream());
					}
					stop = true;
					aClient.close();
				} catch (IOException e) {
					server.close();
				}
			} catch (SocketTimeoutException e) {
				e.printStackTrace();
			}

		}
		server.close();
	}
}
