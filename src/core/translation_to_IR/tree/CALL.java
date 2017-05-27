package core.translation_to_IR.tree;

import core.translation_to_IR.visitor.Visitor;

public class CALL extends AbstractExp {
  public AbstractExp func;
  public ExpList args;
  public CALL(AbstractExp f, ExpList a) {func=f; args=a;}
  public ExpList kids() {return new ExpList(func,args);}
  public AbstractExp build(ExpList kids) {
    return new CALL(kids.head,kids.tail);
  }
  
  public void accpet(Visitor v){
	  v.visit(this);
  }
  
}

