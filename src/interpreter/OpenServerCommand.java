package interpreter;

public class OpenServerCommand implements Command {
	DataReaderServer drs;
	public static Object notifier = new Object();

	@Override
	public void execute(String[] line) {
		int port = Integer.parseInt(line[0]);
		int latency = Integer.parseInt(line[1]);
		try {
			if (line.length != 2)
				throw new Exception("Parameters are not correct");
			if (port < 1000 || port > 9999)
				throw new Exception("Port is not correct");
		} catch (Exception e) {
		}
		drs = new DataReaderServer(port, latency);
		new Thread(() -> drs.runServer()).start();
		synchronized (notifier) {
			try {
				notifier.wait();
			} catch (InterruptedException e) {
			}
		}
	}

}
