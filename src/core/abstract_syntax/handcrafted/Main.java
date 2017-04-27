package core.abstract_syntax.handcrafted;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import core.abstract_syntax.syntaxtree.*;
import core.abstract_syntax.visitor.*;
import core.lexical_analysis.MiniJavaParser;
import core.lexical_analysis.ParseException;

public class Main {
   public static void main(String [] args) {
	   try {
		   
		   BufferedReader br = new BufferedReader(new FileReader("data.txt"));
		   
		   @SuppressWarnings("static-access")
		   Program root = new MiniJavaParser(br).Program();
		   
		   root.accept(new PrettyPrintVisitor());
		   
	   	} catch (ParseException e) {
	   		System.out.println(e.toString());
	   		
	    } catch (IOException ioe) {
	    	ioe.printStackTrace();
	    }
   }
}