package core.abstract_syntax.syntaxtree;

import core.abstract_syntax.visitor.Visitor;
import core.translation_to_IR.tree.Stm;
import devel.IR_translation.IRVisitor;
import devel.semantic_analysis.ClassTable;
import devel.semantic_analysis.TypeCheckVisitor;
import core.abstract_syntax.visitor.TypeVisitor;

public abstract class ClassDecl {
  public abstract void accept(Visitor v);
  public abstract Type accept(TypeVisitor v);
  public abstract ClassTable accept(TypeCheckVisitor v);
  public abstract Stm accept(IRVisitor v);
}
