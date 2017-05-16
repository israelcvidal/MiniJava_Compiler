package core.activation_records.frame;

import java.util.Vector;

public class AccessList {
   private Vector<Access> list;

   public AccessList() {
      list = new Vector<>();
   }

   public void addElement(Access n) {
      list.addElement(n);
   }

   public Access elementAt(int i)  { 
      return list.elementAt(i); 
   }

   public int size() { 
      return list.size(); 
   }
}
