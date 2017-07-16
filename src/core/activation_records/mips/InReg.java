package core.activation_records.mips;

import core.activation_records.frame.Access;
import core.activation_records.temp.Temp;
import core.translation_to_IR.tree.AbstractExp;
import core.translation_to_IR.tree.TEMP;

public class InReg extends Access {
    Temp temp;
    InReg(Temp t) {
	temp = t;
    }

    public AbstractExp exp(AbstractExp fp) {
        return new TEMP(temp);
    }

    public String toString() {
        return temp.toString();
    }
}