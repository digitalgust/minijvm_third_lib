package  espresso.classfile.classgen;




/** 
 * CALOAD - Load char from array
 * Stack: ..., arrayref, index -> ..., value
 *
 * @version 971114
 * @author  <A HREF="http://www.inf.fu-berlin.de/~dahm">M. Dahm</A>
 */
public class CALOAD
    extends Instruction
{

    public CALOAD () {
        super(CALOAD, (short)1);
    }

}

