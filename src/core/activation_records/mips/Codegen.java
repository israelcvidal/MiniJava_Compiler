package core.activation_records.mips;

import core.activation_records.frame.Frame;
import core.activation_records.temp.Temp;
import core.instruction_selection.assem.Instr;
import core.instruction_selection.assem.InstrList;
import core.translation_to_IR.tree.AbstractExp;
import core.translation_to_IR.tree.Stm;

public class Codegen {
	private Frame frame;
	private InstrList ilist = null, last = null;
	
	public Codegen(Frame frame) {
		this.frame = frame;
	}
	
	private void emit(Instr inst) {
		if (last != null)
			last = last.tail = new InstrList(inst, null);
		else
			last = ilist = new InstrList(inst, null);
	}
	
	private void munchStm(Stm s) {
		
	}
	
	private Temp munchExp(AbstractExp e) {
		return null;
	}
	
	public void codegen(Stm s) {
		munchStm(s);
	}
	
	public Frame getFrame() {
		return frame;
	}
	
	public InstrList getCode() {
		return ilist;
	}
	
}
