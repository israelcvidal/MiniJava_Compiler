package core.activation_records.mips;

import core.activation_records.frame.Access;
import core.activation_records.temp.Temp;

public class InReg extends Access {
	Temp temp;
	
	public InReg(Temp t){
		temp = t;
	}
}
