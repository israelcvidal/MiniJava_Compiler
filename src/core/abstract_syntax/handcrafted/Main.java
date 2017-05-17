package core.abstract_syntax.handcrafted;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import core.abstract_syntax.syntaxtree.*;
import core.lexical_analysis.MiniJavaParser;
import core.lexical_analysis.ParseException;
import devel.semantic_analysis.BuildTableVisitor;
import devel.semantic_analysis.CheckTableVisitor;
import devel.semantic_analysis.ProgramTable;

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
	
   public static void main(String [] args) {
	   try {
		   
		   BufferedReader br = new BufferedReader(new FileReader(TEST_TREE_VISITOR));
		   
		   @SuppressWarnings("static-access")
		   Program root = new MiniJavaParser(br).Program();
		   
//		   root.accept(new PrettyPrintVisitor());
		   
		   ProgramTable pt = root.accept(new BuildTableVisitor());
		   
		   root.accept(new CheckTableVisitor(pt));
		   
		   //pt.print();
		   
	   	} catch (ParseException e) {
	   		System.out.println(e.toString());
	   		
	    } catch (IOException ioe) {
	    	ioe.printStackTrace();
	    }
   }
}