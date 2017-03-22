package core.abstract_syntax.syntaxtree;

import core.abstract_syntax.visitor.Visitor;
import core.abstract_syntax.visitor.TypeVisitor;

public class IntegerLiteral extends Exp {
  public int i;

  public IntegerLiteral(int ai) {
    i=ai;
  }

  public void accept(Visitor v) {
    v.visit(this);
  }

  public Type accept(TypeVisitor v) {
    return v.visit(this);
  }
}
