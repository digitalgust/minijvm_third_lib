package  espresso.classfile.classgen;




/**
 * POP - Pop top operand stack word
 *
 * Stack: ..., word -> ...
 *
 * @version 971115
 * @author  <A HREF="http://www.inf.fu-berlin.de/~dahm">M. Dahm</A>
 */
public class POP
    extends Instruction
{

    public POP () {
        super(POP, (short)1);
    }

}

