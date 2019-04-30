package  espresso.classfile.classgen;




/** 
 * DNEG - Negate double
 * Stack: ..., value.word1, value.word2 -> ..., result.word1, result.word2
 *
 * @version 971115
 * @author  <A HREF="http://www.inf.fu-berlin.de/~dahm">M. Dahm</A>
 */
public class DNEG
    extends Instruction
{

    public DNEG () {
        super(DNEG, (short)1);
    }

}

