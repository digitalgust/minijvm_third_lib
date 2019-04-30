package  espresso.classfile.classgen;




/**
 * POP2 - Pop two top operand stack words
 *
 * Stack: ..., word2, word1 -> ...
 *
 * @version 971115
 * @author  <A HREF="http://www.inf.fu-berlin.de/~dahm">M. Dahm</A>
 */
public class POP2
    extends Instruction
{

    public POP2 () {
        super(POP2, (short)1);
    }

}

