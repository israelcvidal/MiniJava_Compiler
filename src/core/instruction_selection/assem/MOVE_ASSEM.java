package core.instruction_selection.assem;

import core.activation_records.temp.Temp;
import core.activation_records.temp.TempList;

public class MOVE_ASSEM extends Instr {
   public Temp dst;   
   public Temp src;

   public MOVE_ASSEM(String a, Temp d, Temp s) {
      assem=a; dst=d; src=s;
   }
   public TempList use() {return new TempList(src,null);}
   public TempList def() {return new TempList(dst,null);}
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
