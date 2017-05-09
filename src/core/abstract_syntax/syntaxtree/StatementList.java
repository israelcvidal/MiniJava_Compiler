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
   
   public void addElementOnStart(Statement n){
	   list.add(0, n);
   }
   
   public void joinAtEnd(StatementList ns){
	   for(int i=0; i<list.size();i++)
		   list.add(ns.elementAt(i));
   }
}
