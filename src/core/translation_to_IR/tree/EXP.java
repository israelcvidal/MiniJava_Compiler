package core.translation_to_IR.tree;

public class EXP extends Stm {
	  public AbstractExp exp; 
	  public EXP(AbstractExp e) {exp=e;}
	  public ExpList kids() {return new ExpList(exp,null);}
	  public Stm build(ExpList kids) {
	    return new EXP(kids.head);
	  }
	  
	}