package devel.semantic_analysis;

import java.util.Enumeration;

import core.abstract_syntax.syntaxtree.Type;

/**
 * The class implements a environment for a MiniJava Method.
 * @author daniel
 *
 */

public class MethodTable {
	private Type returnType;
	private TypeTable localTypeTable = new TypeTable();
	private TypeTable formalTypeTable = new TypeTable();
	
	public MethodTable(Type returnType) {
		this.returnType = returnType;
	}
	
	public void putLocal(Symbol key, Type value) throws SemanticErrorException {
		localTypeTable.put(key, value);
	}
	
	public void putFormal(Symbol key, Type value) throws SemanticErrorException {
		formalTypeTable.put(key, value);
	}
	
	public Type getLocal(Symbol key) throws SemanticErrorException {
		return localTypeTable.get(key);
	}

	public Type getFormal(Symbol key) throws SemanticErrorException {
		return formalTypeTable.get(key);
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
