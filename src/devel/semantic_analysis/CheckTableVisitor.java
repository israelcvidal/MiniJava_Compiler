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
 * This class checks the table built by BuildTableVisitor in order to check if variables and methods used were declared and
 * have the correct type.  
 * @author Israel
 */

public class CheckTableVisitor implements TypeCheckVisitor {

	@Override
	public ProgramTable visit(Program n) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ClassTable visit(MainClass n) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ClassTable visit(ClassDeclSimple n) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ClassTable visit(ClassDeclExtends n) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Type visit(VarDecl n) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public MethodTable visit(MethodDecl n) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Type visit(Formal n) {
		// TODO Auto-generated method stub
		return null;
	}

}
