package  espresso.classfile.classgen;




/** 
 * DUP_X2 - Duplicate top operand stack word and put three down
 * Stack: ..., word3, word2, word1 -> ..., word1, word3, word2, word1
 *
 * @version 971115
 * @author  <A HREF="http://www.inf.fu-berlin.de/~dahm">M. Dahm</A>
 */
public class DUP_X2
    extends Instruction
{

    public DUP_X2 () {
        super(DUP_X2, (short)1);
    }

}

