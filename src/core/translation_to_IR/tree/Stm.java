package core.translation_to_IR.tree;

import core.translation_to_IR.visitor.Visitor;

abstract public class Stm {
	abstract public ExpList kids();
	abstract public Stm build(ExpList kids);
	
	abstract public void accpet(Visitor v);
}

