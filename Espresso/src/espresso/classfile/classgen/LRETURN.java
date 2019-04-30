package  espresso.classfile.classgen;




/** 
 * LRETURN -  Return long from method
 * Stack: ..., value.word1, value.word2 -> <empty>
 *
 * @version 971115
 * @author  <A HREF="http://www.inf.fu-berlin.de/~dahm">M. Dahm</A>
 */
public class LRETURN
    extends Instruction
{

    public LRETURN () {
        super(LRETURN, (short)1);
    }

}

