import java.io.*;
import java.util.*;
import java.lang.reflect.Array;

interface Expr { Object run(HashMap<String, Object> hm); }
interface Condition { boolean test(Expr e1, Expr e2, HashMap<String, Object> hm); }
interface Operator { int count(Expr e1, Expr e2, HashMap<String, Object> hm); }

interface SimpleInstruction { void run(HashMap<String,Object> hm); }

interface WhileInstructionI extends SimpleInstruction {
	void run(HashMap<String, Object> hm); }
interface IfInstructionI extends SimpleInstruction {
	void run(HashMap<String, Object> hm); }
interface ForInstructionI extends SimpleInstruction {
	void run(HashMap<String, Object> hm); }
interface ForeachInstructionI extends SimpleInstruction {
	void run(HashMap<String, Object> hm); }
interface FunctionInstructionI extends SimpleInstruction {
	void run(HashMap<String, Object> hm); }

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

	public String getId() {
		return name;
	}

	public void setId(String name) {
		this.name = name;
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
		}
		else if (v1 instanceof String && v2 instanceof String) {
			return ((String)v1).equals((String)v2);
		}
		else {
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


/** LIST OPERATIONS	*/

class ListContent {

	private ArrayList<Expr> expr;

	public ListContent(Expr e){
		expr = new ArrayList<Expr>();
		expr.add(e);
	}

	public void add(Expr expression) {
		expr.add(expression);
	}

	public ArrayList<Expr> getExpr() {
		return expr;
	}

	public void run(HashMap<String, Object> hm){
		for (Expr si: expr) {
			si.run(hm);
		}

	}
}

class ListExpr implements Expr {
	ArrayList<Expr> value;
	ArrayList<Object> valueNEW = new ArrayList<>();
	public ListExpr(ArrayList<Expr> list) {
		value = list;
	}

	@Override
	public Object run(HashMap<String, Object> hm) {

		for (Expr expr : value) {
			Object v = expr.run(hm);
			valueNEW.add(v);
		}
		return valueNEW;
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

class WhileInstruction implements WhileInstructionI {
	Expr cond;
	private List<SimpleInstruction> si;

	public WhileInstruction(Expr c, ArrayList<SimpleInstruction>  s) {
		cond = c;
		si = s;
	}

	public void run(HashMap<String, Object> hm) {
		boolean isBreakEncountered = false;
		boolean isContinueEncountered = false;

		while ((Boolean)cond.run(hm) && (!isBreakEncountered || !isContinueEncountered)) {
			for(int i = 0; i < si.size(); i++) {
				si.get(i).run(hm);
				if (si.get(i) instanceof BreakInstruction) {
					isBreakEncountered = true;
					break;
				}
				else if (si.get(i) instanceof IfInstruction) {
					IfInstruction ifInst = (IfInstruction) si.get(i);
					if (ifInst.isBreakEncountered) {
						isBreakEncountered = true;
						break;
					}
				}
				else if (si.get(i) instanceof IfElseInstruction) {
					IfElseInstruction ifelseInst = (IfElseInstruction) si.get(i);
					if (ifelseInst.isBreakEncountered) {
						isBreakEncountered = true;
						break;
					}
				}
				//continue

				if (si.get(i) instanceof ContinueInstruction) {
					isContinueEncountered = true;
					continue;
				}
				else if (si.get(i) instanceof IfInstruction) {
					IfInstruction ifInst = (IfInstruction) si.get(i);

					if (ifInst.isContinueEncountered) {
						isContinueEncountered = true;
						continue;
					}
				}
				else if (si.get(i) instanceof IfElseInstruction) {
					IfElseInstruction ifelseInst = (IfElseInstruction) si.get(i);
					if (ifelseInst.isContinueEncountered) {
						isContinueEncountered=true;
						continue;
					}
				}
			}
		}

	}
}


class ForInstruction implements ForInstructionI{

	String name;
	Expr e1;
	Expr e2;
	Expr e3;
	private List<SimpleInstruction> recursiveList;
	boolean isBreakEncountered = false; //break control variable
	boolean isContinueEncountered = false;

	public ForInstruction(String name, Expr e1, Expr e2, Expr e3, ArrayList<SimpleInstruction> recursiveList){
		this.name = name;
		this.e1 = e1;
		this.e2 = e2;
		this.e3 = e3;
		this.recursiveList = recursiveList;
	}

	@Override
	public void run(HashMap<String, Object> hm) {

		if(((Integer)e1.run(hm)).intValue()>((Integer)e2.run(hm)).intValue() ){
			Expr e=e1;
			e1=e2;
			e2=e;
		}
		int i = ((Integer)e1.run(hm)).intValue();
		while (i <= ((Integer)e2.run(hm)).intValue() && (!isBreakEncountered || !isContinueEncountered)){

			hm.put(name, i);
			for(int j = 0; j < recursiveList.size(); j++){
				recursiveList.get(j).run(hm);
				if(recursiveList.get(j) instanceof BreakInstruction){ //break control
					isBreakEncountered= true;
					break;
				} else if (recursiveList.get(j) instanceof IfInstruction) {
					IfInstruction ifInst = (IfInstruction) recursiveList.get(j);
					if (ifInst.isBreakEncountered) {
						isBreakEncountered = true;
						break;
					}
				}
				else if (recursiveList.get(j) instanceof IfElseInstruction) {
					IfElseInstruction ifelseInst = (IfElseInstruction) recursiveList.get(j);
					if (ifelseInst.isBreakEncountered) {
						isBreakEncountered = true;
						break;
					}
				}
				else if (recursiveList.get(j) instanceof IfElseIfInstruction) {
					IfElseIfInstruction ifelseifInst = (IfElseIfInstruction) recursiveList.get(j);
					if (ifelseifInst.isBreakEncountered) {
						isBreakEncountered = true;
						break;
					}
				}
				else if (recursiveList.get(j) instanceof ElseInstruction) {
					ElseInstruction elseinst = (ElseInstruction) recursiveList.get(j);
					if (elseinst.isBreakEncountered) {
						isBreakEncountered = true;
						break;
					}
					else if (elseinst.isContinueEncountered) {
						isContinueEncountered=true;
						continue;
					}
				}
				//continue
				if (recursiveList.get(j) instanceof ContinueInstruction) {
					isContinueEncountered=true;
					continue;
				}
				else if (recursiveList.get(j) instanceof IfInstruction) {
					IfInstruction ifInst = (IfInstruction) recursiveList.get(j);
					if (ifInst.isContinueEncountered) {
						isContinueEncountered=true;
						continue;
					}
				}
				else if (recursiveList.get(j) instanceof IfElseInstruction) {
					IfElseInstruction ifelseInst = (IfElseInstruction) recursiveList.get(j);
					if (ifelseInst.isContinueEncountered) {
						isContinueEncountered=true;
						continue;
					}
				}
				else if (recursiveList.get(j) instanceof IfElseIfInstruction) {
					IfElseIfInstruction ifelseifInst = (IfElseIfInstruction) recursiveList.get(j);
					if (ifelseifInst.isContinueEncountered) {
						isContinueEncountered=true;
						continue;
					}
				}
				else if (recursiveList.get(j) instanceof ElseInstruction) {
					ElseInstruction elseinst = (ElseInstruction) recursiveList.get(j);
					if (elseinst.isContinueEncountered) {
						isContinueEncountered=true;
						continue;
					}
				}
			}
			i += ((Integer)e3.run(hm)).intValue();

		}


	}
}
class ForInstruction2 implements ForInstructionI{

	String name;
	Expr e1;
	Expr e2;
	Expr e3;
	private List<SimpleInstruction> recursiveList;
	boolean isBreakEncountered = false; //break control variable
	boolean isContinueEncountered = false;

	public ForInstruction2(String name, Expr e1, Expr e2, Expr e3, ArrayList<SimpleInstruction> recursiveList){
		this.name = name;
		this.e1 = e1;
		this.e2 = e2;
		this.e3 = e3;

		this.recursiveList = recursiveList;
	}

	@Override
	public void run(HashMap<String, Object> hm) {


		if(((Integer)e1.run(hm)).intValue()>((Integer)e2.run(hm)).intValue()){
			Expr e=e1;
			e1=e2;
			e2=e;
		}
		int i = ((Integer)e2.run(hm)).intValue();
		while (i >= ((Integer)e1.run(hm)).intValue() && (!isBreakEncountered || !isContinueEncountered)){
			hm.put(name, i);
			for(int j = 0; j < recursiveList.size(); j++){
				recursiveList.get(j).run(hm);
				if(recursiveList.get(j) instanceof BreakInstruction){ // break
					isBreakEncountered= true;
					break;
				}
				else if (recursiveList.get(j) instanceof IfInstruction) {
					IfInstruction ifInst = (IfInstruction) recursiveList.get(j);
					if (ifInst.isBreakEncountered) {
						isBreakEncountered = true;
						break;
					}
				}
				else if (recursiveList.get(j) instanceof IfElseInstruction) {
					IfElseInstruction ifelseInst = (IfElseInstruction) recursiveList.get(j);
					if (ifelseInst.isBreakEncountered) {
						isBreakEncountered = true;
						break;
					}
				}
				else if (recursiveList.get(j) instanceof IfElseIfInstruction) {
					IfElseIfInstruction ifelseifInst = (IfElseIfInstruction) recursiveList.get(j);
					if (ifelseifInst.isBreakEncountered) {
						isBreakEncountered = true;
						break;
					}
				}
				else if (recursiveList.get(j) instanceof ElseInstruction) {
					ElseInstruction elseinst = (ElseInstruction) recursiveList.get(j);
					if (elseinst.isBreakEncountered) {
						isBreakEncountered = true;
						break;
					}
				}
				//continue
				if (recursiveList.get(j) instanceof ContinueInstruction) {
					isContinueEncountered=true;
					continue;
				}
				else if (recursiveList.get(j) instanceof IfInstruction) {
					IfInstruction ifInst = (IfInstruction) recursiveList.get(j);
					if (ifInst.isContinueEncountered) {
						isContinueEncountered=true;
						continue;
					}
				}
				else if (recursiveList.get(j) instanceof IfElseInstruction) {
					IfElseInstruction ifelseInst = (IfElseInstruction) recursiveList.get(j);
					if (ifelseInst.isContinueEncountered) {
						isContinueEncountered=true;
						continue;
					}
				}
				else if (recursiveList.get(j) instanceof IfElseIfInstruction) {
					IfElseIfInstruction ifelseifInst = (IfElseIfInstruction) recursiveList.get(j);
					if (ifelseifInst.isContinueEncountered) {
						isContinueEncountered=true;
						continue;
					}
				}
				else if (recursiveList.get(j) instanceof ElseInstruction) {
					ElseInstruction elseinst = (ElseInstruction) recursiveList.get(j);
					if (elseinst.isContinueEncountered) {
						isContinueEncountered=true;
						continue;
					}
				}
			}

			i -= ((Integer)e3.run(hm)).intValue();
		}


	}
}

class ForeachInstruction implements ForeachInstructionI{

	Expr e;
	String ident;
	private List<SimpleInstruction> recursiveList;

	public ForeachInstruction(Expr e, String ident, List<SimpleInstruction> recursiveList) {
		this.ident = ident;
		this.e = e;
		this.recursiveList = recursiveList;
	}

	@Override
	public void run(HashMap<String, Object> hm) {

		Object list = e.run(hm);
		ArrayList<Object> listA;

		if(list instanceof ArrayList){
			listA = (ArrayList<Object>) list;
		}
		else if(list.getClass().isArray()) {
			listA = new ArrayList<>();
			int length = Array.getLength(list);
			for(int i = 0; i < length; i++) {
				listA.add(Array.get(list, i));
			}
		}
		else {
			System.out.println("Error: wrong object type");
			System.exit(1);
			return;
		}

		for (Object i: listA) {
			hm.put(ident, i);
			for(int j = 0; j < recursiveList.size(); j++){
				recursiveList.get(j).run(hm);
			}
		}
	}
}



class IfInstruction implements IfInstructionI {

	Expr condition;
	ArrayList<SimpleInstruction> recursiveList;
	boolean isBreakEncountered = false;
	boolean isContinueEncountered = false;

	public IfInstruction (Expr condition,ArrayList<SimpleInstruction> recursiveList) {
		this.condition = condition;
		this.recursiveList = recursiveList;

	}
	public void run(HashMap<String, Object> hm){

		if ((Boolean)condition.run(hm) && (!isBreakEncountered || !isContinueEncountered)) {

			for(int i =0; i < recursiveList.size(); i++){
				recursiveList.get(i).run(hm);

				if(recursiveList.get(i) instanceof BreakInstruction){
					isBreakEncountered = true;
					break;
				}
				if (recursiveList.get(i) instanceof ContinueInstruction) {
					isContinueEncountered = true;
					continue;
				}
			}
		}

	}
}

class IfElseIfInstruction implements IfInstructionI {

	Expr condition;

	ArrayList<SimpleInstruction> recursiveList;

	ArrayList<SimpleInstruction> recursiveList2;
	boolean isBreakEncountered = false;
	boolean isContinueEncountered = false;

	public IfElseIfInstruction (Expr condition, ArrayList<SimpleInstruction> recursiveList, ArrayList<SimpleInstruction> recursiveList2) {
		this.condition = condition;
		this.recursiveList = recursiveList;
		this.recursiveList2 = recursiveList2;
	}

	public void run(HashMap<String, Object> hm){

		if ((Boolean)condition.run(hm) && (!isBreakEncountered || !isContinueEncountered)) {
			for(int i =0; i < recursiveList.size(); i++){
				recursiveList.get(i).run(hm);
				if(recursiveList.get(i) instanceof BreakInstruction){
					isBreakEncountered = true;
					break;
				}
				if (recursiveList.get(i) instanceof ContinueInstruction) {
					isContinueEncountered=true;
					continue;
				}

			}
		}
		else {
			for(int i =0; i < recursiveList2.size(); i++){
				recursiveList2.get(i).run(hm);
			}
		}

	}
}

class IfElseInstruction implements IfInstructionI {

	Expr condition1;
	ArrayList<SimpleInstruction> recursiveList;
	ArrayList<Expr> boolList;
	private ArrayList<ArrayList<SimpleInstruction>> recursiveList2;
	boolean isBreakEncountered = false;
	boolean isContinueEncountered = false;

	public IfElseInstruction (Expr condition1, ArrayList<SimpleInstruction> recursiveList, ArrayList<Expr> boolList, ArrayList<ArrayList<SimpleInstruction>> recursiveList2 ) {
		this.condition1 = condition1;
		this.boolList = boolList;
		this.recursiveList = recursiveList;
		this.recursiveList2 = recursiveList2;
	}

	public void run(HashMap<String, Object> hm){

		if ((Boolean)condition1.run(hm) && (!isBreakEncountered || !isContinueEncountered)) {
			for(int i =0; i < recursiveList.size(); i++){
				recursiveList.get(i).run(hm);
				if(recursiveList.get(i) instanceof BreakInstruction){ //check if the break statement is true for condition1 (eğer ise)
					isBreakEncountered = true;
					break; // if true, breaks the loop
				}
				if (recursiveList.get(i) instanceof ContinueInstruction) {
					isContinueEncountered=true;
					continue;
				}
			}
		}

		else {
			for(int i=0;i<boolList.size();i++){
				if((Boolean)(boolList.get(i).run(hm))){
					if(!isBreakEncountered) {
						for (int a = 0; a < recursiveList2.get(i).size(); a++) { //check if the break statement is true for condition2 (ya da ise)
							recursiveList2.get(i).get(a).run(hm);
							if(recursiveList2.get(i).get(a) instanceof BreakInstruction){
								isBreakEncountered = true; // if true, breaks the loop
								break;
							}

						}
					}
					if (!isContinueEncountered) {
						for (int a = 0; a < recursiveList2.get(i).size(); a++) {
							recursiveList2.get(i).get(a).run(hm);
							if(recursiveList2.get(i).get(a) instanceof ContinueInstruction){
								isContinueEncountered = true;
								continue;
							}
						}
					}
				}
			}
		}

	}
}


class ElseInstruction implements IfInstructionI {

	Expr condition1;
	ArrayList<SimpleInstruction> recursiveList;
	ArrayList<Expr> boolList;
	ArrayList<ArrayList<SimpleInstruction>> simpleInsList;
	ArrayList<SimpleInstruction> recursiveList2;
	boolean isBreakEncountered =  false;
	boolean isContinueEncountered =  false;

	public ElseInstruction(Expr condition1, ArrayList<SimpleInstruction> recursiveList, ArrayList<Expr> boolList, ArrayList<ArrayList<SimpleInstruction>> simpleInsList, ArrayList<SimpleInstruction> recursiveList2) {
		this.condition1 = condition1;
		this.recursiveList = recursiveList;
		this.boolList = boolList;
		this.simpleInsList = simpleInsList;
		this.recursiveList2 = recursiveList2;
	}


	public void run(HashMap<String, Object> hm){
		int a = 0;

		if ((Boolean)condition1.run(hm) && (!isBreakEncountered || !isContinueEncountered)) {
			for(int i =0; i < recursiveList.size(); i++){
				recursiveList.get(i).run(hm);
				if(recursiveList.get(i) instanceof BreakInstruction){
					isBreakEncountered = true;
					break;
				}
				if (recursiveList.get(i) instanceof ContinueInstruction) {
					isContinueEncountered = true;
					continue;
				}
			}
		}

		else {
			for(int i=0;i<boolList.size();i++){
				if((Boolean)(boolList.get(i).run(hm))) {
					if (!isBreakEncountered){
						for (int b = 0; b < simpleInsList.get(i).size(); b++) {
							simpleInsList.get(i).get(b).run(hm);
							if(simpleInsList.get(i).get(b) instanceof BreakInstruction){ //check if conditions has break statement
								isBreakEncountered = true;
								break;
							}
						}
						a++;
						break;
					}
					if (!isContinueEncountered) {
						for (int b = 0; b < simpleInsList.get(i).size(); b++) {
							simpleInsList.get(i).get(b).run(hm);
							if(simpleInsList.get(i).get(b) instanceof ContinueInstruction){
								isContinueEncountered = true;
								continue;
							}
						}
						a++;
						break;
					}
				}
			}
			if(a==0) {
				for(int i =0; i < recursiveList2.size(); i++){
					recursiveList2.get(i).run(hm);
				}
			}
		}

	}
}

class ElseIfContent{

	private ArrayList<Expr> booleanList;
	private ArrayList<ArrayList<SimpleInstruction>> recursiveList;

	public ElseIfContent(Expr bool, ArrayList<SimpleInstruction>ins){

		booleanList = new ArrayList<Expr>();
		recursiveList = new ArrayList<>();

		booleanList.add(bool);
		recursiveList.add(ins);
	}

	public void add(Expr bool, ArrayList<SimpleInstruction>ins) {
		booleanList.add(bool);
		recursiveList.add(ins);
	}

	public ArrayList<Expr> getExpr() {
		return booleanList;
	}

	public ArrayList<ArrayList<SimpleInstruction>> getSimpleinsList() {
		return recursiveList;
	}

	public void run(HashMap<String, Object> hm){
		for (int i=0;i<booleanList.size();i++) {
			booleanList.get(i).run(hm);
			for (int a=0;a<recursiveList.size();a++){
				recursiveList.get(i).get(a).run(hm);
			}

		}

	}

}


class Recursive{
	private ArrayList<SimpleInstruction> recursiveList;
	public Recursive(SimpleInstruction ins){

		recursiveList = new ArrayList<SimpleInstruction>();
		recursiveList.add(ins);
	}
	public void add(SimpleInstruction ins) {
		recursiveList.add(ins);
	}
	public ArrayList<SimpleInstruction> getSimplein() {

		return recursiveList;
	}
	public void run(HashMap<String, Object> hm){
		/*for (int i=0;i<recursiveList.size();i++) {
			recursiveList.get(i).run(hm);
		} */
	}
}

class RecursiveID{
	private ArrayList<Expr> recursiveID;
	public RecursiveID(Expr ins){

		recursiveID = new ArrayList<Expr>();
		recursiveID.add(ins);
	}
	public void add(Expr ins) {
		recursiveID.add(ins);
	}
	public ArrayList<Expr> getRecursiveID() {

		return recursiveID;
	}
	public void run(HashMap<String, Object> hm){
		/*for (int i=0;i<recursiveList.size();i++) {
			recursiveList.get(i).run(hm);
		} */
	}
}

class BreakInstruction implements SimpleInstruction
{

	public void run(HashMap<String, Object> hm)
	{


	}
}

class ContinueInstruction implements SimpleInstruction{

	@Override
	public void run(HashMap<String, Object> hm) {


	}
}

class ReturnInstruction implements Expr {

	private Expr expr;

	public ReturnInstruction(Expr expr) {
		this.expr = expr;
	}
	public Expr getReturn(){
		return expr;
	}
	public Object run(HashMap<String,Object> hm)
	{
		return hm.get(expr);
	}
}


class FunctionInstruction implements FunctionInstructionI {

	private ArrayList<Expr> recursiveID;
	private Expr functionName;
	private List<SimpleInstruction> recursiveList;

	private Expr ret;

	public FunctionInstruction(ArrayList<Expr> recursiveID, Expr functionName, ArrayList<SimpleInstruction> recursiveList,Expr ret) {
		this.functionName = functionName;
		this.recursiveList = recursiveList;
		this.recursiveID = recursiveID;
		this.ret = ret;
	}

	@Override
	public void run(HashMap<String, Object> hm) {

		for (Expr si : recursiveID) {
			si.run(hm);
		}

		// Execute the function's body
		for (int i = 0; i < recursiveList.size(); i++) {
			recursiveList.get(i).run(hm);
			/*
			// Check if the function should return
			if (recursiveList.contains("döndür")) {
				Expr returnValue = (Expr) recursiveList.get(i);
				recursiveList.remove("döndür");
			} */
		}
		ArrayList <Object> object= new ArrayList<>();
		object.add(recursiveID);
		System.out.println(functionName.run(hm));
		hm.put((String) functionName.run(hm),object);
	}

	// Getters for the function name and parameter names
	public Expr getFunctionName() {
		return functionName;
	}

	public void add(Expr expr) {
		recursiveID.add(expr);
	}

	public ArrayList<Expr> getExpr() {
		return recursiveID;
	}

}
class CallFunction implements FunctionInstructionI {
	private Expr functionName;
	private ArrayList<Expr> recursiveID; //parameters

	CallFunction(Expr functionName, ArrayList<Expr> recursiveID) {
		this.functionName = functionName;
		this.recursiveID = recursiveID;
	}

	@Override
	public void run(HashMap<String, Object> hm) {
		// Get the function object from the symbol table
		Object function = hm.get(functionName.run(hm));
		ArrayList<Object> parameterNames = null;
		ArrayList<Expr> listParam = null;

		// Check if the function is defined
		if (function == null) {
			throw new RuntimeException("Tanımlanamayan fonksiyon: " + functionName);
		}
		// Check if the function is an array list or an array
		try {
			if (function instanceof ArrayList) {
				//parameterNames = ((ArrayList<Expr>) function).get(0).getExpr();
				parameterNames = (ArrayList<Object>) function;
			} else if (function.getClass().isArray()) {
				//parameterNames = ((Expr[]) Array.get(function, 0))[0].getExpr();
				parameterNames = new ArrayList<>();
				int length = Array.getLength(function);
				for(int i = 0; i < length; i++) {
					parameterNames.add((Expr) Array.get(function, i));
				}
			} else {
				throw new RuntimeException("Geçersiz fonksiyon tipi: " + function.getClass());
			}
		} catch (ClassCastException e) {
			throw new RuntimeException("Geçersiz fonksiyon tipi: " + function.getClass());
		}
		// Check if the number of arguments matches the number of parameters
		 /*if (parameterNames.size() != recursiveID.size()) {
			throw new RuntimeException(functionName + " isimli fonksiyon " +
					parameterNames.size() + " parametre gerektiriyor fakat " +
					recursiveID.size() + " tane parametre verilmiş.");
		} */

		// Create a new symbol table for the function call
		HashMap<String, Object> newHm = new HashMap<>(hm);

		// Bind the arguments to the parameters
		if(parameterNames.get(0) instanceof ArrayList){

			ArrayList<?> tempList = (ArrayList<?>) parameterNames.get(0);
			listParam = new ArrayList<>();

			for (Object obj : tempList) {
				if (obj instanceof Expr) {
					listParam.add((Expr) obj);
				}
			}
		}
		for (int i = 0; i < listParam.size(); i++) {
			Object argValue = recursiveID.get(i).run(newHm);
			String paramName = listParam.get(i).run(newHm).toString();
			newHm.put(paramName, argValue);
			//System.out.println(hm.get(0));

			Object[] objectArray = (Object[]) hm.get(functionName);
			System.out.println(Arrays.toString(objectArray));
			// Iterate through the keys in newhm and update corresponding values in hm
			/*for (String key : hm.keySet()) {
				if (newHm.containsKey(key)) {
					Object[] oldArray = (Object[]) hm.get(key);
					Object[] newArray = (Object[]) newHm.get(key);
					String[] oldStringArray = (String[]) oldArray[0];
					String[] newStringArray = (String[]) newArray[0];
					oldStringArray[0] = newStringArray[0];
				}
			} */
		}

		// Execute the function body
		Object returnValue;
		try {
			if (function instanceof ArrayList<?>) {
				ArrayList<?> list = (ArrayList<?>) function;
				if (!list.isEmpty() && list.get(0) instanceof Expr) {
					((Expr) list.get(1)).run(newHm);
				}
			}
			else {
				Expr[] instructions = (Expr[]) Array.get(function, 1);
				for (Expr instruction : instructions) {
					instruction.run(newHm);
				}
			}
		}catch (RuntimeException e) {
			throw new RuntimeException(functionName+ " fonksiyonunu çalıştırırken hata oluştu " + ": " + e.getMessage());
		}

		// Check if the function has a return value and store it in the symbol table
		if (newHm.containsKey("returnValue")) {
			//
			hm.put("returnValue", newHm.get("returnValue"));
		}
		System.out.println(hm.get("returnValue"));

	}
}


class BeginEndInstruction implements SimpleInstruction {
	private InstructionList instructions;

	public BeginEndInstruction(InstructionList instructions) {
		this.instructions = instructions;
	}

	public void run(HashMap<String, Object> hm) {
		instructions.run(hm);

	}
}


