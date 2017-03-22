package core.abstract_syntax.syntaxtree;

import core.abstract_syntax.visitor.Visitor;
import core.abstract_syntax.visitor.TypeVisitor;

public abstract class Exp {
  public abstract void accept(Visitor v);
  public abstract Type accept(TypeVisitor v);
}
