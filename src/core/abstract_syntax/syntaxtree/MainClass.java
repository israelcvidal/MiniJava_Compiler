package core.abstract_syntax.syntaxtree;

import core.abstract_syntax.visitor.Visitor;
import core.translation_to_IR.tree.Stm;
import devel.IR_translation.IRVisitor;
import devel.semantic_analysis.ClassTable;
import devel.semantic_analysis.TypeCheckVisitor;
import core.abstract_syntax.visitor.TypeVisitor;

public class MainClass {
  public Identifier i1,i2;
  public Statement s;

  public MainClass(Identifier ai1, Identifier ai2, Statement as) {
    i1=ai1; i2=ai2; s=as;
  }

  public void accept(Visitor v) {
    v.visit(this);
  }

  public Type accept(TypeVisitor v) {
    return v.visit(this);
  }
  
  public ClassTable accept(TypeCheckVisitor v) {
	  return v.visit(this);
  }
  
  public Stm accept(IRVisitor v) {
	  return v.visit(this);
  }
  
}

