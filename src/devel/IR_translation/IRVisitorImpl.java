package devel.IR_translation;

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
import core.abstract_syntax.syntaxtree.This;
import core.abstract_syntax.syntaxtree.Times;
import core.abstract_syntax.syntaxtree.True;
import core.abstract_syntax.syntaxtree.VarDecl;
import core.abstract_syntax.syntaxtree.While;
import core.abstract_syntax.visitor.IRVisitor;
import core.translation_to_IR.tree.AbstractExp;
import core.translation_to_IR.tree.PRINT;
import core.translation_to_IR.tree.Stm;

public class IRVisitorImpl implements IRVisitor {

	@Override
	public Stm visit(Program n) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Stm visit(MainClass n) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Stm visit(ClassDeclSimple n) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Stm visit(ClassDeclExtends n) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Stm visit(VarDecl n) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Stm visit(MethodDecl n) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Stm visit(Formal n) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Stm visit(Block n) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Stm visit(If n) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Stm visit(While n) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Stm visit(Assign n) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Stm visit(ArrayAssign n) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PRINT visit(Print n) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AbstractExp visit(IntArrayType n) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AbstractExp visit(BooleanType n) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AbstractExp visit(IntegerType n) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AbstractExp visit(IdentifierType n) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AbstractExp visit(And n) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AbstractExp visit(LessThan n) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AbstractExp visit(Plus n) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AbstractExp visit(Minus n) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AbstractExp visit(Times n) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AbstractExp visit(ArrayLookup n) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AbstractExp visit(ArrayLength n) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AbstractExp visit(Call n) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AbstractExp visit(IntegerLiteral n) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AbstractExp visit(True n) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AbstractExp visit(False n) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AbstractExp visit(IdentifierExp n) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AbstractExp visit(This n) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AbstractExp visit(NewArray n) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AbstractExp visit(NewObject n) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AbstractExp visit(Not n) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AbstractExp visit(Identifier n) {
		// TODO Auto-generated method stub
		return null;
	}
	
}