package  espresso.classfile.classgen;




/** 
 * LUSHR - Logical shift right long
 * Stack: ..., value1, value2 -> ..., result
 *
 * @version 971119
 * @author  <A HREF="http://www.inf.fu-berlin.de/~dahm">M. Dahm</A>
 */
public class LUSHR
    extends Instruction
{

    public LUSHR () {
        super(LUSHR, (short)1);
    }

}

