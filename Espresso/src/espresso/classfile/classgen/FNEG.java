package  espresso.classfile.classgen;




/** 
 * FNEG - Negate float
 * Stack: ..., value -> ..., result
 *
 * @version 971115
 * @author  <A HREF="http://www.inf.fu-berlin.de/~dahm">M. Dahm</A>
 */
public class FNEG
    extends Instruction
{

    public FNEG () {
        super(FNEG, (short)1);
    }

}

