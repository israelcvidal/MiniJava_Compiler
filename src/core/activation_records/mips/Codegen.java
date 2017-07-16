package core.activation_records.mips;

import core.activation_records.frame.Frame;
import core.activation_records.temp.LabelList;
import core.activation_records.temp.Temp;
import core.activation_records.temp.TempList;
import core.instruction_selection.assem.OPER_ASSEM;
import core.instruction_selection.assem.MOVE_ASSEM;
import core.instruction_selection.assem.LABEL_ASSEM;
import core.instruction_selection.assem.Instr;
import core.instruction_selection.assem.InstrList;
import core.translation_to_IR.tree.AbstractExp;
import core.translation_to_IR.tree.BINOP;
import core.translation_to_IR.tree.CALL;
import core.translation_to_IR.tree.CJUMP;
import core.translation_to_IR.tree.CONST;
import core.translation_to_IR.tree.EXP;
import core.translation_to_IR.tree.ExpList;
import core.translation_to_IR.tree.JUMP;
import core.translation_to_IR.tree.LABEL;
import core.translation_to_IR.tree.MEM;
import core.translation_to_IR.tree.MOVE;
import core.translation_to_IR.tree.NAME;
import core.translation_to_IR.tree.Stm;
import core.translation_to_IR.tree.TEMP;

/**
 * This class implements instruction selection's phase.
 * @author daniel
 *
 */

public class Codegen {
	private MipsFrame frame;
	private InstrList ilist = null, last = null;
	
	public Codegen(MipsFrame frame) {
		this.frame = frame;
	}
	
	private void emit(Instr inst) {
		if (last != null)
			last = last.tail = new InstrList(inst, null);
		else
			last = ilist = new InstrList(inst, null);
	}
	
	private void munchStm(Stm s) {
		if 		(s instanceof CJUMP) 	{ munchStm((CJUMP) s); 	}
		else if (s instanceof EXP) 		{ munchStm((EXP) s); 	}
		else if (s instanceof JUMP) 	{ munchStm((JUMP) s);	}
		else if (s instanceof LABEL) 	{ munchStm((LABEL) s); 	}
		else if (s instanceof MOVE) 	{ munchStm((MOVE) s); 	}
	}
	
    private void munchStm(CJUMP s) {
        LabelList targets = new LabelList(s.iftrue, new LabelList(s.iffalse, null));
        String command = "";
        
        switch(s.relop) {
	        case CJUMP.EQ:
	        	command = "beq";
	        	break;
	        case CJUMP.NE:
	        	command = "bne";
	        	break;
	        case CJUMP.LT:
	        	command = "blt";
	        	break;
	        case CJUMP.GT:
	        	command = "bgt";
	        	break;
        }
        
        if (!(s.left instanceof CONST) && !(s.right instanceof CONST)) {
        	emit(new OPER_ASSEM("\t" + command + " `s0 `s1 " + s.iftrue.toString(), null, new TempList(munchExp(s.left), new TempList(munchExp(s.right), null)), targets));
        } else if (s.right instanceof CONST) {
    		emit(new OPER_ASSEM("\t" + command + " `s0 " + ((CONST)s.right).value + " " + s.iftrue.toString(), null, new TempList(munchExp(s.left), null), targets));
        } else {
    		CONST left = (CONST) s.left;
        	
    		s.left = s.right;
    		s.right = left;
    	
    		switch (s.relop) {
	    	    case CJUMP.EQ:
	    	    case CJUMP.NE:
	    	    	break;
	    	    case CJUMP.LT:
	    	    	s.relop = CJUMP.GT;
	    	    	break;
	    	    case CJUMP.GE:
	    	    	s.relop = CJUMP.LE;
	    	    	break;
	    	    case CJUMP.GT:
	    	    	s.relop = CJUMP.LT;
	    	    	break;
    		}
        	
        	emit(new OPER_ASSEM("\t" + command + " `s0 " + ((CONST)s.right).value + " " + s.iftrue.toString(), null, new TempList(munchExp(s.left), null), targets));
        }
	}
    
