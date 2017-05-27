package core.translation_to_IR.tree;

import core.translation_to_IR.visitor.Visitor;

public class ESEQ extends AbstractExp {
  public Stm stm;
  public AbstractExp exp;
  public ESEQ(Stm s, AbstractExp e) {stm=s; exp=e;}
  public ExpList kids() {throw new Error("kids() not applicable to ESEQ");}
  public AbstractExp build(ExpList kids) {throw new Error("build() not applicable to ESEQ");}
  
  public void accpet(Visitor v){
	  v.visit(this);
  }
}

