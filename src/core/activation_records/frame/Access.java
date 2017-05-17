package core.activation_records.frame;

import core.translation_to_IR.tree.AbstractExp;

public abstract class Access {
	  public abstract String toString();
	  public abstract AbstractExp exp(AbstractExp e);
}