    private void munchStm(EXP s) { 
    	munchExp(s.exp); 
    }
    
    private void munchStm(JUMP s) {
        if (s.exp instanceof NAME)
        	emit(new OPER_ASSEM("\t" + "b " + ((NAME) s.exp).label.toString(), null, null, s.targets));
        else
        	emit(new OPER_ASSEM("\t" + "jr `s0", null, new TempList(munchExp(s.exp), null), s.targets));        
    }

    private void munchStm(LABEL l) {
        emit(new LABEL_ASSEM(l.label.toString() + ":", l.label));
    }
	
    private void munchStm(MOVE s) {
    	boolean emitted = false;
    	
    	if (s.dst instanceof TEMP) {
		    Temp dstTemp = ((TEMP) s.dst).temp;
		    
		    if (s.src instanceof MEM) {
		    	MEM mem = (MEM) s.src;
		    	
		    	if (mem.exp instanceof CONST) {
		    		emitted = true;
		    		emit(new OPER_ASSEM("\t" + "lw `d0 " + ((CONST) mem.exp).value + "(`s0)", new TempList(dstTemp, null), new TempList(frame.ZERO(), null), null));
		    	} else if (mem.exp instanceof BINOP && ((BINOP) mem.exp).binop == BINOP.PLUS) {
					BINOP plus = (BINOP) mem.exp;
					
			        if (plus.left instanceof CONST && !(plus.right instanceof CONST)) {
			    		CONST left = (CONST) plus.left;
			        	
			    		plus.left = plus.right;
			    		plus.right = left;
			        } 
			        
			        if (plus.right instanceof CONST) {
				        int rightConst = ((CONST) plus.right).value;
				        String offset = Integer.toString(rightConst);
				        Temp left = null;
				        
				        if (plus.left instanceof TEMP)
				        	left = ((TEMP) plus.left).temp;
				        else
				        	munchExp(plus.left);					    
					    
					    if (left == frame.FP()) {
					    	left = frame.SP();
					    	offset += "+" + frame.name + "_framesize";
					    }
					    
					    emitted = true;
					    emit(new OPER_ASSEM("\t" + "lw `d0 " + offset + "(`s0)", new TempList(dstTemp, null), new TempList(left, null), null));
					}
			    }
		    	
		    	if (mem.exp instanceof TEMP && ((TEMP) mem.exp).temp == frame.FP()) {
			    	emit(new OPER_ASSEM("\t" + "lw `d0 " + frame.name + "_framesize" + "(`s0)", new TempList(dstTemp, null), new TempList(frame.SP(), null), null));
			    } else if (!emitted) {
			    	emit(new OPER_ASSEM("\t" + "lw `d0 (`s0)", new TempList(dstTemp, null), new TempList(munchExp(mem.exp), null), null));
			    }
		    } else {
		    	emit(new MOVE_ASSEM("\t" + "move `d0 `s0", dstTemp, munchExp(s.src)));
		    }
    	} else {
		    MEM mem = (MEM) s.dst;
	
	    	if (mem.exp instanceof CONST) {
	    		emitted = true;
	    		emit(new OPER_ASSEM("\t" + "sw `s0 " + ((CONST) mem.exp).value + "(`s1)", null, new TempList(munchExp(s.src), new TempList(frame.ZERO(), null)), null));
	    	} else if (mem.exp instanceof BINOP && ((BINOP) mem.exp).binop == BINOP.PLUS) {
				BINOP plus = (BINOP) mem.exp;
				
		        if (plus.left instanceof CONST && !(plus.right instanceof CONST)) {
		    		CONST left = (CONST) plus.left;
		        	
		    		plus.left = plus.right;
		    		plus.right = left;
		        }
		        
		        if (plus.right instanceof CONST) {
			        int rightConst = ((CONST) plus.right).value;
			        String offset = Integer.toString(rightConst);
			        Temp left = null;
			        
			        if (plus.left instanceof TEMP)
			        	left = ((TEMP) plus.left).temp;
			        else
			        	munchExp(plus.left);					    
				    
				    if (left == frame.FP()) {
				    	left = frame.SP();
				    	offset += "+" + frame.name + "_framesize";
				    }
				    
				    emitted = true;				    
				    emit(new OPER_ASSEM("\t" + "sw `s0 " + offset + "(`s1)", null, new TempList(munchExp(s.src), new TempList(left, null)), null));
				}
		    }
	    	
	    	if (mem.exp instanceof TEMP && ((TEMP) mem.exp).temp == frame.FP()) {
		    	emit(new OPER_ASSEM("\t" + "sw `s0 " + frame.name + "_framesize" + "(`s1)", null, new TempList(munchExp(s.src), new TempList(frame.SP(), null)), null));
		    } else if (!emitted) {
		    	emit(new OPER_ASSEM("\t" + "sw `s0 (`s1)", null, new TempList(munchExp(s.src), new TempList(munchExp(mem.exp), null)), null));
		    }
    	}
    }
    
