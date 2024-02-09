//Carmen Vargas
import java.util.Hashtable;
import java.math.*;
public class Calculator {
	
	/**
	 * RULES FOR INPUT
	 * 
	 * :: SEMICOLONS ::
	 * Multiple statements can be passed as a single string; they MUST be separated by semicolons.
	 * Carriage Returns \n will not compute.
	 * All string inputs must end with a semicolon as well.
	 * 
	 * :: SPACES ::
	 * Spaces are allowed. They will be automatically removed before being parsed
	 * 
	 * :: OPERATIONS ::
	 * All operations must be explicitly written, in particular multiplication.
	 * 		- Between numbers and variables (identifiers)
	 * 		- Between a number and an expression in parentheses
	 * 		- Between a variable (identifier) and an expression in parentheses
	 * 		- Between variables (identifiers)
	 * 
	 * :: POWERS ::
	 * All exponents consisting of more than a set of digits must be encapsulated in parentheses.
	 * 
	 * THANK YOU!
	 * 
	 **/
	
	//**************
	// Data Fields
	//**************
	private String expression;
	private int position;
	private Hashtable<String, BigDecimal> identifiers;
	
	//***********************************************************************
	//		Constructor
	//***********************************************************************
	public Calculator(String input)
	{
		this.expression = input;
		this.position = 0;
		this.identifiers = new Hashtable<String, BigDecimal>();
	}
	
	//***********************************************************************
	//		Get Identifier Hashtable
	//***********************************************************************
	public String getIdentifiers()
	{
		return identifiers.toString();
	}
	
	//***********************************************************************
	//		Helper Methods
	//***********************************************************************
	
	public char peek()
	{
		try
		{
			if(position >= expression.length())
			{
				return '\0';
			} else
			{
				char c = expression.charAt(position);
				return c;
			}
		}
		catch(Exception e)
		{
			System.out.println("ERROR: " + e);
			return '\0';
		}
	}
	
	public char addChar()
	{
		try
		{
			char c = expression.charAt(position);
			position++;
			return c;
		}
		catch(Exception e)
		{
			System.out.println("ERROR: " + e);
			return '\0';
		}
	}
	
	//***********************************************************************
	//		Number / Identifier Method : Parse Number / Identifier
	//***********************************************************************
	public BigDecimal parseNumber(StringBuilder inputNumber)
	{
		try
		{
			BigDecimal number;
			boolean isNumber = true;
			int i = 0;
			if(inputNumber.charAt(0) == '-')
			{
				i = 1;
			}
			for(; i < inputNumber.length(); i++)
			{
				if(!Character.isDigit(inputNumber.charAt(i)) && inputNumber.charAt(i) != '.')
				{
					isNumber = false;
				}
			}
			if(isNumber)
			{
				number = new BigDecimal(inputNumber.toString());
				return number;
			} else
			{
				System.out.println("ERROR: Invalid number - most likely missing an operation after number.");
				return null;
			}
		}
		catch(Exception e)
		{
			System.out.println("ERROR: Invalid number - " + e);
			return null;
		}
	}
	
	public BigDecimal parseIdentifier(StringBuilder inputVar)
	{
		try
		{
			BigDecimal variable;
			if(identifiers.containsKey(inputVar.toString()))
			{
				variable = identifiers.get(inputVar.toString());
				return variable;
			} else
			{
				System.out.println("Identifier " + inputVar + " is unknown.");
				return null;
			}
		}
		catch(Exception e)
		{
			System.out.println("ERROR: Invalid identifier - " + e);
			return null;
		}
	}

	//***********************************************************************
	//		Power Method : Parse Exponentials
	//***********************************************************************
	public BigDecimal precisePower(double power)
	{
		try
		{
			StringBuilder strPower = new StringBuilder(Double.toString(power));
			strPower.delete(strPower.length()-1, strPower.length());
			BigDecimal newPower = new BigDecimal(strPower.toString());
			newPower.stripTrailingZeros();
			return newPower;
		}
		catch(Exception e)
		{
			System.out.println("ERROR: " + e);
			return null;
		}
	}
	
