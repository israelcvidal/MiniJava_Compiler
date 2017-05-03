package devel.semantic_analysis;

import core.abstract_syntax.syntaxtree.And;
import core.abstract_syntax.syntaxtree.ArrayAssign;
import core.abstract_syntax.syntaxtree.ArrayLength;
import core.abstract_syntax.syntaxtree.ArrayLookup;
import core.abstract_syntax.syntaxtree.Assign;
import core.abstract_syntax.syntaxtree.Block;
import core.abstract_syntax.syntaxtree.BooleanType;
import core.abstract_syntax.syntaxtree.Call;
import core.abstract_syntax.syntaxtree.ClassDeclExtends;
import core.abstract_syntax.syntaxtree.ClassDeclSimple;
import core.abstract_syntax.syntaxtree.Exp;
import core.abstract_syntax.syntaxtree.False;
import core.abstract_syntax.syntaxtree.Formal;
import core.abstract_syntax.syntaxtree.Identifier;
import core.abstract_syntax.syntaxtree.IdentifierExp;
import core.abstract_syntax.syntaxtree.IdentifierType;
import core.abstract_syntax.syntaxtree.If;
import core.abstract_syntax.syntaxtree.IntArrayType;
import core.abstract_syntax.syntaxtree.IntegerLiteral;
import core.abstract_syntax.syntaxtree.IntegerType;
import core.abstract_syntax.syntaxtree.LessThan;
import core.abstract_syntax.syntaxtree.MainClass;
import core.abstract_syntax.syntaxtree.MethodDecl;
import core.abstract_syntax.syntaxtree.Minus;
import core.abstract_syntax.syntaxtree.NewArray;
import core.abstract_syntax.syntaxtree.NewObject;
import core.abstract_syntax.syntaxtree.Not;
import core.abstract_syntax.syntaxtree.Plus;
import core.abstract_syntax.syntaxtree.Print;
import core.abstract_syntax.syntaxtree.Program;
import core.abstract_syntax.syntaxtree.Statement;
import core.abstract_syntax.syntaxtree.This;
import core.abstract_syntax.syntaxtree.Times;
import core.abstract_syntax.syntaxtree.True;
import core.abstract_syntax.syntaxtree.Type;
import core.abstract_syntax.syntaxtree.VarDecl;
import core.abstract_syntax.syntaxtree.While;
import core.abstract_syntax.visitor.TypeVisitor;
import core.abstract_syntax.visitor.Visitor;

/**
 * This class checks the table built by BuildTableVisitor in order to check if variables and methods used were declared and
 * have the correct type.  
 * @author Israel
 */

public class CheckTableVisitor implements SecondPhaseTypeCheckVisitor{
	private ProgramTable programTable;
	
	public CheckTableVisitor(ProgramTable pt) {
		this.programTable = pt;
	}
	
	@Override
	public Type visit(IntArrayType n) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Type visit(BooleanType n) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Type visit(IntegerType n) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Type visit(IdentifierType n) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Type visit(Block n) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Type visit(If n) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Type visit(While n) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Type visit(Print n) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Type visit(Assign n) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Type visit(ArrayAssign n) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Type visit(And n) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Type visit(LessThan n) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Type visit(Plus n) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Type visit(Minus n) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Type visit(Times n) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Type visit(ArrayLookup n) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Type visit(ArrayLength n) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Type visit(Call n) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Type visit(IntegerLiteral n) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Type visit(True n) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Type visit(False n) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Type visit(IdentifierExp n) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Type visit(This n) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Type visit(NewArray n) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Type visit(NewObject n) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Type visit(Not n) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Type visit(Identifier n) {
		// TODO Auto-generated method stub
		return null;
	}

	public ProgramTable getProgramTable() {
		return programTable;
	}

	public void setProgramTable(ProgramTable programTable) {
		this.programTable = programTable;
	}



	@Override
	public Type visit(MainClass n) {
		// TODO Auto-generated method stub
		return null;
	}

	  
	

}