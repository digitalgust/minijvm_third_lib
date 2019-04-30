
class Matrix {

	public static void main(String args[]) {
		int[][] m1 = new int[3][3];
		int[][] m2 = new int[3][3];

		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				m1[i][j] = (int) (Math.random() * 100);
			}
		}

		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				System.out.print(m1[i][j]);
				System.out.print(" ");
			}
			System.out.println("");
		}

		System.out.println("");

		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				m2[i][j] = m1[j][i];
			}
		}

		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				System.out.print(m2[i][j]);
				System.out.print(" ");
			}
			System.out.println("");
		}

		System.out.println("");
	}

}
