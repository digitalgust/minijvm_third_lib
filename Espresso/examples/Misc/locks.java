
class A {

	public static void main(String args[]) {
		A a = new A();
		a.g();		// blocks ?
	}

	public synchronized int f() {
		System.out.println("A.f()");
		return 0;
	}

	public synchronized int g() {
		System.out.println("A.g()");
		return new B().f(this);
	}

}

class B {

	public int f(A a) {
		System.out.println("B.f()");
		return a.f();
	}

}
