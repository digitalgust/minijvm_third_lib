package  espresso.classfile.classgen;




/** 
 * FCMPG - Compare floats: value1 > value2
 * Stack: ..., value1, value2 -> ..., result
 *
 * @version 971115
 * @author  <A HREF="http://www.inf.fu-berlin.de/~dahm">M. Dahm</A>
 */
public class FCMPG
    extends Instruction
{

    public FCMPG () {
        super(FCMPG, (short)1);
    }

}

