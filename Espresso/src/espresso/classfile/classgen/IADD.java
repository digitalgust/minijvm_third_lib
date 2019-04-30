package  espresso.classfile.classgen;




/** 
 * IADD - Add ints
 * Stack: ..., value1, value2 -> result
 *
 * @version 971115
 * @author  <A HREF="http://www.inf.fu-berlin.de/~dahm">M. Dahm</A>
 */
public class IADD
    extends Instruction
{

    public IADD () {
        super(IADD, (short)1);
    }

}