	public BigDecimal parsePower(StringBuilder inputPower)
	{
		try
		{
			StringBuilder left = new StringBuilder();
			StringBuilder right = new StringBuilder();
			boolean isNeg = false;
			int powerOp = inputPower.indexOf("^");
			
			// Gather left and right numbers of ^ operation
			for(int i = 0; i < inputPower.length(); ++i)
			{
				if(i < powerOp)
				{
					left.append(inputPower.charAt(i));
				}
				if(i > powerOp)
				{
					if(inputPower.charAt(i) == '-')
					{
						isNeg = true;
					} else
					{
						right.append(inputPower.charAt(i));
					}
				}
			}
			
			// Check if left/right is a variable or number
			// Convert left and right strings to numbers
			BigDecimal leftNum;
			BigDecimal rightNum;
			if(Character.isLetter(left.charAt(0)))
			{
				leftNum = parseIdentifier(left);
			} else 
			{
				leftNum = parseNumber(left);
			}
			if(Character.isLetter(right.charAt(0)))
			{
				rightNum = parseIdentifier(right);
			} else
			{
				rightNum = parseNumber(right);
			}
			
			// Perform power
			// Note: if exponent is negative, divide one by poewr
			if(leftNum == null || rightNum == null)
			{
				return null;
			} else
			{
				if(isNeg)
				{
					double dDenominator = Math.pow(leftNum.doubleValue(), rightNum.doubleValue());
					BigDecimal denominator = precisePower(dDenominator);
					BigDecimal one = new BigDecimal(1.0);
					denominator = one.divide(denominator, 15, RoundingMode.CEILING);
					denominator = denominator.stripTrailingZeros();
					return denominator;
				} else
				{
					double numPower = (Math.pow(leftNum.doubleValue(), rightNum.doubleValue()));
					BigDecimal power = precisePower(numPower);
					power = power.stripTrailingZeros();
					return power;
				}
			}
			
		}
		catch(Exception e)
		{
			System.out.println("ERROR: Invalid power - " + e);
			return null;
		}
	}
	
	
	//***********************************************************************
	//		Factor Method : Parse Factor
	//***********************************************************************
	public BigDecimal parseFactor(StringBuilder inputFactor)
	{
		try
		{
			StringBuilder numStr = new StringBuilder();
			BigDecimal factor = new BigDecimal(0.0);
			char operation = '\0';
			
			//If parentheses detected: parse parentheses and return value
			if(inputFactor.charAt(0) == '(')
			{
				for(int i = 0 + 1; i < inputFactor.length(); i++)
				{
					if(inputFactor.charAt(i) == ')')
					{
						break;
					}
					numStr.append(inputFactor.charAt(i));
				}
				factor = factor.add(parseExp(numStr));
				return factor;
			}
			// If power operator detected: parse power and return value
			else if(inputFactor.indexOf("^") > -1) 
			{
				factor = parsePower(inputFactor);
				return factor;
			} 
			// If variable detected: search for variable in hashtable and return value
			else if(Character.isLetter(inputFactor.charAt(0)))
			{
				factor = parseIdentifier(inputFactor);
				return factor;
			}
			// Else, factor is a number: parse number and return value
			else
			{
				factor = parseNumber(inputFactor);
				return factor;
			}
		}
		catch(Exception e)
		{
			System.out.println("ERROR: Invalid factor - " + e);
			return null;
		}
	}
	
