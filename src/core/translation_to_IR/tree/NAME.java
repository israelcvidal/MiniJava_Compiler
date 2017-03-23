package core.translation_to_IR.tree;

import core.activation_records.temp.Label;

public class NAME extends Exp {
  public Label label;
  public NAME(Label l) {label=l;}
  public ExpList kids() {return null;}
  public Exp build(ExpList kids) {return this;}
}

