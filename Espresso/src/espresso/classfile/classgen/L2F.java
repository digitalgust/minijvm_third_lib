package  espresso.classfile.classgen;




/**
 * L2F - Convert long to float
 * Stack: ..., value.word1, value.word2 -> ..., result
 *
 * @version 971114
 * @author  <A HREF="http://www.inf.fu-berlin.de/~dahm">M. Dahm</A>
 */
public class L2F
    extends Instruction
{

    public L2F () {
        super(L2F, (short)1);
    }

}

