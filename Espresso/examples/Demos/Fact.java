class Fact {
//
//	computes the factorial
//
	static int fact ( int a ) {

		if ( a <= 0 ) {
		   return 1;
			}
	else {  
		   return a*fact(a-1);
                }
	}

	public static void main (String[] args ) {
		if ( args.length == 1 ) {
		   int x = new Integer(args[0]).intValue();
		   System.out.println(fact(x));
		}
	}

}
