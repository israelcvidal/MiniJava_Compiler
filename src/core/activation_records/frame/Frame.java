package core.activation_records.frame;

import java.util.List;

import core.activation_records.temp.Label;
import core.activation_records.temp.Temp;
import core.activation_records.temp.TempMap;
import core.instruction_selection.assem.Instr;
import core.translation_to_IR.tree.AbstractExp;
import core.translation_to_IR.tree.Stm;
import devel.semantic_analysis.Symbol;

public abstract class Frame implements TempMap {
    public Label name;
    public List<Access> formals;
    public abstract Frame newFrame(Symbol name, List<Boolean> formals);
    public abstract Access allocLocal(boolean escape);
    public abstract Temp FP();
    public abstract int wordSize();
    public abstract AbstractExp externalCall(String func, List<AbstractExp> args);
    public abstract Temp RV();
    public abstract String string(Label label, String value);
    public abstract Label badPtr();
    public abstract Label badSub();
    public abstract String tempMap(Temp temp);
    public abstract List<Instr> codegen(List<Stm> stms);
    public abstract void procEntryExit1(List<Stm> body);
    public abstract void procEntryExit2(List<Instr> body);
    public abstract void procEntryExit3(List<Instr> body);
    public abstract Temp[] registers();
    public abstract void spill(List<Instr> insns, Temp[] spills);
    public abstract String programTail(); //append to end of target code
}