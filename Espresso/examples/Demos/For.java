class For {
//
// Test for statement and continue, by adding up
// all odd number, less than or equal to a given value 
//
	static void foo ( int a) {
	int k = 0;
	for ( int i =1; i <= a; i++) {
	     if ( i % 2 == 0 ) {
		continue;
             }
	     k = k + i;
	}
	System.out.println(k);
}

	public static void main (String[] args ) {
	   if ( args.length == 1 ) {
	      int x = new Integer(args[0]).intValue();
	      foo(x);
           }
	}

}