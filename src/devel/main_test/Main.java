package devel.main_test;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import core.abstract_syntax.syntaxtree.*;
import core.activation_records.mips.MipsFrame;
import core.activation_records.temp.Temp;
import core.canonical_trees.BasicBlocks;
import core.canonical_trees.Canon;
import core.canonical_trees.TraceSchedule;
import core.dataflow_analysis.graph.NodeList;
import core.instruction_selection.assem.Instr;
import core.instruction_selection.assem.InstrList;
import core.lexical_analysis.MiniJavaParser;
import core.lexical_analysis.ParseException;
import devel.IR_translation.Frag;
import devel.IR_translation.ProcFrag;
import devel.IR_translation.TranslateVisitor;
import devel.assemble_flow_graph.AssemFlowGraph;
import devel.liveness_analysis.Liveness;
import devel.reg_alloc.RegAlloc;
import devel.semantic_analysis.BuildTableVisitor;
import devel.semantic_analysis.CheckTableVisitor;
import devel.semantic_analysis.ProgramTable;
import core.translation_to_IR.tree.Print;
import core.translation_to_IR.tree.StmList;;

/**
 * The class implements all of compiler's phases for testing.
 * @author daniel
 *
 */

public class Main {
	public final static String TEST_FOLDER = "samples/";
	public final static String TEST_BINARY_SEARCH = TEST_FOLDER + "BinarySearch.java";
	public final static String TEST_BINARY_TREE = TEST_FOLDER + "BinaryTree.java";
	public final static String TEST_BUBBLE_SORT = TEST_FOLDER + "BubbleSort.java";
	public final static String TEST_FACTORIAL = TEST_FOLDER + "Factorial.java";
	public final static String TEST_LINEAR_SEARCH = TEST_FOLDER + "LinearSearch.java";
	public final static String TEST_LINKED_LIST = TEST_FOLDER + "LinkedList.java";
	public final static String TEST_QUICK_SORT = TEST_FOLDER + "QuickSort.java";
	public final static String TEST_TREE_VISITOR = TEST_FOLDER + "TreeVisitor.java";
	public final static String TEST_TRIVIAL = TEST_FOLDER + "Trivial.java";

//													0					1				2					3
	public final static String[] caseTests = {TEST_BINARY_SEARCH, TEST_BINARY_TREE, TEST_BUBBLE_SORT, TEST_FACTORIAL,
											  TEST_LINEAR_SEARCH, TEST_LINKED_LIST, TEST_QUICK_SORT, TEST_TREE_VISITOR,
//													4					5				6					7
											  TEST_TRIVIAL};
//													8
	public static void main(String [] args) {
	   try {
		   //Read a file with the program
		   BufferedReader br = new BufferedReader(new FileReader(caseTests[7]));
		   
		   // Analysis lexical and parsing
		   @SuppressWarnings("static-access")
		   Program root = new MiniJavaParser(br).Program();
		   
		   // Analysis semantic's first pass
		   ProgramTable pt = root.accept(new BuildTableVisitor());
		   
		   // Analysis semantic's second pass
		   root.accept(new CheckTableVisitor(pt));
		   
		   // Translate to IR tree
		   List<Frag> frags = root.accept(new TranslateVisitor(new MipsFrame(), pt));
		   
		   //Print IR tree
//		   for (Frag f : frags)
//			   if (f instanceof ProcFrag) {
//				   ProcFrag pf = (ProcFrag) f;
//				   
//				   System.out.println("METHOD'S NAME: " + pf.getFrame().name);
//				   
//				   if (pf.getBody() != null)
//					   new Print(System.out).prStm(pf.getBody());
//				   
//			   }
		   
		   // Compute canonical IR tree, Basic Blocks and Trace for each Frag.
		   List<StmList> traces = new LinkedList<>();
		   
		   for (int i = 0; i < frags.size(); i++) {
			   Frag f = frags.get(i);
			   
			   if (f instanceof ProcFrag) {
				   ProcFrag pf = (ProcFrag) f;
				   BasicBlocks bb = new BasicBlocks(Canon.linearize((pf).getBody()));
				   TraceSchedule ts = new TraceSchedule(bb);
				   traces.add(ts.stms);
			   }
			   
		   }
		   
		   // Tiling IR trees for each Frag and generate a instructions program's list.
		   InstrList allInstr = new InstrList(null, null);
		   InstrList allInstrIt = allInstr, allInstrIt_ = allInstrIt;
		   
		   for (int i = 0; i < frags.size(); i++) {
			   Frag f = frags.get(i);
			   
			   List<Instr> bodyList = new LinkedList<>();
			   
			   if (f instanceof ProcFrag) {
				   ProcFrag pf = (ProcFrag) f;
				   StmList stms = traces.get(i);
				   InstrList body = pf.getFrame().codegen(stms);
				   
				   InstrList insIt = body;
				   
				   while (insIt != null) {
					   bodyList.add(insIt.head);
					   insIt = insIt.tail;
				   }
				   
				   pf.getFrame().procEntryExit2(bodyList);
				   pf.getFrame().procEntryExit3(bodyList);
				   
				   allInstrIt.head = bodyList.get(0);
				   allInstrIt_ = allInstrIt;
				   allInstrIt = allInstrIt.tail = new InstrList(null, null);
				   
				   for (int j = 1; j < bodyList.size(); j++) {
					   allInstrIt.head = bodyList.get(j);
					   allInstrIt_ = allInstrIt;
					   allInstrIt = allInstrIt.tail = new InstrList(null, null);
				   }
				   
			   }
			   
			   // Print each instructions' frag
//			   for (Instr ins : bodyList) {
//				   System.out.println(ins.assem);
//			   }
		   }
		   
		   allInstrIt_.tail = null;
		   
		   
		   //Print all of instructions' program
//		   InstrList printInst = allInstr;
//		   
//		   while (printInst != null) {
//			   System.out.println(printInst.head.assem);
//			   printInst = printInst.tail;
//		   }
		   
		   // Generate FlowGraph and InterferenceGraph
		   AssemFlowGraph afg = new AssemFlowGraph(allInstr);
		   
//		   Printing flowGraph
//		   for(NodeList nl = afg.nodes(); nl!=null; nl=nl.tail){
////			   System.out.println(nl.head.getInstruction().assem);
//			   for (NodeList p = nl.head.succ(); p!=null; p = p.tail) {
//				   System.out.println(nl.head.getInstruction().assem + " -> " + p.head.getInstruction().assem);
//				   System.out.println("--------------");
//			   }
//		   }
		  
		   Liveness ig = new Liveness(afg);
		   
//		   printing interferences
//		   for(String node: ig.getNodes()){
//			   System.out.println(node + " - "+ ig.getInterferences(node));
//		   }
		   
		   // Allocate registers to temps
		   RegAlloc.instList = allInstr;
		   HashMap<String, Integer> colors = RegAlloc.Alloc(ig);
		   
	   	} catch (ParseException e) {
	   		System.out.println(e.toString());
	   		
	    } catch (IOException ioe) {
	    	ioe.printStackTrace();
	    }
   }
}