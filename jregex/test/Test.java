
import jregex.Matcher;
import jregex.Pattern;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author gust
 */
public class Test {

    public static void main(String[] args) {

        t1();
        t2();

    }

    static void t1() {
        Matcher m = new Pattern("\\w+").matcher("abc");
        while (m.proceed(0)) {
            System.out.println(m.group(0));
        }

    }

    static void t2() {
        Matcher m = new Pattern("\\d+").matcher("123");
        while (m.proceed(0)) {
            String match = m.group(0);
            if (isOdd(Integer.parseInt(match))) {
                System.out.println(match);
            }
        }
Pattern LOOKS_LIKE_TYPE_PARAMETER = Pattern.compile("\\p{javaUpperCase}+");
         
    }

    static boolean isOdd(int i) {
        return (i & 1) > 0;
    }
}
