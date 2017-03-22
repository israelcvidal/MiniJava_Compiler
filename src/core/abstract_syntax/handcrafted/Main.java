package core.abstract_syntax.handcrafted;

import core.abstract_syntax.syntaxtree.*;
import core.abstract_syntax.visitor.*;

public class Main {
   public static void main(String [] args) {
      try {
         Program root = new MiniJavaParser(System.in).Goal();
          root.accept(new PrettyPrintVisitor());
      }
      catch (ParseException e) {
         System.out.println(e.toString());
      }
   }
}

