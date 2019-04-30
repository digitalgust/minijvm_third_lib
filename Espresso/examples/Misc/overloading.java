
class Overloading {

	void f() {
		g(5);
		g(5.0);
	}

	int g(int i) {
		return i;
	}

	double g(double d) {
		return d;
	}

}

