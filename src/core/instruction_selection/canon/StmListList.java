package core.instruction_selection.canon;

import core.translation_to_IR.tree.StmList;

public class StmListList {
  public StmList head;
  public StmListList tail;
  public StmListList(StmList h, StmListList t) {head=h; tail=t;}
}

