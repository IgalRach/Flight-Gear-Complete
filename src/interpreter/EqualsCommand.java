package interpreter;

public class EqualsCommand implements Command {

	@Override
	public void execute(String[] line) {
		String[] temp = line[0].split("=");
		if (temp[1].contains("+")) {
			String delimiter = "\\+";
			String[] tempSplitted = temp[1].split(delimiter);
			double sum = Double.parseDouble(tempSplitted[1]) + Interpreter.symTable.get(tempSplitted[0]);
			Interpreter.symTable.put(temp[0], sum);
		} else
			Interpreter.symTable.put(temp[0], Double.parseDouble(temp[1]));
	}

}
