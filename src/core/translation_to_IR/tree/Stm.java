package core.translation_to_IR.tree;

abstract public class Stm {
	abstract public ExpList kids();
	abstract public Stm build(ExpList kids);
}

