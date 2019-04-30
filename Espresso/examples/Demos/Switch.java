class Switch {

//
// Does some operations on a given input value
// using the switch statement
//

	public int f(int x) {

		switch (x) {
		case 1:
		case 3:
		case 4:
		case 6:
		case 7:
		case 5:
			x = 2*x;
			break;
		case 2:
			x = 3*x;
			break;
		default:
			x = 4*x;
		}
		return x;
	}

	public int g(int x) {
                return f(x);
	}

	public static void main (String[] args) {
	   if ( args.length == 1 ) {
		int x = new Integer(args[0]).intValue();
		System.out.println(new Switch().g(x));
           }
	}
}
