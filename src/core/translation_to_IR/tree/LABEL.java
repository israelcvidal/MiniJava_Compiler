package core.translation_to_IR.tree;

import core.activation_records.temp.Label;
import core.translation_to_IR.visitor.Visitor;

public class LABEL extends Stm { 
  public Label label;
  public LABEL(Label l) {label=l;}
  public ExpList kids() {return null;}
  public Stm build(ExpList kids) {
    return this;
  }
  
  public void accpet(Visitor v){
	  v.visit(this);
  }
}

