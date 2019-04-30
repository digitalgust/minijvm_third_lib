
class Assign {

	public static void main(String args[]) {
		int i = 0;
		boolean b, c;
		c = b = i < 0;

		Assign a = new Assign();
		a.f();
		a.g();
		a.h();
	}

	void f() {
		int k, j, i = 0;
		k = j = i += 2.5;

		System.out.println(i);
		System.out.println(j);
		System.out.println(k);
	}

	int x, y;

	void g() {
		x = y = 1;
		y = (x *= 2);

		System.out.println(x);
		System.out.println(y);
	}

	int z[] = new int[10];

	void h() {
		z[0] = 0;
		System.out.println(z[0]);
		z[0] += 5;
		System.out.println(z[0]);
	}
}
