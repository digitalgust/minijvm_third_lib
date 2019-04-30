package  espresso.classfile.classgen;




/** 
 * RETURN -  Return from void method
 * Stack: ... -> <empty>
 *
 * @version 971124
 * @author  <A HREF="http://www.inf.fu-berlin.de/~dahm">M. Dahm</A>
 */
public class RETURN
    extends Instruction
{

    public RETURN () {
        super(RETURN, (short)1);
    }

}

