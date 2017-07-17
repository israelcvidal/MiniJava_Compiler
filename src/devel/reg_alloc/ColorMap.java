package devel.reg_alloc;

import java.util.HashMap;

import core.activation_records.mips.MipsFrame;
import core.activation_records.temp.Temp;
import core.activation_records.temp.TempMap;

public class ColorMap implements TempMap {
	private HashMap<String, Integer> colors;
	
	public ColorMap(HashMap<String, Integer> colors) {
		this.colors = colors;
	}
	
	@Override
	public String tempMap(Temp t) {
		if (MipsFrame.tempMap.containsKey(t))
			return MipsFrame.tempMap.get(t);
		
		int color = 7;
		
		if (t == null)
			return MipsFrame.tempMap.get(MipsFrame.registers()[color]);
		
		if (colors.get(t.toString()) != null)
			color = colors.get(t.toString());
		
//		if (color >= MipsFrame.registers().length)
//			color = color - MipsFrame.registers().length;
		
		Temp reg = MipsFrame.registers()[color];
		
		return MipsFrame.tempMap.get(reg);
	}

}
