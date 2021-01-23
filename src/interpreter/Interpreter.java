package interpreter;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class Interpreter {

	public static Map<String, Command> commandMap = new HashMap<>();
	static Map<String, Double> symTable = new HashMap<>();
	static Map<String, String> varToPath = new HashMap<>();
	static Command cp = new SituationParser();

	public Interpreter() {
		Interpreter.commandMap.put("print", new PrintCommand());
		Interpreter.commandMap.put("var", new DeclareVarCommand());
		Interpreter.commandMap.put("openDataServer", new OpenServerCommand());
		Interpreter.commandMap.put("connect", new ConnectCommand());
		Interpreter.commandMap.put("if", new SituationParser());
		Interpreter.commandMap.put("while", new SituationParser());
		Interpreter.commandMap.put("sleep", new SleepCommand());
		Interpreter.commandMap.put("return", new ReturnCommand());
		Interpreter.commandMap.put("disconnect", new DisconnectCommand());
	}

	public int interpret(String[] lines) {
		return new Parser().parse(lines);
	}

	public static String[] lexer(String fileName) {
		try {
			return Files.lines(Paths.get("./resources/" + fileName)).toArray(String[]::new);
		} catch (IOException e) {
		}
		return null;
	}

	public static void main(String[] args) {
		Interpreter i = new Interpreter();
		i.interpret(lexer("inputFromSimulator.txt"));
	}

}
