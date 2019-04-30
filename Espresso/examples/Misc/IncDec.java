
class IncDec {

	static int f;

	static int[] a;

	public static void main(String args[]) {

		int i;
		double d;
		a = new int[10];

		d = 0.0;
		i = a[0] = f = 0;

		System.out.println(i++);
		System.out.println(i--);
		System.out.println(++i);
		System.out.println(--i);

		System.out.println(d++);
		System.out.println(d--);
		System.out.println(++d);
		System.out.println(--d);

		System.out.println(f++);
		System.out.println(f--);
		System.out.println(++f);
		System.out.println(--f);

		System.out.println(a[0]++);
		System.out.println(a[0]--);
		System.out.println(++a[0]);
		System.out.println(--a[0]);

		System.out.println(a[i++]);
		System.out.println(a[i--]);
		System.out.println(a[++i]);
		System.out.println(a[--i]);

		System.out.println(a[f++]);
		System.out.println(a[f--]);
		System.out.println(a[++f]);
		System.out.println(a[--f]);

		System.out.println(a[(int) d++]);
		System.out.println(a[(int) d--]);
		System.out.println(a[(int) ++d]);
		System.out.println(a[(int) --d]);
	}

}

