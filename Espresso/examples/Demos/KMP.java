
public class KMP {

	char[] p;		// pattern
	char[] t;		// text
	int[] pi;

	int shifts[], next = 0;
	
	int comp1 = 0;		// # of comparisons prefix
	int comp2 = 0;		// # of comparisons match
	
    public KMP(String pp, String tt) {
		p = new char[pp.length() + 1];
		t = new char[tt.length() + 1];
        pp.getChars(0, pp.length(), p, 1);
        tt.getChars(0, tt.length(), t, 1);
	
		shifts = new int[tt.length()];
		for (int i = 0; i < shifts.length; i++)
			shifts[i] = -1;
				
		computePrefixFunction();
		matcher();
    }

	public void computePrefixFunction() {
		int m = p.length - 1;
		pi = new int[m + 1];		// entry 0 unused !
		
		pi[1] = 0;
		int k = 0;
		for (int q = 2; q <= m; q++) {
			comp1++;
			while (k > 0 && p[k + 1] != p[q]) {
				k = pi[k]; comp1++; 	// comp1 += 2;
			}
			comp1++;
			if (p[k + 1] == p[q])
				k++;
			pi[q] = k;
		}			
	}

	public void matcher() {
		int n = t.length - 1;
		int m = p.length - 1;

		int q = 0;
		for (int i = 1; i <= n; i++) {
			comp2++;
			while (q > 0 && p[q + 1] != t[i]) {
				q = pi[q]; comp2++;
			}
			
			comp2++;
			if (p[q + 1] == t[i]) 
				q++;
				
			comp2++;
			if (q == m) {
				shifts[next++] = i - m;
				q = pi[q];
			}				
		}
	}
	
	public void print() {
		System.out.print("pi = [ ");
		int len = pi.length;
		for (int i = 1; i < len; i++) {
			System.out.print(pi[i]);
			System.out.print(" ");
		}
		System.out.print("] => comparisons = ");
		System.out.println(comp1);
	
		System.out.print("shifts = [ ");
		for (int i = 0; i < shifts.length && shifts[i] != -1; i++) {
			System.out.print(shifts[i]);
			System.out.print(" ");
		}
		System.out.print("] => comparisons = ");
		System.out.println(comp2);
	}

    public static void main (String args[]) {
        KMP app = new KMP(args[0], args[1]);
		app.print();
	}
}
