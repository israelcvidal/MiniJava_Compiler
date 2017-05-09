package devel.semantic_analysis;

import java.util.Enumeration;

import core.abstract_syntax.syntaxtree.Type;

/**
 * The class implements a environment for a MiniJava Class.
 * @author daniel
 *
 */

public class ClassTable {
	private ClassTable parentClass = null;
	private TypeTable fieldTypeTable = new TypeTable();
	private Table<MethodTable> methodTable = new Table<>();
	
	public void setParentClass(ClassTable parentClass) {
		this.parentClass = parentClass;
	}
	
	public ClassTable getParentClass() {
		return this.parentClass;
	}
	
	public void putField(Symbol key, Type value) throws SemanticErrorException {
		fieldTypeTable.put(key, value);
	}
	
	public void putMethod(Symbol key, MethodTable value) throws SemanticErrorException {
		methodTable.put(key, value);
	}
	
	public Type getField(Symbol key) throws SemanticErrorException {
		if (parentClass == null)
			return fieldTypeTable.get(key);
		
		try {
			return fieldTypeTable.get(key);
		} catch (SemanticErrorException see) {
			return parentClass.getField(key);
		}
		
	}

	public MethodTable getMethod(Symbol key) throws SemanticErrorException {
		if (parentClass == null)
			return methodTable.get(key);
		
		try {
			return methodTable.get(key);
		} catch (SemanticErrorException see) {
			return parentClass.getMethod(key);
		}
		
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
		   try {
			System.out.println(s.toString() + "Type: " + fieldTypeTable.get(s).toString());
		} catch (SemanticErrorException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	   }
	   
	   for (Enumeration<Symbol> iterator = methodTable.keys(); iterator.hasMoreElements();) {
		   Symbol s = iterator.nextElement();
		   System.out.println(s.toString());
		   MethodTable mt;
		try {
			mt = methodTable.get(s);
			mt.print();
		} catch (SemanticErrorException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	   }
	}
}
