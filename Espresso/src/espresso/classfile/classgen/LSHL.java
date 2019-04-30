package  espresso.classfile.classgen;




/** 
 * LSHL - Arithmetic shift left long
 * Stack: ..., value1, value2 -> ..., result
 *
 * @version 971119
 * @author  <A HREF="http://www.inf.fu-berlin.de/~dahm">M. Dahm</A>
 */
public class LSHL
    extends Instruction
{

    public LSHL () {
        super(LSHL, (short)1);
    }

}

