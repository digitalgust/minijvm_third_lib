package  espresso.classfile.classgen;




/** 
 * IMUL - Multiply ints
 * Stack: ..., value1, value2 -> result
 *
 * @version 971119
 * @author  <A HREF="http://www.inf.fu-berlin.de/~dahm">M. Dahm</A>
 */
public class IMUL
    extends Instruction
{

    public IMUL () {
        super(IMUL, (short)1);
    }

}

