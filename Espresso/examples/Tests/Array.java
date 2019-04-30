class A {}
class B extends A {}

class Array {

	static void f(Cloneable o) { }
	static void f(Object o) { }

	static void f(A[] o) { 
		System.out.println("f(A[])");
	}

	public static void main(String[] args) {
		char[] ret = new char[5];

		System.out.println(ret.length);

		B[] a = new B[2];
		f(a);

		System.arraycopy(ret, 0, ret, 0, 5);
	}

}
