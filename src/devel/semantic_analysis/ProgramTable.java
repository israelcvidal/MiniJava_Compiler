package devel.semantic_analysis;

import java.util.Enumeration;

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
	
	public ClassTable getClass(Symbol key) throws SemanticErrorException {
		return classTable.get(key);
	}
	
	public Enumeration<Symbol> keys() {
		return classTable.keys();
	}
	
	public void print() {
	   for (Enumeration<Symbol> iterator = classTable.keys(); iterator.hasMoreElements();) {
		   Symbol s = iterator.nextElement();
		   System.out.println(s.toString());
		   
		   try {
			   ClassTable ct = classTable.get(s);
			   ct.print();
		   } catch (SemanticErrorException see) {
			   see.printStackTrace();
		   }
	   }
	}
	
}