class Super {

	private void f() { 
		System.out.println("called via invokespecial");
	}

	public void g() { 
		System.out.println("called via invokevirtual");
	}

	void print() {
		System.out.println("Super.print()");
		f(); g();
	}

}

class This extends Super {

	void print() {
		System.out.println("This.print()");
		super.print();
	}

	public static void main(String[] args) {
		new This().print();
	}

}
