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
interface FunctionReturnInstructionI extends SimpleInstruction {
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

class SimpleExit implements SimpleInstruction
{
	public void run(HashMap<String, Object> hm)
	{
		System.exit(0);

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
			System.out.println("HATA: Yanlış nesne türü kullanıldı!" +
					" \n " + "Lütfen nesne türü olarak sayı giriniz.");
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
			System.out.println("HATA: Yanlış nesne türü kullanıldı!"+
					" \n " + "Lütfen nesne türü olarak sayı giriniz.");
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
			System.out.println("HATA: Yanlış nesne türü kullanıldı!"+
					" \n " + "Lütfen nesne türü olarak sayı giriniz.");
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
				System.out.println("HATA: 0'a bölme işlemi yapıldı!");
				System.exit(1);
			}
			return (Integer)v1 / (Integer)v2;
		} else {
			System.out.println("HATA: Yanlış nesne türü kullanıldı!"+
					" \n " + "Lütfen nesne türü olarak sayı giriniz.");
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
				System.out.println("HATA: 0'a bölme işlemi yapıldı!");
				System.exit(1);
			}
			return (Integer)v1 % (Integer)v2;
		} else {
			System.out.println("HATA: Yanlış nesne türü kullanıldı!"+
					" \n " + "Lütfen nesne türü olarak sayı giriniz.");
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
		try{
			return value;
		}
		catch (Exception e) {
			System.out.println("HATA: Lütfen sayı giriniz!");
			return null;
		}
	}
}

class IntValueExpression implements Expr {
	Expr value;

	public IntValueExpression(Expr value) {
		this.value = value;
	}

	@Override
	public Object run(HashMap<String, Object> hm) {
		return value.run(hm);
	}

	@Override
	public String toString() {
		return value.toString();
	}

	public int getIntValue(HashMap<String, Object> hm) {
		return ((Number) value.run(hm)).intValue();
	}
}



class IntEnterExpression implements Expr {

	public Object run(HashMap<String, Object> hm) {
		Scanner scanner = new Scanner(System.in);
		try {
			int input = scanner.nextInt();
			return new IntValueExpression(new Expr() {
				@Override
				public Object run(HashMap<String, Object> hm) {
					return input;
				}
				@Override
				public String toString() {
					return "\"" + input + "\"";

				}
			}).getIntValue(hm);
		}
		catch(Exception e){
			System.out.println("HATA: Lütfen sayı giriniz.");
			System.exit(0);
			return null;
		}
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
			System.out.println("HATA: Yanlış nesne türü kullanıldı!"+
					" \n " + "Lütfen nesne türü olarak sayı giriniz.");
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
			System.out.println("HATA: Yanlış nesne türü kullanıldı!"+
					" \n " + "Lütfen nesne türü olarak kelime giriniz.");
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
			System.out.println("HATA: Yanlış nesne türü kullanıldı!"+
					" \n " + "Lütfen nesne türü olarak kelime giriniz.");
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
		else if(v1 instanceof Boolean && v2 instanceof Boolean){
			return ((Boolean)v1).equals((Boolean)v2);
		}
		else {
			System.out.println("HATA: Yanlış nesne türü kullanıldı!"+
					" \n " + "Lütfen nesne türü olarak sayı ile sayı, kelime ile kelime veya boolean ile boolean giriniz.");
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
			System.out.println("HATA: Yanlış nesne türü kullanıldı!"+
					" \n " + "Lütfen nesne türü olarak sayı giriniz.");
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
			System.out.println("HATA: Yanlış nesne türü kullanıldı!"+
					" \n " + "Lütfen nesne türü olarak sayı giriniz.");
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
			System.out.println("HATA: Yanlış nesne türü kullanıldı!"+
					" \n " + "Lütfen nesne türü olarak sayı giriniz.");
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
			System.out.println("HATA: Yanlış nesne türü kullanıldı!"+
					" \n " + "Lütfen nesne türü olarak sayı giriniz.");
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
		}
		else if (v1 instanceof String && v2 instanceof String) {
			return !((String)v1).equals((String)v2);
		}
		else if(v1 instanceof Boolean && v2 instanceof Boolean){
			return !((Boolean)v1).equals((Boolean)v2);

		}
		else
		{
			System.out.println("HATA: Yanlış nesne türü kullanıldı!"+
					" \n " + "Lütfen nesne türü olarak sayı ile sayı, kelime ile kelime veya boolean ile boolean giriniz.");
			System.exit(1);
			return false;
		}
	}
}



/** BOOLEAN OPERATIONS */
class BooleanAssignInstruction implements SimpleInstruction
{
	String name;
	Expr val;

