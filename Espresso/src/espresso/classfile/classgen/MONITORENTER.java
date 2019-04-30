package  espresso.classfile.classgen;




/** 
 * MONITORENTER - Enter monitor for object
 * Stack: ..., objectref -> ...
 *
 * @version 971204
 * @author  <A HREF="http://www.inf.fu-berlin.de/~dahm">M. Dahm</A>
 */
public class MONITORENTER
    extends Instruction
{

    public MONITORENTER () {
        super(MONITORENTER, (short)1);
    }

}

