// Try.java

class Try
{
    public static void main(String args[])
    {
	System.out.println("TestReturnInt(...)");
	int test = TestReturnInt(args);
	System.out.println("got " + test);

	System.out.println("TestReturnVoid()");
	TestReturnVoid();
    }

    static int TestReturnInt(String args[])
    {
	try
	    {
		try
		    {
			System.out.println("throwing exception?");

			if(args.length > 1)
			    throw new Exception("foo");

			System.out.println("didn't throw exception");
		
			return 1;
		    }
		catch(Exception e)
		    {
			System.out.println("caught exception " + e + " (1)");
		    }
		finally
		    {
			System.out.println("ran finally (1)");
		    }

		if(args.length > 0)
		    throw new Exception("bar");

		return 3;
	    }
	catch(Exception e)
	    {
		System.out.println("caught exception " + e + " (2)");
	    }
	finally
	    {
		System.out.println("ran finally (2)");
	    }
	
     	return 0;
    }

    static void TestReturnVoid()
    {
	try
	    {
		throw new Exception("hi");
	    }
	catch(Exception e)
	    {
		System.out.println("caught exception " + e + " (3)");
	    }

	try
	    {
		try
		    {
			return;
		    }
		finally
		    {
			System.out.println("finally (4)");
		    }
	    }
	finally
	    {
		System.out.println("finally (5)");
	    }
    }
}
