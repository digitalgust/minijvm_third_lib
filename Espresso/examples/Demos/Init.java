class Init {

// Tests field initializers and static initializers

	static int q = 100*3;
	static double d = 15.0;
	boolean a = true;
	static int w;
	static double v;
		
	static {
	   w = 40/2;	
	}

	String z = new String("Hello");
	

	Init() {
	   q = 100;
	}


	Init( int b ) {
	   q = b;
	}

	static {
	   v = 10.0;	
	}

	public static void main (String[] args ) {
		System.out.println(q);
		System.out.println(d);
		System.out.println(w);
		System.out.println(v);
		System.out.println(Init.q);
		System.out.println((new Init()).q);
		System.out.println((new Init(12)).q);	
	}
}