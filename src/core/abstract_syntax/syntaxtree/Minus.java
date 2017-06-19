package core.abstract_syntax.syntaxtree;

import core.abstract_syntax.visitor.Visitor;
import core.translation_to_IR.tree.AbstractExp;
import devel.IR_translation.IRVisitor;
import core.abstract_syntax.visitor.TypeVisitor;

public class Minus extends Exp {
  public Exp e1,e2;
  
  public Minus(Exp ae1, Exp ae2) {
    e1=ae1; e2=ae2;
  }

  public void accept(Visitor v) {
    v.visit(this);
  }

  public Type accept(TypeVisitor v) {
    return v.visit(this);
  }

	@Override
	public AbstractExp accept(IRVisitor v) {
	    return v.visit(this);
	}

}
