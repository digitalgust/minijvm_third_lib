package  espresso.classfile.classgen;




/** 
 * AASTORE -  Store into reference array
 * Stack: ..., arrayref, index, value -> ...
 *
 * @version 971114
 * @author  <A HREF="http://www.inf.fu-berlin.de/~dahm">M. Dahm</A>
 */
public class AASTORE
    extends Instruction
{

    public AASTORE () {
        super(AASTORE, (short)1);
    }

}