	//***********************************************************************
	//		Term Method : Parse Term
	//***********************************************************************
	public BigDecimal parseTerm(StringBuilder term)
	{
		try
		{
			StringBuilder termFactor = new StringBuilder();
			BigDecimal termResult = new BigDecimal(0.0);
			int termPosition = 0;
			char operation = '\0';
			
			while(termPosition < term.length())
			{
				//Assign operation when termPosition != 0
				if(termPosition != 0)
				{
					operation = term.charAt(termPosition);
					termPosition++;
				}
				
				//Gather factor before * or / operation
				for(int i = termPosition; i < term.length(); ++i)
				{
					if(term.charAt(i) == '(')
					{
						if(i - 1 >= 0)
						{
							char opCheck = term.charAt(i-1);
							if(opCheck != '*' && opCheck != '/' && opCheck != '^')
							{
								System.out.println("ERROR: Parentheses must be preceeded by either '*', '/', or '^' \n   if performing an operation with contents of parentheses.");
								return null;
							}
						}
						StringBuilder parentheses = new StringBuilder();
						for(int j = i; j < term.length(); ++j)
						{
							if(term.charAt(j) == ')')
							{
								parentheses.append(term.charAt(j));
								i = j;
								termPosition = j;
								break;
							}
							parentheses.append(term.charAt(j));
						}
						termFactor.append(parseFactor(parentheses).toString());
					} else if(term.charAt(i) == '*' || term.charAt(i) == '/')
					{
						break;
					} else
					{
						termFactor.append(term.charAt(i));
						termPosition++;
					}
					
					if(i == term.length() - 1)
					{
						termPosition = i + 1;
					}
				}
				
				BigDecimal parsedFactor = parseFactor(termFactor);
				termFactor.replace(0, termFactor.length(), "");
				if(parsedFactor != null)
				{
					if(operation == '\0')
					{
						termResult = termResult.add(parsedFactor);
					} else
					{
						if(operation == '*')
						{
							termResult = termResult.multiply(parsedFactor);
						} else if (operation == '/')
						{
							BigDecimal zero = new BigDecimal(0.0);
							if(parsedFactor != zero)
							{

								termResult = termResult.divide(parsedFactor, 15, RoundingMode.CEILING);
								termResult = termResult.stripTrailingZeros();
							} else
							{
								return null;
							}
						} else
						{
							return null;
						}
					}
				} else
				{
					return null;
				}
			}
			return termResult;
		}
		catch(Exception e)
		{
			System.out.println("ERROR: Invalid term - " + e);
			return null;
		}
	}
	
	//***********************************************************************
	//		Expression Method : Parse Expression 
	//***********************************************************************
	public BigDecimal parseExp(StringBuilder inputExp)
	{
		try
		{
			BigDecimal expResult = new BigDecimal(0.0);
			StringBuilder expTerm = new StringBuilder();
			int expPosition = 0;
			char operation = '\0';
			
			while(expPosition < inputExp.length()-1)
			{
				if(expPosition != 0)
				{
					//Operation: + or -
					operation = inputExp.charAt(expPosition);
					expPosition++;
				}
				//Gather term
				int parCount = 0;
				for(int i = expPosition; i < inputExp.length(); ++i)
				{
					if(inputExp.charAt(i) == '(')
					{
						parCount++;
					} else if(inputExp.charAt(i) == ')')
					{
						parCount--;
					}else if(inputExp.charAt(i) == '+' && parCount == 0)
					{
						break;
					} else if(inputExp.charAt(i) == '-' && parCount == 0)
					{
						if((i != 0 && i != (inputExp.length()-1)))
						{
							if(Character.isDigit(inputExp.charAt(i-1)) || Character.isLetter(inputExp.charAt(i-1)) || inputExp.charAt(i-1) == ')')
							{
								if(Character.isDigit(inputExp.charAt(i+1)) || Character.isLetter(inputExp.charAt(i+1)) || inputExp.charAt(i+1) == '(' || inputExp.charAt(i+1) == '-')
								{
									break;
								}
							}
						}
					}
					expTerm.append(inputExp.charAt(i));
					expPosition++;
				}
				
				//If first pass, parse term and add to result "expResult"
				//Otherwise, add or subtract term to expResult
				BigDecimal parsedTerm = parseTerm(expTerm);
				if(parsedTerm != null)
				{
					if(operation == '\0')
					{
						expResult = expResult.add(parsedTerm);
					} else
					{
						if(operation == '+')
						{
							expResult = expResult.add(parsedTerm);
						} else if (operation == '-')
						{
							expResult = expResult.subtract(parsedTerm);
						} else
						{
							return null;
						}
							
					}
				} else
				{
					return null;
				}
				expTerm.replace(0, expTerm.length(), "");
			}
			return expResult;
		}
		catch(Exception e)
		{
			System.out.println("ERROR: Invalid expression - " + e);
			return null;
		}
	}
	
