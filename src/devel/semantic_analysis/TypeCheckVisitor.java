package devel.semantic_analysis;

import core.abstract_syntax.syntaxtree.ClassDeclExtends;
import core.abstract_syntax.syntaxtree.ClassDeclSimple;
import core.abstract_syntax.syntaxtree.Formal;
import core.abstract_syntax.syntaxtree.MainClass;
import core.abstract_syntax.syntaxtree.MethodDecl;
import core.abstract_syntax.syntaxtree.Program;
import core.abstract_syntax.syntaxtree.Type;
import core.abstract_syntax.syntaxtree.VarDecl;

/**
 * This interface defines a set of methods for type checking.
 * @author daniel
 *
 */

public interface TypeCheckVisitor {
	public ProgramTable visit(Program n);
	public ClassTable visit(MainClass n);
	public ClassTable visit(ClassDeclSimple n);
	public ClassTable visit(ClassDeclExtends n);
	public Type visit(VarDecl n);
	public MethodTable visit(MethodDecl n);
	public Type visit(Formal n);
}
