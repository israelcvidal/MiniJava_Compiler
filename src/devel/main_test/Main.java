package devel.main_test;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

import core.abstract_syntax.syntaxtree.*;
import core.activation_records.mips.MipsFrame;
import core.canonical_trees.BasicBlocks;
import core.canonical_trees.Canon;
import core.canonical_trees.TraceSchedule;
import core.lexical_analysis.MiniJavaParser;
import core.lexical_analysis.ParseException;
import devel.IR_translation.Frag;
import devel.IR_translation.ProcFrag;
import devel.IR_translation.TranslateVisitor;
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
//													0					1				2					3
	public final static String[] caseTests = {TEST_BINARY_SEARCH, TEST_BINARY_TREE, TEST_BUBBLE_SORT, TEST_FACTORIAL,
											  TEST_LINEAR_SEARCH, TEST_LINKED_LIST, TEST_QUICK_SORT, TEST_TREE_VISITOR};
//													4					5				6					7
	public static void main(String [] args) {
	   try {
		   //Read a file with the program
		   BufferedReader br = new BufferedReader(new FileReader(caseTests[3]));
		   
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
		   
		   // Generate canonical IR tree and Basic Blocks
		   BasicBlocks bb = new BasicBlocks(Canon.linearize(((ProcFrag) frags.get(0)).getBody()));
		   
		   for (int i = 1; i < frags.size(); i++) {
			   Frag f = frags.get(i);
			   
			   if (f instanceof ProcFrag) {
				   ProcFrag pf = (ProcFrag) f;
				   StmList sl = Canon.linearize(pf.getBody());
				   
				   bb.mkBlocks(sl);
				   
				   //Print canonical IR trees without frag[0].
//				   StmList s = sl;
//				   
//				   while (s != null) {
//					   new Print(System.out).prStm(s.head);
//					   s = s.tail;
//				   }
				   
			   }
		   }
		   
		   //Generate Trace
		   TraceSchedule ts = new TraceSchedule(bb);
		   
		   StmList s = ts.stms; //trace
		   
		   //Print Trace
		   while (s != null) {
			   new Print(System.out).prStm(s.head);
			   s = s.tail;
		   }
		   
		   
	   	} catch (ParseException e) {
	   		System.out.println(e.toString());
	   		
	    } catch (IOException ioe) {
	    	ioe.printStackTrace();
	    }
   }
}