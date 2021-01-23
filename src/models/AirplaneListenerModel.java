package models;

import java.awt.Point;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.Observable;

import server.Server;

public class AirplaneListenerModel extends Observable implements Server {

	int port;
	volatile boolean stop;
	volatile Point airplanePosition;
	public double startX;
	public double startY;

	public AirplaneListenerModel(int port) {
		this.port = port;
		stop = false;
	}
	@Override
	public void stop() {
		this.stop = true;
	}

	public Point getAirplanePosition() {
		return airplanePosition;
	}

	public void setStartX(double startX) {
		this.startX = startX;
	}

	public void setStartY(double startY) {
		this.startY = startY;
	}

	@Override
	public void start() {
		new Thread(() -> {
			try {
				runServer();
			} catch (Exception e) {
				stop = true;
			}
		}).start();
	}



	private void runServer() throws Exception {
		ServerSocket server = new ServerSocket(port);
		server.setSoTimeout(500000);

		while (!stop) {
			try {
				Socket aClient = server.accept();
				System.out.println("Client has connected");
				try {
					BufferedReader userInput = new BufferedReader(new InputStreamReader(aClient.getInputStream()));
					while (true) {
						String[] cooInput = userInput.readLine().split(",");
						airplanePosition = new Point();
						airplanePosition.setLocation(Integer.parseInt(cooInput[0]), Integer.parseInt(cooInput[1]));
						System.out.println(airplanePosition);
						setChanged();
						notifyObservers();
					}
				} catch (IOException e) {
					e.printStackTrace();
				} finally {
					server.close();
				}
			} catch (SocketTimeoutException e) {
				e.printStackTrace();
			}
		}
	}

}
