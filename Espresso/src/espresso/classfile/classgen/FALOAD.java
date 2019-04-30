package  espresso.classfile.classgen;




/** 
 * FALOAD - Load float from array
 * Stack: ..., arrayref, index -> ..., value
 *
 * @version 971115
 * @author  <A HREF="http://www.inf.fu-berlin.de/~dahm">M. Dahm</A>
 */
public class FALOAD
    extends Instruction
{

    public FALOAD () {
        super(FALOAD, (short)1);
    }

}

