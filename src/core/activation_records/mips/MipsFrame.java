package core.activation_records.mips;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

import core.activation_records.frame.Access;
import core.activation_records.frame.Frame;
import core.activation_records.temp.Label;
import core.activation_records.temp.Temp;
import core.activation_records.temp.TempList;
import core.instruction_selection.assem.Instr;
import core.instruction_selection.assem.InstrList;
import core.instruction_selection.assem.OPER_ASSEM;
import core.translation_to_IR.tree.AbstractExp;
import core.translation_to_IR.tree.CALL;
import core.translation_to_IR.tree.CONST;
import core.translation_to_IR.tree.ExpList;
import core.translation_to_IR.tree.MOVE;
import core.translation_to_IR.tree.NAME;
import core.translation_to_IR.tree.SEQ;
import core.translation_to_IR.tree.Stm;
import core.translation_to_IR.tree.StmList;
import core.translation_to_IR.tree.TEMP;
import devel.semantic_analysis.Symbol;

public class MipsFrame extends Frame {

    //Mini Java Library will be appended to end of 
    //program
    public String programTail(){
	
	return      
	    "         .text            \n" +
	    "         .globl _halloc   \n" +
	    "_halloc:                  \n" +
	    "         li $v0, 9        \n" +
	    "         syscall          \n" +
	    "         j $ra            \n" +
	    "                          \n" +
	    "         .text            \n" +
	    "         .globl _printint \n" +
	    "_printint:                \n" +
	    "         li $v0, 1        \n" +
	    "         syscall          \n" +
	    "         la $a0, newl     \n" +
	    "         li $v0, 4        \n" +
	    "         syscall          \n" +
	    "         j $ra            \n" +
	    "                          \n" +
	    "         .data            \n" +
	    "         .align   0       \n" +
	    "newl:    .asciiz \"\\n\"  \n" +
	    "         .data            \n" +
	    "         .align   0       \n" +
	    "str_er:  .asciiz \" ERROR: abnormal termination\\n\" "+
	    "                          \n" +
	    "         .text            \n" +
	    "         .globl _error    \n" +
	    "_error:                   \n" +
	    "         li $v0, 4        \n" +
	    "         la $a0, str_er   \n" +
	    "         syscall          \n" +
	    "         li $v0, 10       \n" +
	    "         syscall          \n" ;
    }
    
    public Frame newFrame(Symbol name, List<Boolean> formals) {
        if (this.name != null) name = Symbol.symbol(this.name + "." + name);
        return new MipsFrame(name, formals);
    }

    public MipsFrame() {}

    private static HashMap<Symbol,Integer> functions
	= new HashMap<Symbol,Integer>();
    private MipsFrame(Symbol n, List<Boolean> f) {
	Integer count = functions.get(n);
	if (count == null) {
	    count = new Integer(0);
	    name = new Label(n);
	} else {
	    count = new Integer(count.intValue() + 1);
	    name = new Label(n + "." + count);
	}
	functions.put(n, count);
        actuals = new LinkedList<Access>();
	formals = new LinkedList<Access>();
	int offset = 0;
        Iterator<Boolean> escapes = f.iterator();
	formals.add(allocLocal(escapes.next().booleanValue()));
	actuals.add(new InReg(V0));
	for (int i = 0; i < argRegs.length; ++i) {
	    if (!escapes.hasNext())
		break;
	    offset += wordSize;
	    actuals.add(new InReg(argRegs[i]));
	    if (escapes.next().booleanValue())
		formals.add(new InFrame(offset));
	    else
		formals.add(new InReg(new Temp()));
	}
	while (escapes.hasNext()) {
	    offset += wordSize;
            Access actual = new InFrame(offset);
	    actuals.add(actual);
	    if (escapes.next().booleanValue())
		formals.add(actual);
	    else
		formals.add(new InReg(new Temp()));
	}
    }

    private static final int wordSize = 4;
    public int wordSize() { return wordSize; }

    private int offset = 0;
    public Access allocLocal(boolean escape) {
	if (escape) {
	    Access result = new InFrame(offset);
	    offset -= wordSize;
	    return result;
	} else
	    return new InReg(new Temp());
    }

    static final Temp ZERO = new Temp(); // zero reg
    static final Temp AT = new Temp(); // reserved for assembler
    static final Temp V0 = new Temp(); // function result
    static final Temp V1 = new Temp(); // second function result
    public static final Temp A0 = new Temp(); // argument1
    public static final Temp A1 = new Temp(); // argument2
    public static final Temp A2 = new Temp(); // argument3
    public static final Temp A3 = new Temp(); // argument4
    static final Temp T0 = new Temp(); // caller-saved
    static final Temp T1 = new Temp();
    static final Temp T2 = new Temp();
    static final Temp T3 = new Temp();
    static final Temp T4 = new Temp();
    static final Temp T5 = new Temp();
    static final Temp T6 = new Temp();
    static final Temp T7 = new Temp();
    static final Temp S0 = new Temp(); // callee-saved
    static final Temp S1 = new Temp();
    static final Temp S2 = new Temp();
    static final Temp S3 = new Temp();
    static final Temp S4 = new Temp();
    static final Temp S5 = new Temp();
    static final Temp S6 = new Temp();
    static final Temp S7 = new Temp();
    static final Temp T8 = new Temp(); // caller-saved
    static final Temp T9 = new Temp();
    static final Temp K0 = new Temp(); // reserved for OS kernel
    static final Temp K1 = new Temp(); // reserved for OS kernel
    static final Temp GP = new Temp(); // pointer to global area
    static final Temp SP = new Temp(); // stack pointer
    static final Temp S8 = new Temp(); // callee-save (frame pointer)
    static final Temp RA = new Temp(); // return address

