package  espresso.classfile.classgen;




/** 
 * LSHR - Arithmetic shift right long
 * Stack: ..., value1, value2 -> ..., result
 *
 * @version 971119
 * @author  <A HREF="http://www.inf.fu-berlin.de/~dahm">M. Dahm</A>
 */
public class LSHR
    extends Instruction
{

    public LSHR () {
        super(LSHR, (short)1);
    }

}

