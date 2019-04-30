package  espresso.classfile.classgen;




/** 
 * SALOAD - Load short from array
 * Stack: ..., arrayref, index -> ..., value
 *
 * @version 971115
 * @author  <A HREF="http://www.inf.fu-berlin.de/~dahm">M. Dahm</A>
 */
public class SALOAD
    extends Instruction
{

    public SALOAD () {
        super(SALOAD, (short)1);
    }

}

