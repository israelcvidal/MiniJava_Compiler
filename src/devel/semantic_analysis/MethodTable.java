package devel.semantic_analysis;

import core.abstract_syntax.syntaxtree.Type;

/**
 * The class implements a environment for a MiniJava Method.
 * @author daniel
 *
 */

public class MethodTable {
	private TypeTable localTypeTable = new TypeTable();
	private TypeTable paramTypeTable = new TypeTable();
	
	public void putLocal(Symbol key, Type value) throws SemanticErrorException {
		localTypeTable.put(key, value);
	}
	
	public void putFormal(Symbol key, Type value) throws SemanticErrorException {
		paramTypeTable.put(key, value);
	}
	
	public Type getLocal(Symbol key) {
		return localTypeTable.get(key);
	}

	public Type getFormal(Symbol key) {
		return paramTypeTable.get(key);
	}
	
}
