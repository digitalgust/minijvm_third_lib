class BadThrow
{
    public static void main(String args[])
    {
	// RuntimeException's do not need to be declared in throws
	throw new String("foo");
	throw 3;
    }
}
