package shuntingYard;

public class Q2 {
	public static double calc() {
		return new Plus(new Div(new Number(10), new Number(2)),
				new Mul(new Number(2), new Minus(new Number(3), new Number(4)))).calculate();

	}
}
