package core.translation_to_IR.tree;

import core.translation_to_IR.visitor.Visitor;

public class CONST extends AbstractExp {
  public int value;
  public CONST(int v) {value=v;}
  public ExpList kids() {return null;}
  public AbstractExp build(ExpList kids) {return this;}
  
  public void accpet(Visitor v){
	  v.visit(this);
  }
}

