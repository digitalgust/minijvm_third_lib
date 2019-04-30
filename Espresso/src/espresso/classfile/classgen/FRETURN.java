package  espresso.classfile.classgen;




/** 
 * FRETURN -  Return float from method
 * Stack: ..., value -> <empty>
 *
 * @version 971115
 * @author  <A HREF="http://www.inf.fu-berlin.de/~dahm">M. Dahm</A>
 */
public class FRETURN
    extends Instruction
{

    public FRETURN () {
        super(FRETURN, (short)1);
    }

}

