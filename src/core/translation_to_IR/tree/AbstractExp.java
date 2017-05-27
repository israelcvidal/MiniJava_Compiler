package core.translation_to_IR.tree;

import core.translation_to_IR.visitor.Visitor;

abstract public class AbstractExp {
	abstract public ExpList kids();
	abstract public AbstractExp build(ExpList kids);
	
}