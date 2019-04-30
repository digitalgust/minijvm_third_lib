package  espresso.classfile.classgen;




/** 
 * ARETURN -  Return reference from method
 * Stack: ..., objectref -> <empty>
 *
 * @version 971114
 * @author  <A HREF="http://www.inf.fu-berlin.de/~dahm">M. Dahm</A>
 */
public class ARETURN
    extends Instruction
{

    public ARETURN () {
        super(ARETURN, (short)1);
    }

}

