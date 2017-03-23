package core.abstract_syntax.syntaxtree;

import java.util.Vector;

public class ClassDeclList {
   private Vector<ClassDecl> list;

   public ClassDeclList() {
      list = new Vector<>();
   }

   public void addElement(ClassDecl n) {
      list.addElement(n);
   }

   public ClassDecl elementAt(int i)  { 
      return list.elementAt(i); 
   }

   public int size() { 
      return list.size(); 
   }
}