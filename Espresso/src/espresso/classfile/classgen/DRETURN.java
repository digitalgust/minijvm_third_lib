package  espresso.classfile.classgen;




/** 
 * DRETURN -  Return double from method
 * Stack: ..., value.word1, value.word2 -> <empty>
 *
 * @version 971115
 * @author  <A HREF="http://www.inf.fu-berlin.de/~dahm">M. Dahm</A>
 */
public class DRETURN
    extends Instruction
{

    public DRETURN () {
        super(DRETURN, (short)1);
    }

}

