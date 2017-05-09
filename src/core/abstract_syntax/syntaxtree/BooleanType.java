package core.abstract_syntax.syntaxtree;

import core.abstract_syntax.visitor.Visitor;
import devel.semantic_analysis.TypeCheckVisitor;
import core.abstract_syntax.visitor.TypeVisitor;

public class BooleanType extends Type {
  public void accept(Visitor v) {
    v.visit(this);
  }

  public Type accept(TypeVisitor v) {
    return v.visit(this);
  }
  
//  public Type accept(TypeCheckVisitor v) {
//	  return v.visit(this);
//  }
}
