class Insert {

//
//	Does insertion sort
//
	public void insert(int[] a ) {
		int x = 0;
		int j = 0;
		for (int i =1; i < a.length; i++ ) {
		  x = a[i];
		  j = i-1;
		  while ( j >= 0 && x < a[j] ) {
			a[j+1] = a[j];
			j--;
		  }
		  a[j+1] = x;
		} 
	}

	public static void main (String[] args ) {
	    if ( args.length != 0 ) {
		int [] in = new int[args.length];
		for (int i =0; i < args.length; i++ ) {
			in[i] = new Integer(args[i]).intValue();
		}
		new Insert().insert(in);
		for (int i =0; i < in.length; i++ ) {
		   System.out.print(in[i]);
		   System.out.print(" ");
		}
		System.out.println("");
	    } 
	}
}
