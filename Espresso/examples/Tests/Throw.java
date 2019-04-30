class Throw
{
    Throw()
    {
	cantBeZero(0);
    }

    public static void main(String args[])
    {
	new Throw();
	System.out.println("foo");
    }

    static void cantBeZero(int i)
    {
	if(i == 0)
	    {
		// RuntimeException's do not need to be declared in throws
		throw new RuntimeException("foo");
	    }
    }
}
