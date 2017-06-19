package core.abstract_syntax.syntaxtree;

import core.abstract_syntax.visitor.Visitor;
import core.translation_to_IR.tree.AbstractExp;
import devel.IR_translation.IRVisitor;
import core.abstract_syntax.visitor.TypeVisitor;

public class Identifier {
  public String s;

  public Identifier(String as) { 
    s=as;
  }

  public void accept(Visitor v) {
    v.visit(this);
  }

  public Type accept(TypeVisitor v) {
    return v.visit(this);
  }

  public AbstractExp accept(IRVisitor v) {
	    return v.visit(this);
	  }
  
  public String toString(){
    return s;
  }
}
