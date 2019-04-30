package  espresso.classfile.classgen;




/**
 * FSUB - Substract floats
 * Stack: ..., value1, value2 -> result
 *
 * @version 971115
 * @author  <A HREF="http://www.inf.fu-berlin.de/~dahm">M. Dahm</A>
 */
public class FSUB
    extends Instruction
{

    public FSUB () {
        super(FSUB, (short)1);
    }

}

