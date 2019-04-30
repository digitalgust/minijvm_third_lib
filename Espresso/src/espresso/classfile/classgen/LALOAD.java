package  espresso.classfile.classgen;




/** 
 * LALOAD - Load long from array
 * Stack: ..., arrayref, index -> ..., value1, value2
 *
 * @version 971115
 * @author  <A HREF="http://www.inf.fu-berlin.de/~dahm">M. Dahm</A>
 */
public class LALOAD
    extends Instruction
{

    public LALOAD () {
        super(LALOAD, (short)1);
    }

}

