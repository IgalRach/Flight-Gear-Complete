package interpreter;

public class SleepCommand implements Command {

	@Override
	public void execute(String[] line) {
		try {
			Thread.sleep(Integer.parseInt(line[0]));
		} catch (NumberFormatException e) {
		} catch (InterruptedException e) {
		}
	}

}