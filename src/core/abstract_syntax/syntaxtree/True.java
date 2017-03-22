package core.abstract_syntax.syntaxtree;

import core.abstract_syntax.visitor.Visitor;
import core.abstract_syntax.visitor.TypeVisitor;

public class True extends Exp {
  public void accept(Visitor v) {
    v.visit(this);
  }

  public Type accept(TypeVisitor v) {
    return v.visit(this);
  }
}
