package core.activation_records.mips;

import core.activation_records.frame.Access;
import core.activation_records.frame.AccessList;
import core.activation_records.frame.Frame;
import core.activation_records.temp.Label;
import core.activation_records.temp.Temp;
import core.activation_records.util.BoolList;

public class Mips implements Frame {
	
	public Label name;
	public AccessList formals;
	private int lastOffset;
	
	public Mips(Label n, BoolList f){
		name = n;
		formals = new AccessList();
		lastOffset = 0;
		
		while (f!=null){
			formals.addElement(allocLocal(f.head));
			//look the next element
			f = f.tail;
		}
		
	}
	
	@Override
	public Access allocLocal(boolean escape) {
		Access a;
		
		//if escape, add in memory
		if (escape){
			a = new InFrame(lastOffset);
			//increment offset
			lastOffset++;
		}
		//add in register
		else{
			a = new InReg(new Temp());
		}
		
		return a;
	}

}
