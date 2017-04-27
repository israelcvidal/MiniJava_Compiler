package core.translation_to_IR.tree;

abstract public class AbstractExp {
	abstract public ExpList kids();
	abstract public AbstractExp build(ExpList kids);
}