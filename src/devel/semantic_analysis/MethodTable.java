package devel.semantic_analysis;

import java.util.Enumeration;
import java.util.HashMap;

import core.abstract_syntax.syntaxtree.Type;
import core.activation_records.temp.Temp;

/**
 * The class implements a environment for a MiniJava Method.
 * @author daniel
 *
 */

public class MethodTable {
	private String name;
	private Type returnType;
	private TypeTable localTypeTable = new TypeTable();
	private HashMap<Symbol, Temp> localTemps = new HashMap<>();
	private TypeTable formalTypeTable = new TypeTable();
	private HashMap<Symbol, Temp> formalTemps = new HashMap<>();
	
	private int formalAmount = 1;
	
	public MethodTable(String name, Type returnType) {
		this.name = name;
		this.returnType = returnType;
	}
	
	public void putLocal(Symbol key, Type value) throws SemanticErrorException {
		localTypeTable.put(key, value);
		localTemps.put(key, new Temp());
	}
	
	public void putFormal(Symbol key, Type value) throws SemanticErrorException {
		formalTypeTable.put(key, value);
		formalTemps.put(key, new Temp(formalAmount++));
	}
	
	public Type getLocal(Symbol key) throws SemanticErrorException {
		return localTypeTable.get(key);
	}

	public Type getLocalType(String id) {
		try {
			return getLocal(Symbol.symbol(id));
		} catch (SemanticErrorException see) { }
		
		return null;
	}
	
	public Temp getLocal(String id) {
		return localTemps.get(Symbol.symbol(id));
	}
	
	public Type getFormal(Symbol key) throws SemanticErrorException {
		return formalTypeTable.get(key);
	}
	
	public Type getFormalType(String id) {
		try {
			return getFormal(Symbol.symbol(id));
		} catch (SemanticErrorException see) { }
		
		return null;
	}
	
	public Temp getFormal(String id) {
		return formalTemps.get(Symbol.symbol(id));
	}
	
	public String getName() {
		return name;
	}
	
	public Type getReturnType() {
		return returnType;
	}
	
	public Enumeration<Symbol> localKeys() {
		return localTypeTable.keys();
	}
	
	public Enumeration<Symbol> formalKeys() {
		return formalTypeTable.keys();
	}

	public int sizeLocal() {
		return formalTypeTable.size();
	}
	
	public int sizeFormal() {
		return formalTypeTable.size();
	}
	
	public void print() {
		   for (Enumeration<Symbol> iterator = localTypeTable.keys(); iterator.hasMoreElements();) {
			   Symbol s = iterator.nextElement();
			   //System.out.println(s.toString() + "Type: " + localTypeTable.get(s).toString());
			   System.out.println(s.toString());
		   }
		   
		   for (Enumeration<Symbol> iterator = formalTypeTable.keys(); iterator.hasMoreElements();) {
			   Symbol s = iterator.nextElement();
			   //System.out.println(s.toString() + "Type: " + formalTypeTable.get(s).toString());
			   System.out.println(s.toString());
		   }
	}
	
}
