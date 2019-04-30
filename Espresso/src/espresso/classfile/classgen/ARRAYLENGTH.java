package  espresso.classfile.classgen;




/** 
 * ARRAYLENGTH -  Get length of array
 * Stack: ..., arrayref -> ..., length
 *
 * @version 971114
 * @author  <A HREF="http://www.inf.fu-berlin.de/~dahm">M. Dahm</A>
 */
public class ARRAYLENGTH
    extends Instruction
{

    public ARRAYLENGTH () {
        super(ARRAYLENGTH, (short)1);
    }

}

