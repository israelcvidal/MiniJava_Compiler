package core.activation_records.temp;

public class Temp {
   private static int count;
   private int num;
   public boolean spillTemp;
   public String toString() {return "t" + num;}
   public Temp() { 
     num=count++;
   }
   public Temp(int t) {
	   num = t ;
   }
}