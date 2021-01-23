package models;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Observable;

public class ConnectModel extends Observable {
	
	PrintWriter outSim;

	public void connect(String ip, int port) {
		Socket server = null;
		try {
			server = new Socket(ip, port);
			System.out.println("Client is connected to a remote Server.");
			outSim = new PrintWriter(server.getOutputStream());
		} catch (IOException e) {}
	}

	public void sendCommandToSimulator(String cmd, double new_val) {
		if (outSim != null) {
			outSim.println(cmd + new_val);
			outSim.flush();
		}
		else {
			System.out.println("you are not connected to the simulator.");
		}
	}

}
