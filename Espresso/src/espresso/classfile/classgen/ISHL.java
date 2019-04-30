package  espresso.classfile.classgen;




/**
 * ISHL - Arithmetic shift left int
 * Stack: ..., value1, value2 -> ..., result
 *
 * @version 971119
 * @author  <A HREF="http://www.inf.fu-berlin.de/~dahm">M. Dahm</A>
 */
public class ISHL
    extends Instruction
{

    public ISHL () {
        super(ISHL, (short)1);
    }

}

