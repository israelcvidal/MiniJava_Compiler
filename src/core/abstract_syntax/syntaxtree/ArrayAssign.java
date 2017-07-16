package core.abstract_syntax.syntaxtree;

import core.abstract_syntax.visitor.Visitor;
import core.translation_to_IR.tree.Stm;
import devel.IR_translation.IRVisitor;
import core.abstract_syntax.visitor.TypeVisitor;

public class ArrayAssign extends Statement {
  public Identifier i;
  public Exp e1,e2;

  public ArrayAssign(Identifier ai, Exp ae1, Exp ae2) {
    i=ai; e1=ae1; e2=ae2;
  }

  public void accept(Visitor v) {
    v.visit(this);
  }

  public Type accept(TypeVisitor v) {
    return v.visit(this);
  }

	@Override
	public Stm accept(IRVisitor v) {
		return v.visit(this);
	}

}

