package core.translation_to_IR.tree;

import core.translation_to_IR.visitor.Visitor;

public class MEM extends AbstractExp {
  public AbstractExp exp;
  public MEM(AbstractExp e) {exp=e;}
  public ExpList kids() {return new ExpList(exp,null);}
  public AbstractExp build(ExpList kids) {
    return new MEM(kids.head);
  }
  
  public void accpet(Visitor v){
	  v.visit(this);
  }
}

