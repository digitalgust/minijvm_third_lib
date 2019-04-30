// BadTry.java

class BadTry
{
    public static void main(String args[])
    {
        try
            {
                throw new Exception("bar");
            }

	try
	    {
		System.out.println("throwing exception");
		throw new Exception("foo");
	    }
	catch(Exception e)
	    {
		System.out.println("caught exception " + e);
	    }
        catch(String s)
            {
                System.out.println("caught string???");
            }
	finally
	    {
		System.out.println("ran finally");
	    }
    }
}
