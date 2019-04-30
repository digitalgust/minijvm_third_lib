package  espresso.classfile.classgen;




/** 
 * IRETURN -  Return int from method
 * Stack: ..., value -> <empty>
 *
 * @version 971115
 * @author  <A HREF="http://www.inf.fu-berlin.de/~dahm">M. Dahm</A>
 */
public class IRETURN
    extends Instruction
{

    public IRETURN () {
        super(IRETURN, (short)1);
    }

}

