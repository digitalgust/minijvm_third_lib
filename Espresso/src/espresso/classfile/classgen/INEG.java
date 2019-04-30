package  espresso.classfile.classgen;




/** 
 * INEG - Negate int
 * Stack: ..., value -> ..., result
 *
 * @version 971124
 * @author  <A HREF="http://www.inf.fu-berlin.de/~dahm">M. Dahm</A>
 */
public class INEG
    extends Instruction
{

    public INEG () {
        super(INEG, (short)1);
    }

}

