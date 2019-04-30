package  espresso.classfile.classgen;




/** 
 * FADD - Add floats
 * Stack: ..., value1, value2 -> result
 *
 * @version 971115
 * @author  <A HREF="http://www.inf.fu-berlin.de/~dahm">M. Dahm</A>
 */
public class FADD
    extends Instruction
{

    public FADD () {
        super(FADD, (short)1);
    }

}

