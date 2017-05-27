package core.translation_to_IR.tree;

import core.activation_records.temp.Temp;
import core.translation_to_IR.visitor.Visitor;

public class TEMP extends AbstractExp {
  public Temp temp;
  public TEMP(Temp t) {temp=t;}
  public ExpList kids() {return null;}
  public AbstractExp build(ExpList kids) {return this;}
  
  
  public void accpet(Visitor v){
	  v.visit(this);
  }
}

