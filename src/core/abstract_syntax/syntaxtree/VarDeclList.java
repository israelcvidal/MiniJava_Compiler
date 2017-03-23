package core.abstract_syntax.syntaxtree;

import java.util.Vector;

public class VarDeclList {
   private Vector<VarDecl> list;

   public VarDeclList() {
      list = new Vector<>();
   }

   public void addElement(VarDecl n) {
      list.addElement(n);
   }

   public VarDecl elementAt(int i)  { 
      return list.elementAt(i); 
   }

   public int size() { 
      return list.size(); 
   }
}
