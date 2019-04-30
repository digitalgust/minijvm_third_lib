package  espresso.classfile.classgen;




/** 
 * LMUL - Multiply longs
 * Stack: ..., value1.word1, value1.word2, value2.word1, value2.word2 ->
 *        ..., result.word1, result.word2
 *
 * @version 971119
 * @author  <A HREF="http://www.inf.fu-berlin.de/~dahm">M. Dahm</A>
 */
public class LMUL
    extends Instruction
{

    public LMUL () {
        super(LMUL, (short)1);
    }

}

