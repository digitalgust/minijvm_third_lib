package  espresso.classfile.classgen;




/** 
 * F2I - Convert float to int
 * Stack: ..., value -> ..., result
 *
 * @version 971115
 * @author  <A HREF="http://www.inf.fu-berlin.de/~dahm">M. Dahm</A>
 */
public class F2I
    extends Instruction
{

    public F2I () {
        super(F2I, (short)1);
    }

}

