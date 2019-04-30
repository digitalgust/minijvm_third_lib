
class Control {

	static long x = 0;

	public static void main(String[] args) {
		for (int i = 1; i < 5; i++) {
			System.out.println(fac(i));
		}

		for (int i = 5; i < 10; i++) {
			System.out.println(fac(i));
		}
	}

	static int fac(int n) {
		if (n <= 0) {
			return 1;
		} 
		else {
			return n * fac(n - 1);
		}
	}

}
