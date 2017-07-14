package core.activation_records.temp;

public class TempList {
   public Temp head;
   public TempList tail;
   public TempList(Temp h, TempList t) {head=h; tail=t;}
   
   public boolean contains(Temp t){
	   return contains(t.toString());
   }
   
   public boolean contains(String t){
	   if(head.toString()==t)
		   return true;
	   
	   TempList l = tail;
	   
	   while(l!=null){
		   if(l.head.toString() == t)
			   return true;
		   l = l.tail;
	   }
	   
	   return false;
   }

   public void replace(Temp oldTemp, Temp newTemp){
	   if(head.toString().equals(oldTemp.toString())){
		   head = newTemp;
		   return;
	   }
	   
	   TempList t = tail;
	   while(t!=null){
		   if(t.head.toString().equals(oldTemp.toString())){
			   t.head = newTemp;
			   return;
		   }   
		   t = t.tail;
	   }
   }
}

