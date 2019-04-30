package  espresso.classfile.classgen;




/** 
 * DUP - Duplicate top operand stack word
 * Stack: ..., word -> ..., word, word
 *
 * @version 971115
 * @author  <A HREF="http://www.inf.fu-berlin.de/~dahm">M. Dahm</A>
 */
public class DUP
    extends Instruction
{

    public DUP () {
        super(DUP, (short)1);
    }

}

