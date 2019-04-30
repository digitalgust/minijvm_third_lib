package  espresso.classfile.classgen;




/** 
 * L2D - Convert long to double
 * Stack: ..., value.word1, value.word2 -> ..., result.word1, result.word2
 *
 * @version 971114
 * @author  <A HREF="http://www.inf.fu-berlin.de/~dahm">M. Dahm</A>
 */
public class L2D
    extends Instruction
{

    public L2D () {
        super(L2D, (short)1);
    }

}

