package core.activation_records.mips;

import core.activation_records.frame.Access;
import core.translation_to_IR.tree.AbstractExp;
import core.translation_to_IR.tree.BINOP;
import core.translation_to_IR.tree.CONST;
import core.translation_to_IR.tree.MEM;

public class InFrame extends Access {
    int offset;
    InFrame(int o) {
	offset = o;
    }

    public AbstractExp exp(AbstractExp fp) {
        return new MEM
	    (new BINOP(BINOP.PLUS, fp, new CONST(offset)));
    }

    public String toString() {
        Integer offset = new Integer(this.offset);
	return offset.toString();
    }
}