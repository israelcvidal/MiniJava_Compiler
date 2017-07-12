package core.instruction_selection.assem;

import core.activation_records.temp.Label;
import core.activation_records.temp.Temp;
import core.activation_records.temp.TempList;

public class LABEL_ASSEM extends Instr {
   public Label label;

   public LABEL_ASSEM(String a, Label l) {
      assem=a; label=l;
   }

   public TempList use() {return null;}
   public TempList def() {return null;}
   public Targets jumps()     {return null;}

@Override
public void replaceUse(Temp s, Temp t) {
	// TODO Auto-generated method stub
	
}

@Override
public void replaceDef(Temp s, Temp t) {
	// TODO Auto-generated method stub
	
}

}