	public BooleanAssignInstruction(String i, Expr e)
	{
		name = i;
		val = e;
	}

	public void run(HashMap<String, Object> hm)
	{
		boolean booleanValue = (boolean) val.run(hm);
		hm.put(name, booleanValue);

	}
}

class BooleanExpression implements Expr
{
	boolean value;

	public BooleanExpression(boolean e)
	{
		value = e;
	}
	@Override

	public Object run(HashMap<String, Object> hm)
	{
		if(value == true){
			return true;
		}
		else{
			return false;
		}
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

/** Adding values to list*/
class ListAdd implements SimpleInstruction {
	Expr exprList, value;

	public ListAdd(Expr exprList, Expr value) {
		this.exprList = exprList;
		this.value = value;
	}

	@Override
	public void run(HashMap<String, Object> hm) {

		Object list = exprList.run(hm);
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
			System.out.println("HATA: Yanlış nesne türü kullanıldı!"+"\n"+"Lütfen liste türünde bir nesne kullanınız.");
			System.exit(1);
			return;
		}

		Object newValue = value.run(hm);
		listA.add(newValue);

	}
}
/** Getting values from the list*/

class ListGet implements Expr {

	Expr exprList, value;

	public ListGet(Expr exprList, Expr value) {
		this.exprList = exprList;
		this.value = value;
	}

	@Override
	public Object run(HashMap<String, Object> hm) {

		Object list = exprList.run(hm);
		ArrayList<Object> listA = null;

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
			System.out.println("HATA: Yanlış nesne türü kullanıldı!"
					+"\n"+"Lütfen liste türünde bir nesne kullanınız.");
			System.exit(1);
		}

		Object newValue = value.run(hm);
		if(newValue instanceof Integer) {
			return listA.get((int) newValue);
		}
		else{
			if(newValue == null)
				System.out.println("HATA: Sayı olması gereken yerde farklı nesne bulunmakta.");
			if(newValue instanceof ArrayList)
				System.out.println("HATA: Sayı olması gereken yerde liste bulunmakta.");
			if(newValue instanceof String)
				System.out.println("HATA: Sayı olması gereken yerde yazı bulunmakta.");
			System.exit(1);
		}

		return null;

	}
}
/** Removing elements from the list by index */
//d listesindeki 3. elemanı sil
class ListRemove implements SimpleInstruction {
	Expr exprList, value;

	public ListRemove(Expr exprList, Expr value) {
		this.exprList = exprList;
		this.value = value;
	}

	@Override
	public void run(HashMap<String, Object> hm) {

		Object list = exprList.run(hm);
		ArrayList<Object> listA;

		try{


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
			System.out.println("HATA: Yanlış nesne türü kullanıldı!"
					+"\n"+"Lütfen liste türünde bir nesne kullanınız.");
			System.exit(1);
			return;
		}
		Object newValue = value.run(hm);
		listA.remove((int)newValue);
		}
		catch (Exception e) {
			System.out.println("HATA: Silinmek istenen eleman liste uzunluğunu aşıyor. Lütfen listenin içindeki bir elemanı seçiniz.");
		}
	}
}
/** Getting length of a list */
//d listesinin uzunluğu
class ListLength implements Expr {

	Expr exprList;

	public ListLength(Expr exprList) {
		this.exprList = exprList;

	}

	@Override
	public Object run(HashMap<String, Object> hm) {

		Object list = exprList.run(hm);
		ArrayList<Object> listA = null;

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
			System.out.println("HATA: Yanlış nesne türü kullanıldı!"
					+"\n"+"Lütfen liste türünde bir nesne kullanınız.");
			System.exit(1);
		}

		return listA.size();

	}
}
/** Clearing a list */
//d listesini temizle
class ListClear implements SimpleInstruction {
	Expr exprList;

	public ListClear(Expr exprList) {
		this.exprList = exprList;
	}

	@Override
	public void run(HashMap<String, Object> hm) {

		Object list = exprList.run(hm);
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
			System.out.println("HATA: Yanlış nesne türü kullanıldı!"
					+"\n"+"Lütfen liste türünde bir nesne kullanınız.");
			System.exit(1);
			return;
		}

		listA.clear();
	}
}


/** Updating a list */
// d listesinin 3. elemanı = b

class ListUpdate implements SimpleInstruction {

	Expr exprList, number, newValue;

	public ListUpdate(Expr exprList, Expr number, Expr newValue) {
		this.exprList = exprList;
		this.number = number;
		this.newValue = newValue;
	}

