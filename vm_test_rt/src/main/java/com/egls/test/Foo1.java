package com.egls.test;

class Foo1 {
//

    public void t0() {
        System.out.println("HelloWorld.");
    }

    public void t1() {

        byte f0 = 100;
        byte f1 = (byte) (5 % f0 - 2 * (9 + 5) / 2);
        System.out.println("+ = " + (f0 + f1));
        System.out.println("- = " + (f0 - f1));
        System.out.println("* = " + (f0 * f1));
        System.out.println("/ = " + (f0 / f1));
        System.out.println("% = " + (f0 % f1));
        byte[] val = new byte[10];
        f1 = 100;
        f1 = (byte) (f1 * f1 / f1 + f1 - f1 / 2);
        System.out.println(f1);
        val[0] = f1;
        System.out.println(val[0]);
        for (int i = 0; i < val.length; i++) {
            val[i] = (byte) (5 % f0 - 2 * (9 + 5) / 2);
        }
        System.out.println("strs.length=" + val.length);
        for (int i = 0; i < val.length; i++) {
            System.out.println("val[" + i + "]=" + val[i]);
        }
    }

    public void t2() {

        short f0 = 100;
        short f1 = (short) (5 % f0 - 2 * (9 + 5) / 2);
        System.out.println("+ = " + (f0 + f1));
        System.out.println("- = " + (f0 - f1));
        System.out.println("* = " + (f0 * f1));
        System.out.println("/ = " + (f0 / f1));
        System.out.println("% = " + (f0 % f1));
        short[] val = new short[10];
        f1 = 1000;
        f1 = (short) (f1 * f1 / f1 + f1 - f1 / 2);
        System.out.println(f1);
        val[0] = f1;
        System.out.println(val[0]);
        for (int i = 0; i < val.length; i++) {
            val[i] = (short) (5 % f0 - 2 * (9 + 5) / 2);
        }
        System.out.println("strs.length=" + val.length);
        for (int i = 0; i < val.length; i++) {
            System.out.println("val[" + i + "]=" + val[i]);
        }
    }

    public void t3() {

        int f0 = 100;
        int f1 = 5 % f0 - 2 * (9 + 5) / 2;;
        System.out.println("+ = " + (f0 + f1));
        System.out.println("- = " + (f0 - f1));
        System.out.println("* = " + (f0 * f1));
        System.out.println("/ = " + (f0 / f1));
        System.out.println("% = " + (f0 % f1));
        int[] val = new int[10];
        f1 = 100000000;
        f1 = f1 * f1 / f1 + f1 - f1 / 2;
        System.out.println(f1);
        val[0] = f1;
        System.out.println(val[0]);
        for (int i = 0; i < val.length; i++) {
            val[i] = 5 % f0 - 2 * (9 + 5) / 2;
        }
        System.out.println("strs.length=" + val.length);
        for (int i = 0; i < val.length; i++) {
            System.out.println("val[" + i + "]=" + val[i]);
        }
    }

    public void t4() {

        long f0 = 100;
        long f1 = 5 % f0 - 2 * (9 + 5) / 2;;
        System.out.println("+ = " + (f0 + f1));
        System.out.println("- = " + (f0 - f1));
        System.out.println("* = " + (f0 * f1));
        System.out.println("/ = " + (f0 / f1));
        System.out.println("% = " + (f0 % f1));
        long[] val = new long[10];
        f1 = 1000000000000000L;
        f1 = f1 * f1 / f1 + f1 - f1 / 2;
        System.out.println(f1);
        val[0] = f1;
        System.out.println(val[0]);
        for (int i = 0; i < val.length; i++) {
            val[i] = 5 % f0 - 2 * (9 + 5) / 2;
        }
        System.out.println("strs.length=" + val.length);
        for (int i = 0; i < val.length; i++) {
            System.out.println("val[" + i + "]=" + val[i]);
        }
    }

    public void t5() {

        float f0 = 1.5f;
        float f1 = f0;
        System.out.println("+ = " + (f0 + f1));
        System.out.println("- = " + (f0 - f1));
        System.out.println("* = " + (f0 * f1));
        System.out.println("/ = " + (f0 / f1));
        System.out.println("% = " + (f0 % f1));
        float[] val = new float[10];
        f1 = 5 % f0 - 0.6f * (0.5f + 5) / 9.8f;
        System.out.println(f1);
        val[0] = f1;
        System.out.println(val[0]);
        for (int i = 0; i < val.length; i++) {
            val[i] = i % f0 - 0.6f * (0.5f + 5) / 9.8f;
        }
        System.out.println("strs.length=" + val.length);
        for (int i = 0; i < val.length; i++) {
            System.out.println("val[" + i + "]=" + val[i]);
        }
    }

    public void t6() {

        double f0 = 1.5f;
        double f1 = f0;
        System.out.println("+ = " + (f0 + f1));
        System.out.println("- = " + (f0 - f1));
        System.out.println("* = " + (f0 * f1));
        System.out.println("/ = " + (f0 / f1));
        System.out.println("% = " + (f0 % f1));
        double[] val = new double[10];
        f1 = 5 % f0 - 0.6f * (0.5f + 5) / 9.8f;
        System.out.println(f1);
        val[0] = f1;
        System.out.println(val[0]);
        for (int i = 0; i < val.length; i++) {
            val[i] = i % f0 - 0.6f * (0.5f + 5) / 9.8f;
        }
        System.out.println("strs.length=" + val.length);
        for (int i = 0; i < val.length; i++) {
            System.out.println("val[" + i + "]=" + val[i]);
        }
    }

    class T7class {

        Object obj_r;
    }

    void t7_1(T7class t7) {

        t7.obj_r = new Long(1);//在方法结束时，此对象应该被释放

        t7.obj_r = new Integer(2);

    }

    void t7() {
        T7class t7 = new T7class();
        t7_1(t7);
    }

    void t8() throws RuntimeException {
        int i = 0;
        try {
            i = 1;
            if (true) {
                throw new Exception("exception test 1");
            }
            i = 0;
        } catch (Exception e) {
            i = 2;
        }
        System.out.println("i=" + i);

        i = 3;

        System.out.println("i=" + i);
    }

    void t9() {
        int ch = 5;
        int v = 66;
        switch (ch) {
            case 4:
                v = 98;
                break;
            case 5:
                v = 85;
                break;
            case 6:
                v = 108;
                break;
            default:
                v = 90;
        }
        System.out.println("v=" + v);
    }

    void t10() {
        int ch = 1000;
        int v = 66;
        switch (ch) {
            case 1:
                v = 98;
                break;
            case 100:
                v = 85;
                break;
            case 1000:
                v = 108;
                break;
            default:
                v = 90;
        }
        System.out.println("v=" + v);
    }

    void t11() {
        int[][][] a3 = new int[2][2][];
        a3[1][1] = new int[3];
        a3[1][1][2] = 9;
        System.out.println("arr print:" + a3[1][1][2]);
    }

    public static void main(String args[]) {
        Foo1 obj = new Foo1();
//        obj.t0();
//        obj.t1();
//        obj.t2();
//        obj.t3();
//        obj.t4();
        obj.t5();
//        obj.t6();
//        obj.t7();
//        obj.t8();
//        obj.t9();
//        obj.t10();
//        obj.t11();
    }
}
