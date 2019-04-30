package  espresso.classfile.classgen;




/** 
 * SWAP - Swa top operand stack word
 * Stack: ..., word2, word1 -> ..., word1, word2
 *
 * @version 971115
 * @author  <A HREF="http://www.inf.fu-berlin.de/~dahm">M. Dahm</A>
 */
public class SWAP
    extends Instruction
{

    public SWAP () {
        super(SWAP, (short)1);
    }

}

