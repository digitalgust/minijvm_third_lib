package  espresso.classfile.classgen;




/** 
 * AALOAD - Load reference from array
 * Stack ..., arrayref, index -> value
 *
 * @version 971114
 * @author  <A HREF="http://www.inf.fu-berlin.de/~dahm">M. Dahm</A>
 */
public class AALOAD
    extends Instruction
{

    public AALOAD () {
        super(AALOAD, (short)1);
    }

}

