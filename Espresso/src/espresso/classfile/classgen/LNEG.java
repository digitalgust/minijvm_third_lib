package  espresso.classfile.classgen;




/** 
 * LNEG - Negate long
 * Stack: ..., value.word1, value.word2 -> ..., result.word1, result.word2
 *
 * @version 971115
 * @author  <A HREF="http://www.inf.fu-berlin.de/~dahm">M. Dahm</A>
 */
public class LNEG
    extends Instruction
{

    public LNEG () {
        super(LNEG, (short)1);
    }

}