    // Register lists: must not overlap and must include every register that
    // might show up in code
    private static final Temp[]
	// registers dedicated to special purposes
	specialRegs = { ZERO, AT, K0, K1, GP, SP },
	// registers to pass outgoing arguments
	argRegs	= { A0, A1, A2, A3 },
        // registers that a callee must preserve for its caller
	calleeSaves = { RA, S0, S1, S2, S3, S4, S5, S6, S7, S8 },
	// registers that a callee may use without preserving
	callerSaves = { T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, V0, V1 };

    static final Temp FP = new Temp(); // virtual frame pointer (eliminated)
    public Temp FP() { return FP; }
    public Temp RV() { return V0; }
    public Temp SP() { return SP; }
    public Temp ZERO() { return ZERO; }
    private static HashMap<String,Label> labels = new HashMap<String,Label>();
    public AbstractExp externalCall(String s, ExpList args) {
	String func = s.intern();
	Label l = labels.get(func);
	if (l == null) {
	    l = new Label("_" + func);
	    labels.put(func, l);
	}

	return new CALL(new NAME(l), new ExpList(new CONST(0),args));
    }

    public String string(Label lab, String string) {
	int length = string.length();
	String lit = "";
	for (int i = 0; i < length; i++) {
	    char c = string.charAt(i);
	    switch(c) {
	    case '\b': lit += "\\b"; break;
	    case '\t': lit += "\\t"; break;
	    case '\n': lit += "\\n"; break;
	    case '\f': lit += "\\f"; break;
	    case '\r': lit += "\\r"; break;
	    case '\"': lit += "\\\""; break;
	    case '\\': lit += "\\\\"; break;
	    default:
		if (c < ' ' || c > '~') {
		    int v = (int)c;
		    lit += "\\" + ((v>>6)&7) + ((v>>3)&7) + (v&7);
		} else
		    lit += c;
		break;
	    }
	}
	return "\t.data\n\t.word " + length + "\n" + lab.toString()
	    + ":\t.asciiz\t\"" + lit + "\"";
    }
    
    private static final Label badPtr = new Label("BADPTR");
    public Label badPtr() {
	return badPtr;
    }

    private static final Label badSub = new Label("BADSUB");
    public Label badSub() {
	return badSub;
    }

    private static final
	HashMap<Temp,String> tempMap = new HashMap<Temp,String>(32);
    static {
        tempMap.put(ZERO, "$0");
	tempMap.put(AT,   "$at");
	tempMap.put(V0,   "$v0");
	tempMap.put(V1,   "$v1");
	tempMap.put(A0,   "$a0");
	tempMap.put(A1,   "$a1");
	tempMap.put(A2,   "$a2");
	tempMap.put(A3,   "$a3");
	tempMap.put(T0,   "$t0");
	tempMap.put(T1,   "$t1");
	tempMap.put(T2,   "$t2");
	tempMap.put(T3,   "$t3");
	tempMap.put(T4,   "$t4");
	tempMap.put(T5,   "$t5");
	tempMap.put(T6,   "$t6");
	tempMap.put(T7,   "$t7");
	tempMap.put(S0,   "$s0");
	tempMap.put(S1,   "$s1");
	tempMap.put(S2,   "$s2");
	tempMap.put(S3,   "$s3");
	tempMap.put(S4,   "$s4");
	tempMap.put(S5,   "$s5");
	tempMap.put(S6,   "$s6");
	tempMap.put(S7,   "$s7");
	tempMap.put(T8,   "$t8");
	tempMap.put(T9,   "$t9");
	tempMap.put(K0,   "$k0");
	tempMap.put(K1,   "$k1");
	tempMap.put(GP,   "$gp");
	tempMap.put(SP,   "$sp");
	tempMap.put(S8,   "$fp");
	tempMap.put(RA,   "$ra");
    }
    public String tempMap(Temp temp) {
	return tempMap.get(temp);
    }

    int maxArgOffset = 0;

    public InstrList codegen(StmList stms) {
		Codegen cg = new Codegen(this);
		
		StmList s = stms;
		
		while (s != null) {
			cg.codegen(s.head);
			s = s.tail;
		}
		
    	return cg.getCode();
    }

