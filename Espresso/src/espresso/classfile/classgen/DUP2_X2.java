package  espresso.classfile.classgen;




/** 
 * DUP2_X2 - Duplicate two top operand stack words and put four down
 * Stack: ..., word4, word3, word2, word1 -> ..., word2, word1, word4, word3, word2, word1
 *
 * @version 971115
 * @author  <A HREF="http://www.inf.fu-berlin.de/~dahm">M. Dahm</A>
 */
public class DUP2_X2
    extends Instruction
{

    public DUP2_X2 () {
        super(DUP2_X2, (short)1);
    }

}

