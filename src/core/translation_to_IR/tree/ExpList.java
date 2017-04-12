package core.translation_to_IR.tree;

public class ExpList {
  public Exp head;
  public ExpList tail;
  public ExpList(Exp h, ExpList t) {head=h; tail=t;}
}