
class Instance {

	public static void main(String args[]) {
		Instance i = new Instance();
		Instance[] j = new Instance[10];

		if (i instanceof Object) {
			System.out.println("YEAH !!");
		}
		if (j instanceof Instance[]) {
			System.out.println("YEAH !!");
		}
	}

}
