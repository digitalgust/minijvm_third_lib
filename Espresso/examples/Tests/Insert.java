class Insert {

	public void insert(int[] a) {
		int x = 0;
		int j = 0;
		for (int i =1; i < a.length; i++) {
		  x = a[i];
		  j = i-1;
		  while (j >= 0 && x < a[j]) {
			a[j+1] = a[j];
			j--;
		  }
		  a[j+1] = x;
		} 
	}

	public static void main (String[] args) {
		int[] in = new int[10];
		in[0] = 3; in[1] = 9; in[2] = 1; in[3] = 5; in[4] = 7;
		in[5] = 9; in[6] = 2; in[7] = 4; in[8] = 5; in[9] = 0;

		new Insert().insert(in);
		for (int i =0; i < in.length; i++) {
		   System.out.print(in[i]);
		   System.out.print(" ");
		}
		System.out.println("");
	}
}
