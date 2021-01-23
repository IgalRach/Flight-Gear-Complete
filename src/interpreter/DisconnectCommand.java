package interpreter;

import java.io.IOException;
import java.io.PrintWriter;

public class DisconnectCommand implements Command {
	
	@Override
	public void execute(String[] line) {
		try {
			PrintWriter out = new PrintWriter(ConnectCommand.server.getOutputStream());
			out.println("bye");
			out.flush();
		} catch (IOException e) {}
	}

}