	@Override
	public void run(HashMap<String, Object> hm) {

		Object list = exprList.run(hm);
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
			System.out.println("HATA: Yanlış nesne türü kullanıldı!"
					+"\n"+"Lütfen liste türünde bir nesne kullanınız.");
			System.exit(1);
			return;
		}

		Object newNumber = number.run(hm);
		listA.set((int)newNumber, newValue.run(hm));

	}
}



/** Getting index of an element in a list */
//d listesindeki a elemanının sırası
class ListIndex implements Expr {

	Expr exprList, value;

	public ListIndex(Expr exprList, Expr value) {
		this.exprList = exprList;
		this.value = value;
	}

	@Override
	public Object run(HashMap<String, Object> hm) {

		Object list = exprList.run(hm);
		ArrayList<Object> listA = null;

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
			System.out.println("HATA: Yanlış nesne türü kullanıldı!"
					+"\n"+"Lütfen liste türünde bir nesne kullanınız.");
			System.exit(1);
		}

		Object newValue = value.run(hm);
		return listA.indexOf(newValue);

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

//GETTING VALUES FROM USER     a=readstr;
class StrValueExpression implements Expr {
	Expr value;

	public StrValueExpression(Expr value) {
		this.value = value;
	}

	@Override
	public Object run(HashMap<String, Object> hm) {
		return value.run(hm);
	}

	@Override
	public String toString() {
		return value.toString();
	}
	public String getStrValue(HashMap<String, Object> hm) {
		return ((String) value.run(hm)).toString();
	}

}

class StrEnterExpression implements Expr {

	public Object run(HashMap<String, Object> hm) {
		Scanner scanner = new Scanner(System.in);
		String input = scanner.nextLine();
		return new StrValueExpression(new Expr() {
			@Override
			public Object run(HashMap<String, Object> hm) {
				return input;
			}
			@Override
			public String toString() {
				return "\"" + input + "\""; // Add quotes to the string representation
			}
		}).getStrValue(hm);
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
			System.out.println("HATA: Yanlış nesne türü kullanıldı!"+
					" \n " + "Lütfen nesne türü olarak kelime giriniz.");
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
			System.out.println("HATA: Yanlış nesne türü kullanıldı!"+
					" \n " + "Lütfen nesne türü olarak sırasıyla kelime, sayı ve sayı giriniz.");
			System.exit(1);
			return null;
		}

	}
}

class OutputInstructionEmpty implements SimpleInstruction
{
	public OutputInstructionEmpty()
	{}
	public void run(HashMap<String, Object> hm)
	{
			System.out.println();
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

		if(expr.run(hm) instanceof  Boolean){
			if(expr.run(hm).equals(true)){
				System.out.println("doğru");
			}
			else if(expr.run(hm).equals(false)){
				System.out.println("yanlış");
			}

		}
		else{
			System.out.println(expr.run(hm));
		}

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

/** WHILE */
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
		boolean isContinueExecuted = false;

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

				if(!isContinueExecuted && isContinueEncountered){
					isContinueExecuted=true;
					break;
				}
			}
		}

	}
}

/** FOR LOOP */

class ForInstruction implements ForInstructionI{

	String name;
	Expr e1;
	Expr e2;
	Expr e3;

