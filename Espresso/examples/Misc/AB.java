

class B extends A {

	int f(int a, short b) {
		x = 0;
		return x;
	}
		
}

class A {

	protected int x;

	int f(int a, int b) {
		return 0;
	}
	
}

class C extends B {

	int g() {
		A a = false ? new B() : new C();
		x = 1;
		return x;
	}

}
