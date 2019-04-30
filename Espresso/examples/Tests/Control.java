
class Control {

	static void f (int i) {
		if (false && !true) {
			System.out.println(i);
		}

		while (true && false) {
			System.out.println(i);
		}

		do {
			System.out.println(i--);
		} while (true && i > 0);

		for (int i = 0; 10 >= i; i++) {
			System.out.println(i);
		}
	}

	public static void main(String args[]) {
		f(2);
	}

}
