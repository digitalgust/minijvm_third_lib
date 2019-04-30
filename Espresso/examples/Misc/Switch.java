
class Switch {

	short s;

	int f() {

		int x = 0;

		x = 1;

		switch (x) {
		case 0:
			x = 2;
		case 2:
			x = 3;
			;
		default:
		}

/*		char c = 'c';
		boolean b = true;

		x = c + b; */

		return 0;
	}

	int g() {
		int x = 1;
	}

}

interface I {

	final int a = 1;

}

class B {

	int f() {
		int x = 1;
		return 0;
	}

}
