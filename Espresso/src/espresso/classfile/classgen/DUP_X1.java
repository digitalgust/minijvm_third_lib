package  espresso.classfile.classgen;




/** 
 * DUP_X1 - Duplicate top operand stack word and put two down
 * Stack: ..., word2, word1 -> ..., word1, word2, word1
 *
 * @version 971115
 * @author  <A HREF="http://www.inf.fu-berlin.de/~dahm">M. Dahm</A>
 */
public class DUP_X1
    extends Instruction
{

    public DUP_X1 () {
        super(DUP_X1, (short)1);
    }

}

