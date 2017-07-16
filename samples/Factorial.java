class Factorial{
    public static void main(String[] a){
	System.out.println(new Fac().ComputeFac(10));
    }
}

class Fac {

    public int ComputeFac(int num){
	int num_aux ;
	if (num < 1)
	    num_aux = 50 ;
	else 
//	    num_aux = num * (this.ComputeFac(num-1)) ;
		num_aux = 200;
	return num_aux ;
    }

}