    private static <R> void addAll(java.util.Collection<R> c, R[] a) {
	for (int i = 0; i < a.length; i++)
	    c.add(a[i]);
    }

    // Registers live on return
    private static Temp[] returnSink = {};
    {
	LinkedList<Temp> l = new LinkedList<Temp>();
	l.add(V0);
	addAll(l, specialRegs);
	addAll(l, calleeSaves);
	returnSink = l.toArray(returnSink);
    }

    // Registers defined by a call
    static Temp[] calldefs = {};
    {
	LinkedList<Temp> l = new LinkedList<Temp>();
	l.add(RA);
	addAll(l, argRegs);
	addAll(l, callerSaves);
	calldefs = l.toArray(calldefs);
    }

    private static Stm SEQ(Stm left, Stm right) {
	if (left == null)
	    return right;
	if (right == null)
	    return left;
	return new SEQ(left, right);
    }
    private static MOVE MOVE(AbstractExp d, AbstractExp s) {
	return new MOVE(d, s);
    }
    private static TEMP TEMP(Temp t) {
	return new TEMP(t);
    }

    private void
	assignFormals(Iterator<Access> formals,
		      Iterator<Access> actuals,
		      List<Stm> body)
    {
	if (!formals.hasNext() || !actuals.hasNext())
	    return;
	Access formal = formals.next();
	Access actual = actuals.next();
	assignFormals(formals, actuals, body);
	body.add(0, MOVE(formal.exp(TEMP(FP)), actual.exp(TEMP(FP))));
    }

    private void assignCallees(int i, List<Stm> body)
    {
	if (i >= calleeSaves.length)
	    return;
	Access a = allocLocal(!spilling);
	assignCallees(i+1, body);
	body.add(0, MOVE(a.exp(TEMP(FP)), TEMP(calleeSaves[i])));
	body.add(MOVE(TEMP(calleeSaves[i]), a.exp(TEMP(FP))));
    }

    private List<Access> actuals;
    public void procEntryExit1(List<Stm> body) {
	assignFormals(formals.iterator(), actuals.iterator(), body);
	assignCallees(0, body);
    }

    private static Instr OPER(String a, TempList d, TempList s) {
    	return new OPER_ASSEM(a, d, s);
    }

    public void procEntryExit2(List<Instr> body) {
    	TempList t = null;
    	for (int i = returnSink.length-1; i>=0; i--)
    		t = new TempList(returnSink[i],t);
    	
    	body.add(OPER("#\treturn", null, t));
    }

    public void procEntryExit3(List<Instr> body) {
	int frameSize = maxArgOffset - offset;
	ListIterator<Instr> cursor = body.listIterator();
	cursor.add(OPER("\t.text", null, null));
	cursor.add(OPER(name + ":", null, null));
	cursor.add(OPER(name + "_framesize=" + frameSize, null, null));
	if (frameSize != 0) {
	    cursor.add(OPER("\tsubu $sp " + name + "_framesize",
	    		new TempList(SP,null), new TempList(SP,null)));
	    body.add(OPER("\taddu $sp " + name + "_framesize",
	    		new TempList(SP,null), new TempList(SP,null)));
	}
	body.add(OPER("\tj $ra", null, new TempList(RA,null)));
    }

    private static Temp[] registers = {};
    {
	LinkedList<Temp> l = new LinkedList<Temp>();
	addAll(l, callerSaves);
	addAll(l, calleeSaves);
	addAll(l, argRegs);
	addAll(l, specialRegs);
	registers = l.toArray(registers);
    }
    public Temp[] registers() {
	return registers;
    }

        private static boolean spilling = true ;
    // set spilling to true when the spill method is implemented
    public void spill(List<Instr> insns, Temp[] spills) {
	if (spills != null) {
	    if (!spilling) {
		for (int s = 0; s < spills.length; s++)
		    System.err.println("Need to spill " + spills[s]);
		throw new Error("Spilling unimplemented");
	    }
            else for (int s = 0; s < spills.length; s++) {
		AbstractExp exp = allocLocal(true).exp(TEMP(FP));
		for (ListIterator<Instr> i = insns.listIterator();
		     i.hasNext(); ) {
		    Instr insn = i.next();
		    TempList use = insn.use();
		    
			while(use!=null) {
			    if (use.head == spills[s]) {
					Temp t = new Temp();
					t.spillTemp = true;
					Stm stm = MOVE(TEMP(t), exp);
					i.previous();
					//stm.accept(new Codegen(this, i));
					if (insn != i.next())
					    throw new Error();
					insn.replaceUse(spills[s], t);
					break;
			    }
			    
			    use = use.tail;
			}
		    TempList def = insn.def();
		    
			while(def!=null) {
			    if (def.head == spills[s]) {
					Temp t = new Temp();
					t.spillTemp = true;
					insn.replaceDef(spills[s], t);
					Stm stm = MOVE(exp, TEMP(t));
					//stm.accept(new Codegen(this, i));
					break;
			    }
			    
			    def = def.tail;
			}
		}
	    }
        }
    }
}