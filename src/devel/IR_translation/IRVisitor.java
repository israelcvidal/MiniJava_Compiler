package devel.IR_translation;

import java.util.List;

import core.abstract_syntax.syntaxtree.*;
import core.translation_to_IR.tree.AbstractExp;
import core.translation_to_IR.tree.Stm;

/**
 * This interface defines a set of methods that must be implemented for IR tree generating.
 * @author daniel
 *
 */

public interface IRVisitor {
	public List<Frag> visit(Program n);
	public Stm visit(MainClass n);
	public Stm visit(ClassDeclSimple n);
	public Stm visit(ClassDeclExtends n);
	public Stm visit(VarDecl n);
	public Stm visit(MethodDecl n);
	public Stm visit(Formal n);
	public Stm visit(IntArrayType n);
	public Stm visit(BooleanType n);
	public Stm visit(IntegerType n);
	public AbstractExp visit(IdentifierType n);
	public Stm visit(Block n);
	public Stm visit(If n);
	public Stm visit(While n);
	public Stm visit(Print n);
	public Stm visit(Assign n);
	public Stm visit(ArrayAssign n);
	public AbstractExp visit(And n);
	public AbstractExp visit(LessThan n);
	public AbstractExp visit(Plus n);
	public AbstractExp visit(Minus n);
	public AbstractExp visit(Times n);
	public AbstractExp visit(ArrayLookup n);
	public AbstractExp visit(ArrayLength n);
	public AbstractExp visit(Call n);
	public AbstractExp visit(IntegerLiteral n);
	public AbstractExp visit(True n);
	public AbstractExp visit(False n);
	public AbstractExp visit(IdentifierExp n);
	public AbstractExp visit(This n);
	public AbstractExp visit(NewArray n);
	public AbstractExp visit(NewObject n);
	public AbstractExp visit(Not n);
	public AbstractExp visit(Identifier n);
}
