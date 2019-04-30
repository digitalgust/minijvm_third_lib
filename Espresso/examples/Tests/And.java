
class And {

	static void f (int i) {
		if (i > 0 && i > 1) {
			System.out.println(i);
		}
		if (i <= 0 && i > 1 && i == 0) {
			System.out.println(i);
		}
	}

	public static void main(String args[]) {
		f(2);
	}

}
