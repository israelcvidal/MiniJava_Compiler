package core.lexical_analysis;

import java.io.BufferedReader;
import java.io.FileReader;

public class Main {
   
   @SuppressWarnings("static-access")
public static void main(String [] args) throws Exception {
      try {
    	 BufferedReader br = new BufferedReader(new FileReader("data.txt"));
         new MiniJavaParser(br).Goal();
         System.out.println("Lexical analysis successfull");
      }
      catch (ParseException e) {
         System.out.println("Lexer Error : \n"+ e.toString());
      }
   }
}
