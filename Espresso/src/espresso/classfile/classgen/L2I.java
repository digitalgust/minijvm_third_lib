package  espresso.classfile.classgen;




/**
 * L2I - Convert long to int
 * Stack: ..., value.word1, value.word2 -> ..., result
 *
 * @version 971114
 * @author  <A HREF="http://www.inf.fu-berlin.de/~dahm">M. Dahm</A>
 */
public class L2I
    extends Instruction
{

    public L2I () {
        super(L2I, (short)1);
    }

}

