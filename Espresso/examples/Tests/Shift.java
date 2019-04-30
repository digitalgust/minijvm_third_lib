
class Shift {

	static void f (int i) {
		System.out.println(i << 1);
		System.out.println(i << 2);
		System.out.println(i >> 1);
		System.out.println(i >>> 1);

		long l = i;
		System.out.println(l << 1);
		System.out.println(l << 2);
		System.out.println(l >> 1);
		System.out.println(l >>> 1);

		System.out.println(l);
		l <<= 2;
		System.out.println(l);
		l >>= 2;
		System.out.println(l);
		l >>>= 2;
		System.out.println(l);
	}

	public static void main(String args[]) {
		f(2);
	}

}
