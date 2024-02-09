//Carmen Vargas
public class CalculatorTest {

	public static void main(String[] args) {
		
		//Test 1 : Parentheses, exponents, rational numbers and exponents
		//Statement 1: define x = 3.9 + 2^3
		//Statement 2: (7.99*2)^(2/3)+3.67*(4.58*x + 2)
		//Statement 3: 4.78+9^2*(23.67-13.67)
		String str = "(7.99*2)^(2/3)+3.67*(4.58 + 2); 4.78+9^2*(23.67-13.67);";
		Calculator statement1 = new Calculator(str);
		statement1.calculate();
		
		System.out.println(""); System.out.println("");
		
		//Test 2 : Identifier and expressions containing identifiers
		//Statement 1: define xy5z = 5.4 + 7
		//Statement 2: 5xy5z + 8.98
		String str2 = "define xy5z = 5.4 + 7; 5*xy5z + 8.98;";
		Calculator statement2 = new Calculator(str2);
		statement2.calculate();
		
		System.out.println(""); System.out.println("");
		
		//Test 3: Negative numbers
		//Statement 1: define a2 = -2
		//Statement 2: a2 + 3 * a2^3 - (-5.8 - a2)
		String str3 = "define a2 = -2; a2 + 3 * a2^3 - (-5.8 - a2);";
		Calculator statement3 = new Calculator(str3);
		statement3.calculate();
		
		System.out.println(""); System.out.println("");
		
		//Test 3: Multiple Identifiers
		//Statement 1: define xyz123 = 3 + 4 * (2 - 3^0)
		//Statement 2: define a2a = 7.1 - 3.4
		//Statement 3: define myVariable = 10
		//Statement 4: xyz123 + a2a * (myVariable ^ 2)
		String str4 = "define xyz123 = 3 + 4 * (2 - 3^0); define a2a = 7.1 - 3.4; define myVariable = 10;xyz123 + a2a * (myVariable ^ 2);";
		Calculator statement4 = new Calculator(str4);
		statement4.calculate();
		System.out.println("Identifier Hashtable = " + statement4.getIdentifiers());
		
	}

}
