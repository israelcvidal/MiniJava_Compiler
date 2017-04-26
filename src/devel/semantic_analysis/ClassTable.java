package devel.semantic_analysis;

import core.abstract_syntax.syntaxtree.Type;

/**
 * The class implements a environment for a MiniJava Class.
 * @author daniel
 *
 */

public class ClassTable {
	private TypeTable fieldTypeTable = new TypeTable();
	private TypeTable methodTypeTable = new TypeTable();
	
	public void putField(Symbol key, Type value) throws SemanticErrorException {
		fieldTypeTable.put(key, value);
	}
	
	public void putMethod(Symbol key, Type value) throws SemanticErrorException {
		methodTypeTable.put(key, value);
	}
	
	public Type getField(Symbol key) {
		return fieldTypeTable.get(key);
	}

	public Type getMethod(Symbol key) {
		return methodTypeTable.get(key);
	}
	
}
