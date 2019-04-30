package  espresso.classfile.classgen;




/** 
 * BALOAD - Load byte or boolean from array
 * Stack: ..., arrayref, index -> ..., value
 *
 * @version 971114
 * @author  <A HREF="http://www.inf.fu-berlin.de/~dahm">M. Dahm</A>
 */
public class BALOAD
    extends Instruction
{

    public BALOAD () {
        super(BALOAD, (short)1);
    }

}

