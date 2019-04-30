package  espresso.classfile.classgen;




/** 
 * DCMPG - Compare doubles: value1 > value2
 * Stack: ..., value1.word1, value1.word2, value2.word1, value2.word2 ->
 *        ..., result
 *
 * @version 971114
 * @author  <A HREF="http://www.inf.fu-berlin.de/~dahm">M. Dahm</A>
 */
public class DCMPG
    extends Instruction
{

    public DCMPG () {
        super(DCMPG, (short)1);
    }

}

