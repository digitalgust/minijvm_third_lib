package  espresso.classfile.classgen;




/** 
 * IUSHR - Logical shift right int
 * Stack: ..., value1, value2 -> ..., result
 *
 * @version 971119
 * @author  <A HREF="http://www.inf.fu-berlin.de/~dahm">M. Dahm</A>
 */
public class IUSHR
    extends Instruction
{

    public IUSHR () {
        super(IUSHR, (short)1);
    }

}

