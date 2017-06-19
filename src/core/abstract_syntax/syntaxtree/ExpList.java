package core.abstract_syntax.syntaxtree;

import java.util.Vector;

public class ExpList {
   public Vector<Exp> list;

   public ExpList() {
      list = new Vector<>();
   }

   public void addElement(Exp n) {
      list.addElement(n);
   }

   public Exp elementAt(int i)  { 
      return list.elementAt(i); 
   }

   public int size() { 
      return list.size(); 
   }
}
