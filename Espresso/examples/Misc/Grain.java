
class Grain {

	static int align2grain(int i, int grain) {
		return ((i + grain-1) & ~(grain-1));
	}

	public static void main(String args[]) {
		try {
			int i = Integer.parseInt(args[0]);
			int grain = Integer.parseInt(args[1]);
			System.out.println("aligned = " + align2grain(i, grain));
		}
		catch (Exception e) {
		}
	}

}
		
