package  espresso.classfile.classgen;




/**
 * IDIV - Divide ints
 * Stack: ..., value1, value2 -> result
 *
 * @version 971124
 * @author  <A HREF="http://www.inf.fu-berlin.de/~dahm">M. Dahm</A>
 */
public class IDIV
    extends Instruction
{

    public IDIV () {
        super(IDIV, (short)1);
    }

}

