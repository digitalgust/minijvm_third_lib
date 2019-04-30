
class Exp1 {
	int x = 0;
}

class Exp3 {
	Exp3() { }
	int x = 0;
}

class Exp2 extends Exp1 {

	public Exp2() {
	}

	Exp2 self() {
		// throw x;

		synchronized (null) {

		}
		return this;
	}

	void f() {
		self().x = 0;

		double a = 2.2 % 2;
		System.out.println(a);
	}

	void g(int x) {
		int n = 0;
		n += (short) 4;
		n /= 1.0;
		short s = 1;
		s *= 1.2;
	}

	public static void main(String[] args) {
		Exp2 t = new Exp2();
		t.g(1);
		if (null == null) return;

		boolean b = false;
		if (b == true) { b = false; } else { b = true; }

		double d = 4.5;
		if (d == 0) { b = false; } else { b = true; }

		for (int i = 0; i <= 0; i++) {
			i = i + 100;
		}

		Exp2 e = (Exp2) new Exp1();
		if (e instanceof Exp1) {
		}

		byte y = (byte) d;

		long l = 5 & 10 ^ 7 | 9;
		boolean bb = false ^ true;

		long ll = 5L << y;
	}
}
