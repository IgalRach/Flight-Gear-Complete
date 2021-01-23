package server;

public class ServerMain {

	public static void main(String[] args) {
		Server s = new MySerialServer(1234, new MyClientHandler());
		s.start();
		System.out.println("Server is live and running");
	}
}
