package  espresso.classfile.classgen;




/**
 * ISHR - Arithmetic shift right int
 * Stack: ..., value1, value2 -> ..., result
 *
 * @version 971119
 * @author  <A HREF="http://www.inf.fu-berlin.de/~dahm">M. Dahm</A>
 */
public class ISHR
    extends Instruction
{

    public ISHR () {
        super(ISHR, (short)1);
    }

}

