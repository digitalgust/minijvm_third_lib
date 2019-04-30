import java.io.IOException;

public class Throws2 {

    public void test() {
	throw new RuntimeException();
    }

    public void test2() {
	try {
	    try {
		test();
	    }
	    catch (IOException e) {}
	    try {
		test();
		try {
		    test3();
		} catch (Throwable t) {}
	    }
	    catch (Exception e) {}
	} catch(IOException e) {}
    }

    public void test3() throws IOException {
	test();
	test();
	throw new IOException();
    }

}

