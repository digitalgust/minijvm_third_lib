
class Proof {

	final int LIMIT = 100;
	final int LIMIT2 = 100;

	static public void main(String args[]) {
		new Proof().run();
	}

	void run() {
		for (int n = 0; n < LIMIT; n += 2) {
			for (int k = 0; k < LIMIT2; k++) {
				if (n < 2 * k + 2) {
					if (n >= k + 3) {
						System.out.println("n = " + n + " k = " + k);
					}	
				}
			}
		}
	}

}
				
