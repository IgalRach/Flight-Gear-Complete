package interpreter;

import java.util.ArrayList;
import java.util.List;

public class SituationParser implements Command {

	List<String[]> commands;
	boolean sit;
	boolean whileFlag;
	String[] sitLine;

	public SituationParser() {
		commands = new ArrayList<>();
	}

	private boolean update(String[] line) { // while throttle > 0 {
		boolean cond = false;
		String operator = line[2];
		Double x = Interpreter.symTable.get(line[1]); // val of var
		if (x == null)
			return false;
		if (operator.equals(">") || operator.equals("<") || operator.equals("<=") || operator.equals(">=")
				|| operator.equals("==") || operator.equals("!=")) {
			switch (operator) {
			case "<":
				cond = x < Integer.parseInt(line[3]);
				break;
			case ">":
				cond = x > Integer.parseInt(line[3]);
				break;
			case "<=":
				cond = x <= Integer.parseInt(line[3]);
				break;
			case ">=":
				cond = x >= Integer.parseInt(line[3]);
				break;
			case "==":
				cond = x == Integer.parseInt(line[3]);
				break;
			case "!=":
				cond = x != Integer.parseInt(line[3]);
				break;
			}
		}
		return cond;
	}

	@Override
	public void execute(String[] line) {
		if (line[0].equals("while") || line[0].equals("if")) { // while throttle > 0 {
			sitLine = line;
			whileFlag = line[0].equals("while");
			sit = update(sitLine);
		} else {
			if (!line[0].equals("}")) {
				String firstWord = line[0].substring(1, line[0].length());
				line[0] = firstWord;
			}
			commands.add(line);
		}
		if (line[0].equals("}") && sit) {
			if (sit && whileFlag) {
				while (sit) {
					commands.forEach(lines -> {
						StringBuilder sb = new StringBuilder();
						if (lines.length <= 3) {
							for (int i = 0; i < lines.length; i++)
								sb.append(lines[i] + " ");
							sb.substring(0, sb.length() - 1);

							String[] temp = new String[1];
							temp[0] = sb.toString();
							new Parser().parse(temp);
							sit = update(sitLine);
						} else {
							AlgebraicParser.parse(lines);
							sit = update(sitLine);
						}
					});
				}

			} else {
				commands.forEach(lines -> {
					StringBuilder sb = new StringBuilder();
					if (lines.length <= 3) {
						for (int i = 0; i < lines.length; i++)
							sb.append(lines[i] + " ");
						sb.substring(0, sb.length() - 1);

						String[] temp = new String[1];
						temp[0] = sb.toString();
						new Parser().parse(temp);
					} else {
						AlgebraicParser.parse(lines);
					}
				});

			}
		}

	}
}
