
class A {

}

class B extends A {

	static {
		B[][] b = new B[2][2];
		A[][] a = b;
		a[0][0] = null;
	}

}
