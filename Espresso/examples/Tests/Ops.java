
class Ops {

	public static boolean g() {
		boolean b = true;
		return (b ^ false); 
	}

	public static int f() {
		int j = 0xFF;
		return (j & 0x0F);
	}

	public static void main(String args[]) {
		int i = 0xFF;
		System.out.println(i & 0x0F);

		long l = 0xFFFFFFFFFFFL;
		System.out.println(l & 0x11);

		boolean b = true;
		b = b & true;
		System.out.println(b);

		i = 1;
		if (b & true && i > 0) {
			System.out.println(i);
		}

		b = b ^ false;
		b ^= false;

		b = b | false;
		b |= false;

		b = b & true;
		b &= true;

		System.out.println(f());

		System.out.println(g());
		b = true;
		boolean c = (b == true);
		System.out.println(c);

		c = !c;
		System.out.println(c);
	}

}
