package core.abstract_syntax.syntaxtree;

import core.abstract_syntax.visitor.Visitor;
import core.translation_to_IR.tree.AbstractExp;
import devel.IR_translation.IRVisitor;
import core.abstract_syntax.visitor.TypeVisitor;

public abstract class Exp {
  public abstract void accept(Visitor v);
  public abstract Type accept(TypeVisitor v);
  public abstract AbstractExp accept(IRVisitor v);
}
