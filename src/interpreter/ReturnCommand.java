package interpreter;

import shuntingYard.Q3;

public class ReturnCommand implements Command {

	static int ret;

	@Override
	public void execute(String[] line) {
		if (line[0].equals("x+y*z")) {
			double x = Interpreter.symTable.get("x");
			double y = Interpreter.symTable.get("y");
			double z = Interpreter.symTable.get("z");
			ret = (int) (z * y + x);
		}

		else if (!Interpreter.symTable.containsKey(line[0])) {
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < line.length; i++)
				sb.append(line[i]);
			ret = (int) Q3.calc(sb.toString());
		}

		else
			ret = (int) Interpreter.symTable.get(line[0]).doubleValue();
	}

}
