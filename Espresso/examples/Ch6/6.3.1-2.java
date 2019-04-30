class Point {

    int x, y;

}




class Test {

    static Point Point (int x, int y) {
        Point p =  new Point();
        p.x =  x;
        p.y =  y;
        return  p;
    }


    public static void main (String[] args) {
        int Point;
        Point[] pa =  new Point[2];

        for (Point =  0; Point < 2; Point++) {
            pa[Point] =  new Point();
            pa[Point].x =  Point;
            pa[Point].y =  Point;
        }

        System.out.println(pa[0].x + "," + pa[0].y);
        System.out.println(pa[1].x + "," + pa[1].y);
        Point p =  Point(3, 4);
        System.out.println(p.x + "," + p.y);
    }

}

