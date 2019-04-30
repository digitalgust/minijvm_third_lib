package  espresso.classfile.classgen;




/** 
 * MONITOREXIT - Exit monitor for object
 * Stack: ..., objectref -> ...
 *
 * @version 971204
 * @author  <A HREF="http://www.inf.fu-berlin.de/~dahm">M. Dahm</A>
 */
public class MONITOREXIT
    extends Instruction
{

    public MONITOREXIT () {
        super(MONITOREXIT, (short)1);
    }

}

