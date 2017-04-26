package devel.semantic_analysis;

/**
 * The class implements a overall environment for a MiniJava Program.
 * @author daniel
 *
 */

public class ProgramTable {
	private Table<ClassTable> classTable = new Table<>();
	
	public void putClass(Symbol key, ClassTable value) throws SemanticErrorException {
		classTable.put(key, value);
	}
	
	public ClassTable getClass(Symbol key) {
		return classTable.get(key);
	}	
}
