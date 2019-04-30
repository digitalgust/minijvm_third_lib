package  espresso.classfile.classgen;




/** 
 * ATHROW -  Throw exception
 * Stack: ..., objectref -> objectref
 *
 * @version 971114
 * @author  <A HREF="http://www.inf.fu-berlin.de/~dahm">M. Dahm</A>
 */
public class ATHROW
    extends Instruction
{

    public ATHROW () {
        super(ATHROW, (short)1);
    }

}

