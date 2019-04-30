
class Other {
	final static String B = "B_value";
	final static String s = null;
}

class Const extends Other {

	static final int a = 0, b = 1;

	static final String s = new String("a");

	static int f() {
		return b;
	}

	static int g(int i) {
		int result = Integer.MIN_VALUE;
		return result;
	}

	static String h() {
		return s;
	}

	public static void main(String args[]) {
		System.out.println(f());
		System.out.println(g(1));
		System.out.println(h());
		System.out.println(B);
		System.out.println(Other.s);
	}

}
