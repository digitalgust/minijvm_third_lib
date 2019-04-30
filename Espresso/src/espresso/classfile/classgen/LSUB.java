package  espresso.classfile.classgen;




/** 
 * LSUB - Substract longs
 * Stack: ..., value1.word1, value1.word2, value2.word1, value2.word2 ->
 *        ..., result.word1, result.word2
 *
 * @version 971115
 * @author  <A HREF="http://www.inf.fu-berlin.de/~dahm">M. Dahm</A>
 */
public class LSUB
    extends Instruction
{

    public LSUB () {
        super(LSUB, (short)1);
    }

}

