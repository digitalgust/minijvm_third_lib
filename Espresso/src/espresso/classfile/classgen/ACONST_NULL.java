package  espresso.classfile.classgen;




/** 
 * ACONST_NULL -  Push null
 * Stack: ... -> ..., null
 *
 * @version 971114
 * @author  <A HREF="http://www.inf.fu-berlin.de/~dahm">M. Dahm</A>
 */
public class ACONST_NULL
    extends Instruction
{

    public ACONST_NULL () {
        super(ACONST_NULL, (short)1);
    }

}