	private List<SimpleInstruction> recursiveList;
	boolean isBreakEncountered = false; //break control variable
	boolean isContinueEncountered = false;
	boolean isContinueExecuted = false;

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
				}
				//continue
				if (recursiveList.get(j) instanceof ContinueInstruction) {
					isContinueEncountered=true;


				}
				else if (recursiveList.get(j) instanceof IfInstruction) {
					IfInstruction ifInst = (IfInstruction) recursiveList.get(j);
					if (ifInst.isContinueEncountered) {
						isContinueEncountered=true;

					}
				}
				else if (recursiveList.get(j) instanceof IfElseInstruction) {
					IfElseInstruction ifelseInst = (IfElseInstruction) recursiveList.get(j);
					if (ifelseInst.isContinueEncountered) {
						isContinueEncountered=true;

					}
				}
				else if (recursiveList.get(j) instanceof IfElseIfInstruction) {
					IfElseIfInstruction ifelseifInst = (IfElseIfInstruction) recursiveList.get(j);
					if (ifelseifInst.isContinueEncountered) {
						isContinueEncountered=true;

					}
				}
				else if (recursiveList.get(j) instanceof ElseInstruction) {
					ElseInstruction elseinst = (ElseInstruction) recursiveList.get(j);
					if (elseinst.isContinueEncountered) {
						isContinueEncountered=true;

					}
				}

				if(!isContinueExecuted && isContinueEncountered){
					isContinueExecuted=true;
					break;
				}
			}
			i += ((Integer) e3.run(hm)).intValue();
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
	boolean isContinueExecuted = false;

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

				if(!isContinueExecuted && isContinueEncountered){
					isContinueExecuted=true;
					break;
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
			System.out.println("HATA: Yanlış nesne türü kullanıldı!"+
					" \n " + "Lütfen nesne türü olarak liste giriniz.");
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
					else if (!isContinueEncountered) {
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

class ReturnInstruction{

	private Expr expr;

	public ReturnInstruction(Expr expr) {
		this.expr = expr;
	}
	public Expr getReturn(){
		return expr;
	}
	public void run(HashMap<String,Object> hm)
	{

	}
}

class FunctionReturnInstruction implements FunctionInstructionI {

	private ArrayList<Expr> recursiveID;
	private String functionName;
	private List<SimpleInstruction> recursiveList;
	private Expr returnExpr;

	public FunctionReturnInstruction(ArrayList<Expr> recursiveID, String functionName, ArrayList<SimpleInstruction> recursiveList, Expr returnExpr) {
		this.functionName = functionName;
		this.recursiveList = recursiveList;
		this.recursiveID = recursiveID;
		this.returnExpr = returnExpr;
	}
	@Override
	public void run(HashMap<String, Object> hm) {

		try {
			ArrayList<Object> object = new ArrayList<>();
			ArrayList<String> objectID = new ArrayList<>();
			String identifierRet = "null";

			for (int i = 0; i < recursiveID.size(); i++) {
				Expr expr = recursiveID.get(i);
				if (expr instanceof ID) {
					ID idExpr = (ID) expr;
					String identifier = idExpr.getId();
					objectID.add(identifier);
				}
			}
			object.add(objectID);
			object.add(recursiveList);
			object.add(returnExpr);
			hm.put(functionName, object);
		}
		catch(Exception e){
			System.out.println("HATA: Fonksiyonu döndürürken hata yaşandı.");
		}
	}

	// Getters for the function name and parameter names
	public void add(Expr expr) {
		recursiveID.add(expr);
	}

}


class FunctionInstruction implements FunctionInstructionI {

	private ArrayList<Expr> recursiveID;
	private String functionName;
	private List<SimpleInstruction> recursiveList;


	public FunctionInstruction(ArrayList<Expr> recursiveID, String functionName, ArrayList<SimpleInstruction> recursiveList) {
		this.functionName = functionName;
		this.recursiveList = recursiveList;
		this.recursiveID = recursiveID;
	}

	@Override
	public void run(HashMap<String, Object> hm) {

		try {
			ArrayList<Object> object = new ArrayList<>();
			ArrayList<String> objectID = new ArrayList<>();

			for (int i = 0; i < recursiveID.size(); i++) {
				Expr expr = recursiveID.get(i);
				if (expr instanceof ID) {
					ID idExpr = (ID) expr;
					String identifier = idExpr.getId();
					objectID.add(identifier);
				}
			}

			object.add(objectID);
			object.add(recursiveList);
			hm.put(functionName, object);
		}
		catch (Exception e){
			System.out.println("HATA: Fonksiyonda hata var.");
		}
	}

	// Getters for the function name and parameter names

	public void add(Expr expr) {
		recursiveID.add(expr);
	}
}

class CallFunction implements FunctionInstructionI {

	private String functionName;
	private ArrayList<Expr> recursiveID;

	CallFunction(String functionName, ArrayList<Expr> recursiveID) {
		this.functionName = functionName;
		this.recursiveID = recursiveID;
	}

	@Override
	public void run(HashMap<String, Object> hm) {

		try {

			Object function = hm.get(functionName);
			ArrayList<Object> listA = new ArrayList<Object>();
			ArrayList<String> listParam = null;
			ArrayList<SimpleInstruction> listInstruction = new ArrayList<>();

			if (function == null) {
				throw new RuntimeException("Tanımlanamayan fonksiyon: " + functionName);
			}

			if (function instanceof ArrayList) {
				listA = (ArrayList<Object>) function;
			} else if (function.getClass().isArray()) {
				listA = new ArrayList<>();
				int length = Array.getLength(function);
				for (int i = 0; i < length; i++) {
					listA.add(Array.get(function, i));
				}
			}

			/** Parameters **/
			if (listA.get(0) instanceof ArrayList) {

				ArrayList<?> tempList = (ArrayList<?>) listA.get(0);
				listParam = new ArrayList<String>();

				for (Object obj : tempList) {
					if (obj instanceof String) {
						//System.out.println("girdi");
						listParam.add((String) obj);
					}
				}
			}

			/** simpleinstruction **/

			if (listA.get(1) instanceof ArrayList) {

				ArrayList<?> tempList = (ArrayList<?>) listA.get(1);
				listInstruction = new ArrayList<SimpleInstruction>();

				for (Object obj : tempList) {
					if (obj instanceof SimpleInstruction) {
						listInstruction.add((SimpleInstruction) obj);
					}
				}
			}

			HashMap<String, Object> variables = new HashMap<>();
			for (int i = 0; i < listParam.size(); i++) {
				variables.put(listParam.get(i), recursiveID.get(i).run(hm));
			}
			for (SimpleInstruction instruction : listInstruction) {
				instruction.run(variables);
			}
		}
		catch (Exception e){
			System.out.println("HATA: Fonksiyon çağırma bölümünde hata var.");

		}

	}
}

class CallReturnFunction implements Expr {

	private String functionName;
	private ArrayList<Expr> recursiveID;

	CallReturnFunction(String functionName, ArrayList<Expr> recursiveID) {
		this.functionName = functionName;
		this.recursiveID = recursiveID;
	}

	@Override
	public Object run(HashMap<String, Object> hm) {

		try {

			Object function = hm.get(functionName);
			ArrayList<Object> listA = new ArrayList<Object>();
			ArrayList<String> listParam = null;
			ArrayList<SimpleInstruction> listInstruction = new ArrayList<>();
			Expr temp = null;

			if (function == null) {
				throw new RuntimeException("Tanımlanamayan fonksiyon: " + functionName);
			}

			if (function instanceof ArrayList) {
				listA = (ArrayList<Object>) function;
			} else if (function.getClass().isArray()) {
				listA = new ArrayList<>();
				int length = Array.getLength(function);
				for (int i = 0; i < length; i++) {
					listA.add(Array.get(function, i));
				}
			}

			/** parameters **/
			if (listA.get(0) instanceof ArrayList) {

				ArrayList<?> tempList = (ArrayList<?>) listA.get(0);
				listParam = new ArrayList<String>();

				for (Object obj : tempList) {
					if (obj instanceof String) {
						//System.out.println("girdi");
						listParam.add((String) obj);
					}
				}
			}

			/** simpleinstruction **/

			if (listA.get(1) instanceof ArrayList) {

				ArrayList<?> tempList = (ArrayList<?>) listA.get(1);
				listInstruction = new ArrayList<SimpleInstruction>();

				for (Object obj : tempList) {
					if (obj instanceof SimpleInstruction) {
						listInstruction.add((SimpleInstruction) obj);
					}
				}
			}

			if (listA.get(2) instanceof Expr) {
				temp = (Expr) listA.get(2);
			}

			HashMap<String, Object> variables = new HashMap<>();

			for (int i = 0; i < listParam.size(); i++) {
				variables.put(listParam.get(i), recursiveID.get(i).run(hm));
			}
			for (SimpleInstruction instruction : listInstruction) {
				instruction.run(variables);
			}

			return temp.run(variables);
		}
		catch(Exception e){
			System.out.println("HATA: Fonksiyon çağırma bölümünde hata var.");
			System.exit(0);
			return null;
		}

	}
}

class RandomExpression implements Expr
{
	Expr min;
	Expr max;

	public RandomExpression(Expr min, Expr max)
	{
		this.min = min;
		this.max = max;
	}

	@Override
	public Object run(HashMap<String, Object> hm)
	{
		try{
			int minValue = (int) min.run(hm);
			int maxValue = (int) max.run(hm);
			int randomNumber = minValue + (int)(Math.random() * ((maxValue - minValue) + 1));
			return randomNumber;
		}
		catch (Exception e){
			System.out.println("HATA: Lütfen değerleri doğru girin.");
			return null;
		}
	}
}


class RandomExpressionList implements Expr
{
	Expr list;
	public RandomExpressionList(Expr list)
	{
		this.list = list;
	}
	@Override
	public Object run(HashMap<String, Object> hm)
	{
		Object _list = list.run(hm);
		ArrayList<Object> listA = new ArrayList<Object>();

		try {
			if (_list instanceof ArrayList) {
				listA = (ArrayList<Object>) _list;
			} else if (_list.getClass().isArray()) {
				listA = new ArrayList<>();
				int length = Array.getLength(_list);
				for (int i = 0; i < length; i++) {
					listA.add(Array.get(_list, i));
				}
			}
			Random rand = new Random();
			Object random = listA.get(rand.nextInt(listA.size()));

			return random;
		}
		catch (Exception e){
			System.out.println("HATA: Girilen nesne liste değil. Lütfen bir liste girin.");
			return null;
		}

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


