package core.canonical_trees;

import core.activation_records.temp.Label;
import core.translation_to_IR.tree.CJUMP;
import core.translation_to_IR.tree.JUMP;
import core.translation_to_IR.tree.LABEL;
import core.translation_to_IR.tree.Stm;
import core.translation_to_IR.tree.StmList;

public class BasicBlocks {
  public StmListList blocks;
  public Label done;

  private StmListList lastBlock;
  private StmList lastStm;

  private void addStm(Stm s) {
	lastStm = lastStm.tail = new StmList(s,null);
  }

  private void doStms(StmList l) {
      if (l==null) 
	doStms(new StmList(new JUMP(done), null));
      else if (l.head instanceof JUMP 
	      || l.head instanceof CJUMP) {
	addStm(l.head);
	mkBlocks(l.tail);
      } 
      else if (l.head instanceof LABEL)
           doStms(new StmList(new JUMP(((LABEL)l.head).label), 
	  			   l));
      else {
	addStm(l.head);
	doStms(l.tail);
      }
  }

  public void mkBlocks(StmList l) {
     if (l==null) return;
     else if (l.head instanceof LABEL) {
	lastStm = new StmList(l.head,null);
        if (lastBlock==null)
  	   lastBlock= blocks= new StmListList(lastStm,null);
        else
  	   lastBlock = lastBlock.tail = new StmListList(lastStm,null);
	doStms(l.tail);
     }
     else mkBlocks(new StmList(new LABEL(new Label()), l));
  }
   

  public BasicBlocks(StmList stms) {
    done = new Label();
    mkBlocks(stms);
  }
}
