package core.abstract_syntax.syntaxtree;

import core.abstract_syntax.visitor.Visitor;
import devel.semantic_analysis.TypeTable;
import devel.semantic_analysis.TypeCheckVisitor;
import core.abstract_syntax.visitor.TypeVisitor;

public class VarDecl {
  public Type t;
  public Identifier i;
  
  public VarDecl(Type at, Identifier ai) {
    t=at; i=ai;
  }

  public void accept(Visitor v) {
    v.visit(this);
  }

  public Type accept(TypeVisitor v) {
    return v.visit(this);
  }
  
  public Type accept(TypeCheckVisitor v) {
	  return v.visit(this);
  }
}
