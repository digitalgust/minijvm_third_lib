
abstract class Figure {

	int x = 0;
	int y = 0;

	public Figure(int xx, int yy) {
		x = xx;
		y = yy;
	}

	public abstract void print();

}
	
class Rectangle extends Figure {

	int width = 0;
	int length = 0;

	public Rectangle(int xx, int yy, int ww, int ll) {
		super(xx, yy);
		width = ww; length = ll;
	}

	public void print() {
		System.out.println("A rectangle");
	}

}

class Square extends Rectangle {

	public Square(int xx, int yy, int ww) {
		super(xx, yy, ww, ww);
	}

	public void print() {
		System.out.println("A square");
	}

}

class Circle extends Figure {

	int radius = 0;

	public Circle(int xx, int yy, int rr) {
		super(xx, yy);
		radius = rr;
	}

	public void print() {
		System.out.println("A circle");
	}

}

public class Main {

	public static void main(String[] args) {
		Figure[] figs = new Figure[3];

		figs[0] = new Circle(1, 1, 4);
		figs[1] = new Rectangle(1, 1, 2, 5);
		figs[2] = new Square(1, 1, 3);

		for (int i = 0; i < 3; i++) {
			figs[i].print();
		}
	}

}

