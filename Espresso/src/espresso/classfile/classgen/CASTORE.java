package  espresso.classfile.classgen;




/** 
 * CASTORE -  Store into char array
 * Stack: ..., arrayref, index, value -> ...
 *
 * @version 971114
 * @author  <A HREF="http://www.inf.fu-berlin.de/~dahm">M. Dahm</A>
 */
public class CASTORE
    extends Instruction
{

    public CASTORE () {
        super(CASTORE, (short)1);
    }

}