	private Temp munchExp(AbstractExp e) {
		Temp result = null;
		
		if 		(e instanceof BINOP) 	{ result = munchExp((BINOP) e); }
		else if (e instanceof CALL) 	{ result = munchExp((CALL) e); 	}
		else if (e instanceof CONST) 	{ result = munchExp((CONST) e); }
		else if (e instanceof MEM) 		{ result = munchExp((MEM) e); 	}
		else if (e instanceof NAME) 	{ result = munchExp((NAME) e); 	}
		else if (e instanceof TEMP) 	{ result = munchExp((TEMP) e); 	}
		
		return result;
	}

    private Temp munchExp(BINOP e) {
        Temp result = new Temp();
    	String command = "";
    	
    	switch (e.binop) {
	    	case BINOP.PLUS:
	    		command = "add";
	    		break;
	    	case BINOP.MINUS:
	    		command = "sub";
	    		break;
	    	case BINOP.MUL:
	    		command = "mulo";
	    		break;
	    	case BINOP.AND:
	    		command = "and";
	    		break;
    	}
        
    	emit(new OPER_ASSEM("\t" + command + " `d0 `s0 `s1", new TempList(result, null), new TempList(munchExp(e.left), new TempList(munchExp(e.right), null)), null));

    	return result;
    }

    private Temp munchExp(CALL s) {
    	int offset = 0;
    	Temp V0 = MipsFrame.V0;
		TempList uses = new TempList(null, null);
		TempList usesIt = null;
		ExpList args = s.args;
		
		if (args != null)
			V0 = munchExp(args.head);
		
		if (V0 != frame.ZERO()) {
		    emit(new MOVE_ASSEM("\t" + "move `d0 `s0", frame.RV(), V0));
		    uses.head = frame.RV();
		    usesIt = uses.tail = new TempList(null, null);
		}
		
		if (args != null)
			args = args.tail;
		
		if (args != null) {
		    offset += frame.wordSize();
		    emit(new MOVE_ASSEM("\t" + "move `d0 `s0", MipsFrame.A0, munchExp(args.head)));
		    usesIt.head = MipsFrame.A0;
		    usesIt = usesIt.tail = new TempList(null, null);
		    args = args.tail;
		}
		
		if (args != null) {
		    offset += frame.wordSize();
		    emit(new MOVE_ASSEM("\t" + "move `d0 `s0", MipsFrame.A1, munchExp(args.head)));
		    usesIt.head = MipsFrame.A1;
		    usesIt = usesIt.tail = new TempList(null, null);
		    args = args.tail;
		}
		
		if (args != null) {
		    offset += frame.wordSize();
		    emit(new MOVE_ASSEM("\t" + "move `d0 `s0", MipsFrame.A2, munchExp(args.head)));
		    usesIt.head = MipsFrame.A2;
		    usesIt = usesIt.tail = new TempList(null, null);
		    args = args.tail;
		}
		
		if (args != null) {
		    offset += frame.wordSize();
		    emit(new MOVE_ASSEM("\t" + "move `d0 `s0", MipsFrame.A3, munchExp(args.head)));
		    usesIt.head = MipsFrame.A3;
		    usesIt.tail = null;
		    args = args.tail;
		}
		
		while (args != null) {
		    offset += frame.wordSize();
		    emit(new OPER_ASSEM("\t" + "sw `s0 " + offset + "(`s1)", null, new TempList(munchExp(args.head), new TempList(frame.SP(), null)), null));
		    args = args.tail;
		}
	
		if (offset > frame.maxArgOffset)
		    frame.maxArgOffset = offset;
	
		TempList calldefsList = null, calldefsIt = null;
		
		if (MipsFrame.calldefs.length > 0) {
			calldefsList = new TempList(MipsFrame.calldefs[0], null);
			calldefsIt = calldefsList;
		}
	
		for (int i = 1; i < MipsFrame.calldefs.length; i++) {
			calldefsIt = calldefsIt.tail = new TempList(null, null);
			calldefsIt.head = MipsFrame.calldefs[i];
		}
	
		calldefsIt.tail = null;
		
        if (s.func instanceof NAME) {
        	emit(new OPER_ASSEM("\t" + "jal " + ((NAME) s.func).label.toString(), calldefsList, uses, null));
        } else {
        	uses = new TempList(munchExp(s.func), uses);
        	emit(new OPER_ASSEM("\t" + "jal `s0", calldefsList, uses, null));
        }

		return MipsFrame.V0;
    }
    
