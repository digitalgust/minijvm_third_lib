
class Bool {

	static boolean st = false;

	static boolean a[] = new boolean[10];

	static int ia[] = new int[10];

	static boolean f (int i) {
		return true == true && false || true;
	}

	public static void main(String args[]) {
		int i;
		for (i = 0; i < 10; i++) {
			a[i] = true;
		}

		if (a[5]) System.out.println("ok");
		if (f(2)) System.out.println("ok");

		boolean b;
		b = a[0];
		System.out.println(b);

		if (b) System.out.println("ok");
		if (st) System.out.println("ok");

		boolean c;
		c = b = i > 0;
		System.out.println(c);
		System.out.println(b);
	}

}
