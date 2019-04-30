package  espresso.classfile.classgen;




/** 
 * DSUB - Substract doubles
 * Stack: ..., value1.word1, value1.word2, value2.word1, value2.word2 ->
 *        ..., result.word1, result.word2
 *
 * @version 971115
 * @author  <A HREF="http://www.inf.fu-berlin.de/~dahm">M. Dahm</A>
 */
public class DSUB
    extends Instruction
{

    public DSUB () {
        super(DSUB, (short)1);
    }

}

