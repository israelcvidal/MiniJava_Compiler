package core.translation_to_IR.tree;

public class ExpList {
  public AbstractExp head;
  public ExpList tail;
  public ExpList(AbstractExp h, ExpList t) {head=h; tail=t;}
}