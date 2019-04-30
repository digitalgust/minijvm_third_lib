package  espresso.classfile.classgen;




/** 
 * DUP2_X1 - Duplicate two top operand stack words and put three down
 * Stack: ..., word3, word2, word1 -> ..., word2, word1, word3, word2, word1
 *
 * @version 971115
 * @author  <A HREF="http://www.inf.fu-berlin.de/~dahm">M. Dahm</A>
 */
public class DUP2_X1
    extends Instruction
{

    public DUP2_X1 () {
        super(DUP2_X1, (short)1);
    }

}

