package interpreter;

import java.util.Arrays;

public class Parser {

	public int parse(String[] textLines) {
		boolean flag = false;
		for (int i = 0; i < textLines.length; i++) {
			String[] parsed = textLines[i].split(" ");

			if (parsed[0].equals("while") || parsed[0].equals("if")) {
				flag = true;
				Interpreter.cp.execute(parsed);
				continue;
			}
			if (flag) {
				if (parsed[0].equals("}")) {
					flag = false;
					Interpreter.cp.execute(parsed);
				}
				Interpreter.cp.execute(parsed);
				continue;
			}

			if (Interpreter.commandMap.containsKey(parsed[0]) == true) {
				Command c = Interpreter.commandMap.get(parsed[0]);
				if (c instanceof OpenServerCommand) {
					String[] subWords = Arrays.copyOfRange(parsed, 1, parsed.length);
					c.execute(subWords);
				} else {
					String[] subWords = Arrays.copyOfRange(parsed, 1, parsed.length);
					c.execute(subWords);
				}

			} else if (Interpreter.symTable.containsKey(parsed[0]) == true) {
				Command cc = new ClientCommand();

				cc.execute(parsed);
			} else if (parsed[0].contains("=")) {
				Command ec = new EqualsCommand();
				ec.execute(parsed);
			}
		}
		return ReturnCommand.ret;
	}
}
