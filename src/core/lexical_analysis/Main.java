package core.lexical_analysis;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Main {
	
	@SuppressWarnings("static-access")
	public static void main(String [] args) {
	    try {
	    	BufferedReader br = new BufferedReader(new FileReader("data.txt"));
	    	
	        new MiniJavaParser(br).Program();
	        System.out.println("Lexical analysis successfull!");
	        
	    } catch (ParseException e) {
	    	System.out.println("Lexer Error : \n"+ e.toString());
	    } catch (IOException ioe) {
	    	ioe.printStackTrace();
	    }
   }
}