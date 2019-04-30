package  espresso.classfile.classgen;




/**
 * LCMP - Compare longs:
 * Stack: ..., value1.word1, value1.word2, value2.word1, value2.word2 ->
 *        ..., result <= -1, 0, 1>
 *
 * @version 971124
 * @author  <A HREF="http://www.inf.fu-berlin.de/~dahm">M. Dahm</A>
 */
public class LCMP
    extends Instruction
{

    public LCMP () {
        super(LCMP, (short)1);
    }

}

