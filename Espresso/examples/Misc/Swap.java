
class Swap {

	static void test() {
		int a = 1;
		a = (a=2) + a;
	}

	static void swap(int a, int b) {
		b += a += b += a += b;
	}

	public static void main(String args[]) {
		try {
			int a = Integer.parseInt(args[0]);
			int b = Integer.parseInt(args[1]);

		b ^= a ^= b;
		a ^= b;

			System.out.println("a = " + a + " b = " + b);
		}
		catch (Exception e) {
		}
	}

}
		
