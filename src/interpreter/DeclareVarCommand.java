package interpreter;

public class DeclareVarCommand implements Command {

	@Override
	public void execute(String[] line) {

		if (line.length == 4) {
			if (!Interpreter.symTable.containsKey(line[3])) {
				StringBuilder sb = new StringBuilder(line[3]);
				Interpreter.varToPath.put(line[0], sb.substring(1, sb.length() - 1).toString());
				Interpreter.symTable.put(line[0], 0.0);
			} else {
				Interpreter.symTable.put(line[0], Interpreter.symTable.get(line[3]));
				Interpreter.varToPath.put(line[0], line[3]);
			}
		}
		if (line.length == 3) {
			if (Interpreter.symTable.containsKey(line[2]))
				Interpreter.symTable.put(line[0], Interpreter.symTable.get(line[2]));
			else {
				Interpreter.symTable.put(line[0], Double.parseDouble(line[2]));
			}
		} else {
			if (line.length == 1) {
				if (line[0].contains("=")) {
					Command ec = new EqualsCommand();
					ec.execute(line);
				} else
					Interpreter.symTable.put(line[0], 0.0);
			}
		}
	}

}
