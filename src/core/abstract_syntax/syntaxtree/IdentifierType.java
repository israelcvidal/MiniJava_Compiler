package core.abstract_syntax.syntaxtree;

import core.abstract_syntax.visitor.Visitor;
import devel.semantic_analysis.TypeCheckVisitor;
import core.abstract_syntax.visitor.TypeVisitor;

public class IdentifierType extends Type {
  public String s;

  public IdentifierType(String as) {
    s=as;
  }

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
