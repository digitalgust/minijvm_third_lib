
class A extends B {

  A() { x = x + 1; f(); }

  boolean x;
  int y = x;

  void f() { z = 4; B a = (B) new B(); }

}

class B {
	int z;
}
