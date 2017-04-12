package core.abstract_syntax.syntaxtree;

import java.util.Vector;

public class StatementList {
   private Vector<Statement> list;

   public StatementList() {
      list = new Vector<>();
   }

   public void addElement(Statement n) {
      list.addElement(n);
   }

   public Statement elementAt(int i)  { 
      return list.elementAt(i); 
   }

   public int size() { 
      return list.size(); 
   }
}
