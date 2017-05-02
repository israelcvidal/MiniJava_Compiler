package devel.semantic_analysis;

import java.util.Enumeration;

import core.abstract_syntax.syntaxtree.Type;

/**
 * The class implements a environment for a MiniJava Class.
 * @author daniel
 *
 */

public class ClassTable {
	private TypeTable fieldTypeTable = new TypeTable();
	private Table<MethodTable> methodTable = new Table<>();
	
	public void putField(Symbol key, Type value) throws SemanticErrorException {
		fieldTypeTable.put(key, value);
	}
	
	public void putMethod(Symbol key, MethodTable value) throws SemanticErrorException {
		methodTable.put(key, value);
	}
	
	public Type getField(Symbol key) {
		return fieldTypeTable.get(key);
	}

	public MethodTable getMethod(Symbol key) {
		return methodTable.get(key);
	}
	
	public Enumeration<Symbol> fieldKeys() {
		return fieldTypeTable.keys();
	}
	
	public Enumeration<Symbol> methodKeys() {
		return methodTable.keys();
	}
	
	public void print() {
	   for (Enumeration<Symbol> iterator = fieldTypeTable.keys(); iterator.hasMoreElements();) {
		   Symbol s = iterator.nextElement();
		   System.out.println(s.toString() + "Type: " + fieldTypeTable.get(s).toString());
	   }
	   
	   for (Enumeration<Symbol> iterator = methodTable.keys(); iterator.hasMoreElements();) {
		   Symbol s = iterator.nextElement();
		   System.out.println(s.toString());
		   MethodTable mt = methodTable.get(s);
		   mt.print();
	   }
	}
	
}
