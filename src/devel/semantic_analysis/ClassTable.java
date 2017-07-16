package devel.semantic_analysis;

import java.util.Enumeration;
import java.util.HashMap;

import core.abstract_syntax.syntaxtree.Type;

/**
 * The class implements a environment for a MiniJava Class.
 * @author daniel
 *
 */

public class ClassTable {
	private String i;
	private ClassTable parentClass = null;
	private TypeTable fieldTypeTable = new TypeTable();
	private HashMap<Symbol, Integer> fieldOffset = new HashMap<>();
	private Table<MethodTable> methodTable = new Table<>();
	private HashMap<Symbol, Integer> methodOffset = new HashMap<>();
	
	private int fieldAmount = 0;
	private int methodAmount = 0;
	
	public ClassTable(String i) {
		this.i = i;
	}
	
	public String getId() {
		return i;
	}
	
	public void setParentClass(ClassTable parentClass) {
		this.parentClass = parentClass;
	}
	
	public ClassTable getParentClass() {
		return this.parentClass;
	}
	
	public void putField(Symbol key, Type value) throws SemanticErrorException {
		fieldTypeTable.put(key, value);
		fieldOffset.put(key, fieldAmount++);
	}
	
	public void putMethod(Symbol key, MethodTable value) throws SemanticErrorException {
		methodTable.put(key, value);
		methodOffset.put(key, methodAmount++);
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

	public Type getFieldType(String id) {
		try {
			return getField(Symbol.symbol(id));
		} catch (SemanticErrorException see) { }
		
		return null;
	}
	
	public int getField(String id) {
		if (parentClass != null && fieldOffset.get(Symbol.symbol(id)) == null)
			return parentClass.getField(id);
		
		return fieldOffset.get(Symbol.symbol(id)); 
	}
	
	public int getFieldsSize() {
		return fieldTypeTable.size();
	}
	
	public int getMethodsSize() {
		return methodTable.size();
	}
	
//	public void putAccessField(Symbol key, Access access) {
//		fieldAccesses.put(key, access);
//	}
//	
//	public Access getAccessField(Symbol key) {
//		return fieldAccesses.get(key);
//	}
	
	public MethodTable getMethod(Symbol key) throws SemanticErrorException {
		if (parentClass == null)
			return methodTable.get(key);
		
		try {
			return methodTable.get(key);
		} catch (SemanticErrorException see) {
			return parentClass.getMethod(key);
		}
		
	}
	
	public MethodTable getMethod(String id) {
		try {
			return this.getMethod(Symbol.symbol(id));
		} catch (SemanticErrorException see) {
			see.printStackTrace();
		}
		
		return null;
	}
	
	public int getMethodOffset(String id) {
		return methodOffset.get(Symbol.symbol(id));
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
