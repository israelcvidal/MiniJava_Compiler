package core.translation_to_IR.tree;

abstract public class Exp {
	abstract public ExpList kids();
	abstract public Exp build(ExpList kids);
}