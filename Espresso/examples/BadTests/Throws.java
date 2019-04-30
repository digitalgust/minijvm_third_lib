import java.io.IOException;

public class Throws {

    public void test() {
	//throw new RuntimeException();
    }

    public void test2() {
	try {
	    try {
		test();
	    }
	    catch (String e) {}
	    try {
		test();
		try {
		    test3();
		} catch (Throwable t) {}
	    }
	    catch (Exception e) {}
	} catch(Exception e) {}
    }

    public void test3() throws IOException {
	test();
	test();
	throw new IOException();
    }

}

