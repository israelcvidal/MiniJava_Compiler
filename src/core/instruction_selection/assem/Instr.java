package core.instruction_selection.assem;

import core.activation_records.temp.Temp;
import core.activation_records.temp.TempList;
import core.activation_records.temp.TempMap;
import core.activation_records.temp.Label;
import core.activation_records.temp.LabelList;

public abstract class Instr {
  public String assem;
  public abstract TempList use();
  public abstract TempList def();
  public abstract void replaceUse(Temp s, Temp t);
  public abstract void replaceDef(Temp s, Temp t);
  public abstract Targets jumps();

  private Temp nthTemp(TempList l, int i) {
    if (i==0) return l.head;
    else return nthTemp(l.tail,i-1);
  }

  private Label nthLabel(LabelList l, int i) {
    if (i==0) return l.head;
    else return nthLabel(l.tail,i-1);
  }

public String format(TempMap m) {
    TempList dst = def();
    TempList src = use();
    Targets j = jumps();
    LabelList jump = (j==null)?null:j.labels;
    StringBuffer s = new StringBuffer();
    int len = assem.length();
    for(int i=0; i<len; i++)
	if (assem.charAt(i)=='`')
	   switch(assem.charAt(++i)) {
              case 's': {int n = Character.digit(assem.charAt(++i),10);
			 s.append(m.tempMap(nthTemp(src,n)));
			}
			break;
	      case 'd': {int n = Character.digit(assem.charAt(++i),10);
			 s.append(m.tempMap(nthTemp(dst,n)));
			}
 			break;
	      case 'j': {int n = Character.digit(assem.charAt(++i),10);
			 s.append(nthLabel(jump,n).toString());
			}
 			break;
	      case '`': s.append('`'); 
			break;
              default: throw new Error("bad Assem format");
       }
       else s.append(assem.charAt(i));

    return s.toString();
  }


}
