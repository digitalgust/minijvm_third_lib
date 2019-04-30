package  espresso.classfile.classgen;




/**
 * SASTORE - Store into short array
 * Stack: ..., arrayref, index, value -> ...
 *
 * @version 971115
 * @author  <A HREF="http://www.inf.fu-berlin.de/~dahm">M. Dahm</A>
 */
public class SASTORE
    extends Instruction
{

    public SASTORE () {
        super(SASTORE, (short)1);
    }

}

