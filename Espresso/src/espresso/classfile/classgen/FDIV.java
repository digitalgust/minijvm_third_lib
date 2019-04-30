package  espresso.classfile.classgen;




/**
 * FDIV - Divide floats
 * Stack: ..., value1, value2 -> result
 *
 * @version 971115
 * @author  <A HREF="http://www.inf.fu-berlin.de/~dahm">M. Dahm</A>
 */
public class FDIV
    extends Instruction
{

    public FDIV () {
        super(FDIV, (short)1);
    }

}

