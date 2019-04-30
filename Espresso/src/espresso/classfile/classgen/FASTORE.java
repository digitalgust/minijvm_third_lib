package  espresso.classfile.classgen;




/** 
 * FASTORE -  Store into float array
 * Stack: ..., arrayref, index, value -> ...
 *
 * @version 971115
 * @author  <A HREF="http://www.inf.fu-berlin.de/~dahm">M. Dahm</A>
 */
public class FASTORE
    extends Instruction
{

    public FASTORE () {
        super(FASTORE, (short)1);
    }

}

