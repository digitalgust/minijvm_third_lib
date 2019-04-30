package  espresso.classfile.classgen;




/** 
 * IALOAD - Load int from array
 * Stack: ..., arrayref, index -> ..., value
 *
 * @version 971119
 * @author  <A HREF="http://www.inf.fu-berlin.de/~dahm">M. Dahm</A>
 */
public class IALOAD
    extends Instruction
{

    public IALOAD () {
        super(IALOAD, (short)1);
    }

}

