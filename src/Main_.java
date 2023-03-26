import java.io.*;
import java.util.*;

interface Expr { Object run(HashMap<String, Object> hm); }
interface Condition { boolean test(Expr e1, Expr e2, HashMap<String, Object> hm); }
interface Operator { int count(Expr e1, Expr e2, HashMap<String, Object> hm); }

interface SimpleInstruction { void run(HashMap<String,Object> hm); }

interface WhileInstructionI extends SimpleInstruction {void run(HashMap<String, Object> hm); }
interface IfInstructionI extends SimpleInstruction {void run(HashMap<String, Object> hm); }
   
public class Main {

	private HashMap<String, Object> hm = new HashMap<>();
	private InstructionList instructionList;

	public Main(InstructionList instructionList)
	{
		this.instructionList = instructionList;
	}

	public void exec()
	{
		instructionList.run(hm);
	}

	static public void main(String argv[]) {
		try {
			parser p = new parser(new Lexer(new FileReader(argv[0])));
			Object result = p.parse().value;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}

/** VARS */
class ID implements Expr
{
	String name;

	public ID(String s)
	{
		name = s;
	}

	public Object run(HashMap<String,Object> hm)
	{
		return hm.get(name);
	}
}

class AssignInstruction implements SimpleInstruction
{
	String name;
	Expr val;

	public AssignInstruction(String i, Expr e)
	{
		name = i;
		val = e;
	}

	public void run(HashMap<String, Object> hm)
	{
			hm.put(name, val.run(hm));
	}
}


/** OPERATORS */
class PlusOperator implements Operator {

	public int count(Expr e1, Expr e2, HashMap<String, Object> hm) {

		Object v1 = e1.run(hm);
		Object v2 = e2.run(hm);

		if (v1 instanceof Integer && v2 instanceof Integer) {
			return (Integer)v1 + (Integer)v2;
		} else {
			System.out.println("Error: wrong objects type");
			System.exit(1);
			return 0;
		}
	}
}

class TimesOperator implements Operator {

	public int count(Expr e1, Expr e2, HashMap<String, Object> hm) {
		Object v1 = e1.run(hm);
		Object v2 = e2.run(hm);
		if (v1 instanceof Integer && v2 instanceof Integer) {
			return (Integer)v1 * (Integer)v2;
		} else {
			System.out.println("Error: wrong objects type");
			System.exit(1);
			return 0;
		}
	}
}

class MinusOperator implements Operator {

	public int count(Expr e1, Expr e2, HashMap<String, Object> hm) {
		Object v1 = e1.run(hm);
		Object v2 = e2.run(hm);
		if (v1 instanceof Integer && v2 instanceof Integer) {
			return (Integer)v1 - (Integer)v2;
		} else {
			System.out.println("Error: wrong objects type");
			System.exit(1);
			return 0;
		}
	}
}

class DivideOperator implements Operator {

	public int count(Expr e1, Expr e2, HashMap<String, Object> hm) {

		Object v1 = e1.run(hm);
		Object v2 = e2.run(hm);
		if (v1 instanceof Integer && v2 instanceof Integer) {
			if ((Integer)v2 == 0) {
				System.out.println("Error: division by zero");
				System.exit(1);
			}
			return (Integer)v1 / (Integer)v2;
		} else {
			System.out.println("Error: wrong objects type");
			System.exit(1);
			return 0;
		}
	}
}

class ModeOperator implements Operator {

	public int count(Expr e1, Expr e2, HashMap<String, Object> hm) {
		Object v1 = e1.run(hm);
		Object v2 = e2.run(hm);

		if (v1 instanceof Integer && v2 instanceof Integer) {
			if ((Integer)v2 == 0) {
				System.out.println("Error: division by zero");
				System.exit(1);
			}
			return (Integer)v1 % (Integer)v2;
		} else {
			System.out.println("Error: wrong objects type");
			System.exit(1);
			return 0;
		}
	}
}

class OperatorExpression implements Expr {

	Expr e, e2;
	Operator o;

	public OperatorExpression(Expr e, Operator o, Expr e2)
	{
		this.e = e;
		this.e2 = e2;
		this.o = o;
	}

	public Object run(HashMap<String, Object> hm)
	{
		return o.count(e, e2, hm);
	}
}

/** INT OPERATIONS */
class IntExpression implements Expr
{
	int value;

	public IntExpression(int e)
	{
		value = e;
	}

	public Object run(HashMap<String, Object> hm)
	{
		return value;
	}
}

class IntEnterExpression implements Expr
{
	public Object run(HashMap<String, Object> hm)
	{
		java.util.Scanner in = new java.util.Scanner(System.in);
		return in.nextInt();
	}
}

class PIntExpression implements Expr
{
	Expr expr;

	public PIntExpression(Expr e)
	{
		expr = e;
	}

	public Object run(HashMap<String, Object> hm)
	{
		return expr.run(hm);
	}
}

class UMinusExpression implements Expr
{
	Expr e;

	public UMinusExpression(Expr e) {
		this.e = e;
	}

	public Object run(HashMap<String, Object> hm) {

		Object v = e.run(hm);
		if (v instanceof Integer) {
			return -((Integer)v);
		} else {
			System.out.println("Error: wrong objects type");
			System.exit(1);
			return 0;
		}
	}

}

class STRLengthExpression implements Expr
{
	Expr e;

	public STRLengthExpression(Expr e) {
		this.e = e;
	}

	public Object run(HashMap<String, Object> hm) {

		Object v = e.run(hm);
		if (v instanceof String) {
			return ((String)v).length();
		} else {
			System.out.println("Error: wrong objects type");
			System.exit(1);
			return 0;
		}

	}

}

class STRPositionExpression implements Expr
{
	Expr e, e2;

	public STRPositionExpression(Expr e, Expr e2) {
		this.e = e;
		this.e2 = e2;
	}

	public Object run(HashMap<String, Object> hm) {

		Object v1 = e.run(hm);
		Object v2 = e2.run(hm);

		if (v1 instanceof String && v2 instanceof String) {
			String s = (String)v1;
			String s2 = (String)v2;

			int pos = s.indexOf(s2);
			return (pos != -1) ? pos + 1 : 0;
		} else {
			System.out.println("Error: wrong objects type");
			System.exit(1);
			return 0;
		}
	}

}

/** CONDITIONS */
class EqCond implements Condition
{
	public boolean test(Expr e1, Expr e2, HashMap<String, Object> hm) {

		Object v1 = e1.run(hm);
		Object v2 = e2.run(hm);

		if (v1 instanceof Integer && v2 instanceof Integer) {
			return (Integer)v1 == (Integer)v2;
		} else {
			System.out.println("Error: wrong objects type");
			System.exit(1);
			return false;
		}

	}
}

class LtCond implements Condition
{
	public boolean test(Expr e1, Expr e2, HashMap<String, Object> hm) {

		Object v1 = e1.run(hm);
		Object v2 = e2.run(hm);

		if (v1 instanceof Integer && v2 instanceof Integer) {
			return (Integer)v1 < (Integer)v2;
		} else {
			System.out.println("Error: wrong objects type");
			System.exit(1);
			return false;
		}
	}
}

class LeCond implements Condition
{
	public boolean test(Expr e1, Expr e2, HashMap<String, Object> hm) {

		Object v1 = e1.run(hm);
		Object v2 = e2.run(hm);

		if (v1 instanceof Integer && v2 instanceof Integer) {
			return (Integer)v1 <= (Integer)v2;
		} else {
			System.out.println("Error: wrong objects type");
			System.exit(1);
			return false;
		}
	}
}

class GtCond implements Condition
{
	public boolean test(Expr e1, Expr e2, HashMap<String, Object> hm) {

		Object v1 = e1.run(hm);
		Object v2 = e2.run(hm);

		if (v1 instanceof Integer && v2 instanceof Integer) {
			return (Integer)v1 > (Integer)v2;
		} else {
			System.out.println("Error: wrong objects type");
			System.exit(1);
			return false;
		}
	}
}

class GeCond implements Condition
{
	public boolean test(Expr e1, Expr e2, HashMap<String, Object> hm) {

		Object v1 = e1.run(hm);
		Object v2 = e2.run(hm);

		if (v1 instanceof Integer && v2 instanceof Integer) {
			return (Integer)v1 >= (Integer)v2;
		} else {
			System.out.println("Error: wrong objects type");
			System.exit(1);
			return false;
		}
	}
}

class NeCond implements Condition
{
	public boolean test(Expr e1, Expr e2, HashMap<String, Object> hm) {

		Object v1 = e1.run(hm);
		Object v2 = e2.run(hm);

		if (v1 instanceof Integer && v2 instanceof Integer) {
			return (Integer)v1 != (Integer)v2;
		} else {
			System.out.println("Error: wrong objects type");
			System.exit(1);
			return false;
		}
	}
}

class StrEqCond implements Condition
{
	public boolean test(Expr e1, Expr e2, HashMap<String, Object> hm) {

		Object v1 = e1.run(hm);
		Object v2 = e2.run(hm);

		if (v1 instanceof String && v2 instanceof String) {
			return ((String)v1).equals((String)v2);
		} else {
			System.out.println("Error: wrong objects type");
			System.exit(1);
			return false;
		}
	}
}

class StrNotEqCond implements Condition
{
	public boolean test(Expr e1, Expr e2, HashMap<String, Object> hm) {

		Object v1 = e1.run(hm);
		Object v2 = e2.run(hm);

		if (v1 instanceof String && v2 instanceof String) {
			return !((String)v1).equals((String)v2);
		} else {
			System.out.println("Error: wrong objects type");
			System.exit(1);
			return false;
		}
	}
}

/** BOOLEAN OPERATIONS */
class BooleanExpression implements Expr
{
	Boolean value;

	public BooleanExpression(Boolean e)
	{
		value = e;
	}

	public Object run(HashMap<String, Object> hm)
	{
		return value;
	}
}

class ConditionBooleanExpression implements Expr{

	Expr e, e2;
	Condition c;

	public ConditionBooleanExpression(Expr e, Condition c, Expr e2)
	{
		this.e = e;
		this.c = c;
		this.e2 = e2;
	}

	public Object run(HashMap<String, Object> hm)
	{
		return c.test(e, e2, hm);
	}
}

class PBooleanExpression implements Expr
{
	Expr expr;

	public PBooleanExpression(Expr e)
	{
		expr = e;
	}

	public Object run(HashMap<String, Object> hm)
	{
		return expr.run(hm);
	}
}

class NegationBooleanExpression implements Expr
{
	Expr expr;

	public NegationBooleanExpression(Expr e)
	{
		expr = e;
	}

	public Object run(HashMap<String, Object> hm)
	{
		return !((Boolean)expr.run(hm));
	}
}

class AndBooleanExpression implements Expr
{
	Expr expr, expr2;

	public AndBooleanExpression(Expr e, Expr e2)
	{
		expr = e;
		expr2 = e2;
	}

	public Object run(HashMap<String, Object> hm)
	{
		return (Boolean)expr.run(hm) && (Boolean)expr2.run(hm) ;
	}
}

class OrBooleanExpression implements Expr
{
	Expr expr, expr2;

	public OrBooleanExpression(Expr e, Expr e2)
	{
		expr = e;
		expr2 = e2;
	}

	public Object run(HashMap<String, Object> hm)
	{
		return (Boolean)expr.run(hm) || (Boolean)expr2.run(hm);
	}
}



/** STRING OPERATIONS*/

class StringExpression implements Expr
{
	String value;

	public StringExpression(String e)
	{
		value = e;
	}

	public Object run(HashMap<String, Object> hm)
	{
		return value;
	}
}

class StrEnterExpression implements Expr
{
	public Object run(HashMap<String, Object> hm)
	{
		java.util.Scanner in = new java.util.Scanner(System.in);
		return in.next();
	}
}

class ConcatStringExpression implements Expr
{
	Expr s, s2;

	public ConcatStringExpression(Expr s, Expr s2)
	{
		this.s = s;
		this.s2 = s2;
	}

	public Object run(HashMap<String, Object> hm)
	{
		Object v1 = s.run(hm);
		Object v2 = s2.run(hm);

		if (v1 instanceof String && v2 instanceof String) {
			return (String)v1 + (String)v2;
		} else {
			System.out.println("Error: wrong objects type");
			System.exit(1);
			return null;
		}
	}
}

class SubStringExpression implements Expr
{
	Expr sExpr, posExpr, lengthExpr;

	public SubStringExpression(Expr s, Expr pos, Expr length)
	{
		sExpr = s;
		posExpr = pos;
		lengthExpr = length;
	}

	public Object run(HashMap<String, Object> hm)
	{

		Object v1 = sExpr.run(hm);
		Object v2 = posExpr.run(hm);
		Object v3 = lengthExpr.run(hm);

		if (v1 instanceof String && v2 instanceof Integer && v3 instanceof Integer) {
			String s = (String)v1;
			int pos = (Integer)v2;
			int length = (Integer)v3;

			if (pos + length - 1 > s.length()) {
				length = s.length() - pos + 1;
			}
			if (pos < 1 || pos > s.length() || length < 1) {
				return "";
			} else {
				return new String(s.substring(pos-1, pos+length-1));
			}
		} else {
			System.out.println("Error: wrong objects type");
			System.exit(1);
			return null;
		}

	}
}


class OutputInstruction implements SimpleInstruction
{
	Expr expr;

	public OutputInstruction(Expr e)
	{
		expr = e;
	}

	public void run(HashMap<String, Object> hm)
	{
		System.out.println(expr.run(hm));
	}
}


/*


class List_stat implements SimpleInstruction {

	private ExpressionList expressionList;

	public List_stat(ExpressionList expressionList){
		this.expressionList=expressionList;
	}

	public void run(HashMap<String, Object> hm)
	{
		expressionList.run(hm);
	}

}


class ExpressionList{

	private List<Expr> value;


	public ExpressionList(Expr e) {
		value = new ArrayList<Expr>();
		value.add(e);
	}

	@Override
	public void run(HashMap<String, Object> hm){
		for (Expr si: value) {
			si.run(hm);

		}

	}

*/

/*
	@Override
	public Object run(HashMap<String, Object> hm) {

		for (Object obj : value) {
			Object v = obj.run(hm);
			valueNEW.add(v);
		}

		return valueNEW;
	}
*/


/*
	ArrayList<Object> value = new ArrayList<Object>();
	Expr e1, e2;

	public ListExpr(Expr e1, Expr e2)
	{
		this.e1=e1;
		this.e2=e2;

	}

	@Override
	public Object run(HashMap<String, Object> hm) {
		Object v1 = e1.run(hm);
		Object v2 = e2.run(hm);

		value.add(v1);
		value.add(v2);

		return value;
	}


}
		*/


/** FLOW OPERATIONS */
class InstructionList
{
	private List<SimpleInstruction> simpleInstructions;

	public InstructionList(SimpleInstruction s){
		simpleInstructions = new ArrayList<SimpleInstruction>();
		simpleInstructions.add(s);
	}

	public void add(SimpleInstruction s) {
		simpleInstructions.add(s);
	}

	public void run(HashMap<String, Object> hm){
		for (SimpleInstruction si: simpleInstructions) {
			si.run(hm);
		}
	}
}

class WhileInstruction implements WhileInstructionI
{
	Expr cond;
	SimpleInstruction si;

	public WhileInstruction(Expr c, SimpleInstruction s)
	{
		cond = c;
		si = s;
	}

	public void run(HashMap<String, Object> hm)
	{
		while ((Boolean)cond.run(hm)){
			si.run(hm);
		}
	}
}

class DoWhileInstruction implements WhileInstructionI
{
	Expr cond;
	SimpleInstruction si;

	public DoWhileInstruction(Expr c, SimpleInstruction s)
	{
		cond = c;
		si = s;
	}

	public void run(HashMap<String, Object> hm)
	{
		do
			si.run(hm);
		while ((Boolean)cond.run(hm));
	}
}

class IfInstruction implements IfInstructionI {

	Expr condition;
	SimpleInstruction simpleInstruction;

	public IfInstruction (Expr condition, SimpleInstruction simpleInstruction) {
		this.condition = condition;
		this.simpleInstruction = simpleInstruction;
	}

	public void run(HashMap<String, Object> hm){
		if ((Boolean)condition.run(hm)) {
			simpleInstruction.run(hm);
		}
	}
}


class IfElseInstruction implements IfInstructionI {

	Expr condition1, condition2;
	SimpleInstruction simpleInstruction1, simpleInstruction2;

	public IfElseInstruction (Expr condition1,  SimpleInstruction simpleInstruction1, Expr condition2, SimpleInstruction simpleInstruction2) {
		this.condition1 = condition1;
		this.condition2 = condition2;
		this.simpleInstruction1 = simpleInstruction1;
		this.simpleInstruction2 = simpleInstruction2;
	}

	public void run(HashMap<String, Object> hm){

		if ((Boolean)condition1.run(hm)) {
			simpleInstruction1.run(hm);
		}
		else if((Boolean)condition2.run(hm)){
			simpleInstruction2.run(hm);
		}
	}
}


class ElseInstruction implements IfInstructionI {

	Expr condition;
	Expr condition2;
	SimpleInstruction simpleInstruction;
	SimpleInstruction simpleInstruction2;
	SimpleInstruction simpleInstruction3;

	public ElseInstruction (Expr condition, SimpleInstruction simpleInstruction,Expr condition2, SimpleInstruction simpleInstruction2, SimpleInstruction simpleInstruction3) {
		this.condition = condition;
		this.condition2 = condition2;
		this.simpleInstruction = simpleInstruction;
		this.simpleInstruction2 = simpleInstruction2;
		this.simpleInstruction3 = simpleInstruction3;
	}

	public void run(HashMap<String, Object> hm){
		if ((Boolean)condition.run(hm)) {
			simpleInstruction.run(hm);
		}
		else if((Boolean)condition2.run(hm)){
			simpleInstruction2.run(hm);
		}
		else {
			simpleInstruction3.run(hm);
		}
	}
}


class BeginEndInstruction implements SimpleInstruction
{
	private InstructionList instructions;

	public BeginEndInstruction(InstructionList instructions)
	{
		this.instructions = instructions;
	}

	public void run(HashMap<String, Object> hm)
	{
		instructions.run(hm);
	}
}


