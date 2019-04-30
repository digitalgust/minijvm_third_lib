package  espresso.classfile.classgen;




/** 
 * BASTORE -  Store into byte or boolean array
 * Stack: ..., arrayref, index, value -> ...
 *
 * @version 971114
 * @author  <A HREF="http://www.inf.fu-berlin.de/~dahm">M. Dahm</A>
 */
public class BASTORE
    extends Instruction
{

    public BASTORE () {
        super(BASTORE, (short)1);
    }

}