    private Temp munchExp(CONST e) {
    	Temp result = frame.ZERO();
    	
    	if (e.value != 0)
    		result = new Temp();
	
    	emit(new OPER_ASSEM("\t" + "li `d0 " + e.value, new TempList(result, null), null, null));
    	
    	return result;
    }
    
    private Temp munchExp(MEM e) {
    	boolean emitted = false;
        Temp result = new Temp();

    	if (e.exp instanceof CONST) {
    		emitted = true;
    		emit(new OPER_ASSEM("\t" + "lw `d0 " + ((CONST) e.exp).value + "(`s0)", new TempList(result, null), new TempList(frame.ZERO(), null), null));
    	} else if (e.exp instanceof BINOP && ((BINOP) e.exp).binop == BINOP.PLUS) {
			BINOP plus = (BINOP) e.exp;
			
	        if (plus.left instanceof CONST && !(plus.right instanceof CONST)) {
	    		CONST left = (CONST) plus.left;
	        	
	    		plus.left = plus.right;
	    		plus.right = left;
	        } 
	        
	        if (plus.right instanceof CONST) {
		        int rightConst = ((CONST) plus.right).value;
		        String offset = Integer.toString(rightConst);
		        Temp left = null;
		        
		        if (plus.left instanceof TEMP)
		        	left = ((TEMP) plus.left).temp;
		        else
		        	munchExp(plus.left);					    
			    
			    if (left == frame.FP()) {
			    	left = frame.SP();
			    	offset += "+" + frame.name + "_framesize";
			    }
			    
			    emitted = true;
			    emit(new OPER_ASSEM("\t" + "lw `d0 " + offset + "(`s0)", new TempList(result, null), new TempList(left, null), null));
			}
	    }
	    	
    	if (e.exp instanceof TEMP && ((TEMP) e.exp).temp == frame.FP()) {
	    	emit(new OPER_ASSEM("\t" + "lw `d0 " + frame.name + "_framesize" + "(`s0)", new TempList(result, null), new TempList(frame.SP(), null), null));
	    } else if (!emitted) {
	    	emit(new OPER_ASSEM("\t" + "lw `d0 (`s0)", new TempList(result, null), new TempList(munchExp(e.exp), null), null));
	    }

	    return result;
    }

    private Temp munchExp(NAME e) {
    	Temp result = new Temp();
    	
    	emit(new OPER_ASSEM("\t" + "la `d0 " + e.label.toString(), new TempList(result, null), null, null));
    	
    	return result;
    }

    private Temp munchExp(TEMP e) {
    	Temp result = e.temp;
    	
		if (result == frame.FP()) {
		    result = new Temp();

		    emit(new OPER_ASSEM("\t" + "addu `d0 `s0 " + frame.name + "_framesize", new TempList(result, null), new TempList(frame.SP(), null), null));
		}
		
		return result;
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
