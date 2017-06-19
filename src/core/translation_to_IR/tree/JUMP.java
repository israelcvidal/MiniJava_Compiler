package core.translation_to_IR.tree;

import core.activation_records.temp.Label;
import core.activation_records.temp.LabelList;

public class JUMP extends Stm {
  public AbstractExp exp;
  public LabelList targets;
  public JUMP(AbstractExp e, LabelList t) {exp=e; targets=t;}
  public JUMP(Label target) {
      this(new NAME(target), new LabelList(target,null));
  }
  public ExpList kids() {return new ExpList(exp,null);}
  public Stm build(ExpList kids) {
    return new JUMP(kids.head,targets);
  }
 
}

