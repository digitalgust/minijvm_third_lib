package  espresso.classfile.classgen;




/** 
 * DREM - Remainder of doubles
 * Stack: ..., value1.word1, value1.word2, value2.word1, value2.word2 ->
 *        ..., result.word1, result.word2
 *
 * @version 971115
 * @author  <A HREF="http://www.inf.fu-berlin.de/~dahm">M. Dahm</A>
 */
public class DREM
    extends Instruction
{

    public DREM () {
        super(DREM, (short)1);
    }

}

