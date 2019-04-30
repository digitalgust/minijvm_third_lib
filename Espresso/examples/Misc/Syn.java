
class Syn {

	synchronized void f() {
		System.out.println("f()");
	}

	synchronized void g() {
		System.out.println("g()");
		f();
	}

	public static void main(String args[]) {
		Syn s = new Syn();
		s.g();
	}
}
