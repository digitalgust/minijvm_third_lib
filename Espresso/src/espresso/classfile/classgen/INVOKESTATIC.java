package  espresso.classfile.classgen;




/** 
 * INVOKESTATIC - Invoke a class (static) method
 *
 * Stack: ..., [arg1, [arg2 ...]] -> ...
 *
 * @version 971124
 * @author  <A HREF="http://www.inf.fu-berlin.de/~dahm">M. Dahm</A>
 */
public class INVOKESTATIC
    extends CPInstruction
{

    /**
   * Empty constructor needed for the Class.newInstance() statement in
   * Instruction.readInstruction(). Not to be used otherwise.
   */
    INVOKESTATIC () {

    }


    public INVOKESTATIC (int index) {
        super(INVOKESTATIC, index);
    }

}

