class Do {

// do Test adds a first integer to itself
// while the result is less than a second value
 
	static void foo ( int a, int b) {
		int x = 0;
		do {
		   x = x+a;
		} while ( x <= b );
		System.out.println(x);
	}

	public static void main (String[] args ) {
	    if ( args.length == 2 ) {
		foo(new Integer(args[0]).intValue(),
		    new Integer(args[1]).intValue());
	    }
	}

}

