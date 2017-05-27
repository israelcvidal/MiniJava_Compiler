package core.translation_to_IR.visitor;

import core.translation_to_IR.tree.*;

public interface Visitor {
	public void visit(BINOP binop);
	public void visit(CALL call);
	public void visit(CJUMP cjump);
	public void visit(CONST con);
	public void visit(ESEQ eseq);
	public void visit(EXP exp);
	public void visit(JUMP jump);
	public void visit(LABEL label);
	public void visit(MEM mem);
	public void visit(MOVE move);
	public void visit(NAME name);
	public void visit(Print print);
	public void visit(SEQ seq);
	public void visit(TEMP temp);
}
