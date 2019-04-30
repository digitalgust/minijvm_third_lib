package  espresso.classfile.classgen;




/** 
 * Thrown on internal errors. Extends RuntimeException so it hasn't to be declared
 * in the throws clause every time.
 *
 * @version 971120
 * @author  <A HREF="http://www.inf.fu-berlin.de/~dahm">M. Dahm</A>
 */
public class ClassGenException
    extends RuntimeException
{

    public ClassGenException () {
        super();
    }


    public ClassGenException (String s) {
        super(s);
    }

}

