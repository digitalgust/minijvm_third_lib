class A {

    protected int x;


    int f (short a, short b) {
        return  0;
    }

}




class B
    extends A
{

    int f (short a, int b) {
        return  0;
    }


    int f (int a, short b) {
        return  0;
    }


    int f (short a, short b) {
        return  0;
    }

}




class C
    extends B
{

    /*
	int f(int a, short b) {
		return 0;
	}
*/
    int g () {
        short x =  0, y =  0;
        return  f(x, y); // which f is chosen ?
    }

}

