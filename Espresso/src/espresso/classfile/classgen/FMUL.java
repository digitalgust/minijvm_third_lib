package  espresso.classfile.classgen;




/**
 * FMUL - Multiply floats
 * Stack: ..., value1, value2 -> result
 *
 * @version 971115
 * @author  <A HREF="http://www.inf.fu-berlin.de/~dahm">M. Dahm</A>
 */
public class FMUL
    extends Instruction
{

    public FMUL () {
        super(FMUL, (short)1);
    }

}

