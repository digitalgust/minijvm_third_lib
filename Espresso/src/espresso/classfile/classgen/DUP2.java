package  espresso.classfile.classgen;




/** 
 * DUP2 - Duplicate two top operand stack words
 * Stack: ..., word2, word1 -> ..., word2, word1, word2, word1
 *
 * @version 971115
 * @author  <A HREF="http://www.inf.fu-berlin.de/~dahm">M. Dahm</A>
 */
public class DUP2
    extends Instruction
{

    public DUP2 () {
        super(DUP2, (short)1);
    }

}

