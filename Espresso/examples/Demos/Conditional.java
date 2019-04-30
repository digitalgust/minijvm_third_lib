class Conditional {

// Tests the Conditional
// Given 2 integer a, b
// if a == b, writes out a*b*-1
// else write out a*b
// If the result is negative, then it makes if positive
// writes aout the result

	public static void main (String[] args ) {
	   if ( args.length == 2 ) {
	      int a = new Integer(args[0]).intValue();
	      int b = new Integer(args[1]).intValue();
	      int c;
	      c = (a == b) ? a*b*-1 : a*b;
	      System.out.println(c);
	      System.out.println( (c < 0) ? c*-1 : c);
	   }
	}	

}