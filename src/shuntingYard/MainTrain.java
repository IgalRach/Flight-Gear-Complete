package shuntingYard;

public class MainTrain {

	public static void main(String[] args) {
		if(!Expression.class.isAssignableFrom(Number.class))
			System.out.println("Number is not an Expression (-5)"); // -5
		if(!Expression.class.isAssignableFrom(BinaryExpression.class))
			System.out.println("BinaryExpression is not an Expression (-5)"); // -5
		
		if(!Expression.class.isAssignableFrom(Plus.class))
			System.out.println("Plus is not an Expression (-5)"); // -5
		if(!BinaryExpression.class.isAssignableFrom(Plus.class))
			System.out.println("Plus is not a BinaryExpression (-5)"); // -5
		
		try {
			BinaryExpression.class.getDeclaredField("left");
			BinaryExpression.class.getDeclaredField("right");
		} catch (NoSuchFieldException | SecurityException e) {
			System.out.println("BinaryExpression does not have correct fields (-10)"); // -10
		}
		
		System.out.println(Q2.calc());
		
		System.out.println("done");
	}

}