	//***********************************************************************
	//		Identifier Method Set : Parse Identifier and Expression
	//***********************************************************************
	
	//Determine if identifier is valid
	public boolean parseAlphaNumeric(StringBuilder identifier)
	{
		try
		{
			int index = 0;
			if(Character.isLetter(identifier.charAt(index)) == false) {
				return false;
			} else
			{
				for(int i = 0; i < identifier.length(); ++i)
				{
					if(Character.isLetter(identifier.charAt(index))) {
						index++;
					} else if(Character.isDigit(identifier.charAt(index))) {
						index++;
					}
				}
				if(index == identifier.length())
				{
					return true;
				} else
				{
					return false;
				}
			}
		}
		catch(Exception e)
		{
			System.out.println("ERROR: Invalid alphanumeric - " + e);
			return false;
		}
	}
	
	//Parse Identifier and store Variable/Value in Hashtable
	public void parseIdentifier()
	{
		try
		{
			StringBuilder variable = new StringBuilder();
			position += 6;
			while(peek() != '=')
			{
				variable.append(peek());
				position++;
			}
			boolean isValidIdentifier = parseAlphaNumeric(variable);
			if(isValidIdentifier)
			{
				position++;
				
				//Gather expression and pass string to Parse Expression Method
				StringBuilder exp = new StringBuilder();
				while(position < expression.length())
				{
					if(peek() != ';')
					{
						exp.append(addChar());
					} else
					{
						break;
					}
				}
				BigDecimal idExp = parseExp(exp);
				identifiers.put(variable.toString(), idExp);
				System.out.println("Variable Statement: " + exp);
				System.out.println("Variable Statement Calculated: " + variable + " = " + idExp);
			}
			else
			{
				System.out.println("ERROR: Invalid Identifier. Expression/Statements could not be calculated.");
				return;
			}
		} 
		catch (Exception e)
		{
			System.out.println("ERROR: Invalid element in input - " + e);
		}
	}
	
	//***********************************************************************
	//		Calculate Method : Parse String Input 
	//***********************************************************************
	public void calculate()
	{
		try
		{
			//Remove, if any, white spaces in input string at first pass
			if(position == 0)
			{
				expression = expression.replaceAll("\\s", "");
			}
			
			//Determine if a variable is present in statement being evaluated
			//Otherwise, parse expression
			for(int i = 0; position < expression.length(); ++i)
			{
				if(peek() == ';') {
					position++;
				} else if(peek() == 'd' && expression.charAt(position + 5) == 'e')
				{
					parseIdentifier();
				} else
				{
					StringBuilder exp = new StringBuilder();
					while(peek() != ';')
					{
						exp.append(addChar());
					}
					System.out.println("Statement: " + exp);
					BigDecimal calculation = parseExp(exp);
					System.out.println("Statement Calculated: " + calculation);
					exp.replace(0, exp.length(), "");
				}
			}
		}
		catch(Exception e)
		{
			System.out.println("ERROR: Invalid elements in input or missing semicolon. Calculation could not be executed: ");
			System.out.println("ERROR: " + e);
		}
	}
	
	public static void main(String[] args) {
		//Test 5: Dividing by one
				//Statement 1: 14 + 12 / (3 - 2)
				String str5 = "14 + 12 / (3 - 2);";
				Calculator statement5 = new Calculator(str5);
				statement5.calculate();
	}

}
