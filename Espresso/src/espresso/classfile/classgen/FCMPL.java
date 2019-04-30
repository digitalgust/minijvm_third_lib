package  espresso.classfile.classgen;




/** 
 * FCMPL - Compare floats: value1 < value2
 * Stack: ..., value1, value2 -> ..., result
 *
 * @version 971115
 * @author  <A HREF="http://www.inf.fu-berlin.de/~dahm">M. Dahm</A>
 */
public class FCMPL
    extends Instruction
{

    public FCMPL () {
        super(FCMPL, (short)1);
    }

}

