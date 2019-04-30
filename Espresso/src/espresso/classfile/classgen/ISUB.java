package  espresso.classfile.classgen;




/** 
 * ISUB - Substract ints
 * Stack: ..., value1, value2 -> result
 *
 * @version 971119
 * @author  <A HREF="http://www.inf.fu-berlin.de/~dahm">M. Dahm</A>
 */
public class ISUB
    extends Instruction
{

    public ISUB () {
        super(ISUB, (short)1);
    }

}

