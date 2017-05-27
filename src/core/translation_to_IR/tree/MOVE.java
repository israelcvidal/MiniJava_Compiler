package core.translation_to_IR.tree;

import core.translation_to_IR.visitor.Visitor;

public class MOVE extends Stm {
  public AbstractExp dst, src;
  public MOVE(AbstractExp d, AbstractExp s) {dst=d; src=s;}
  public ExpList kids() {
        if (dst instanceof MEM)
	   return new ExpList(((MEM)dst).exp, new ExpList(src,null));
	else return new ExpList(src,null);
  }
  public Stm build(ExpList kids) {
        if (dst instanceof MEM)
	   return new MOVE(new MEM(kids.head), kids.tail.head);
	else return new MOVE(dst, kids.head);
  }
  
  public void accpet(Visitor v){
	  v.visit(this);
  }
}

