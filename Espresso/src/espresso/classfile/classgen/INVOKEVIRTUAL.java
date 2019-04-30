package  espresso.classfile.classgen;




/** 
 * INVOKEVIRTUAL - Invoke instance method; dispatch based on class
 *
 * Stack: ..., objectref, [arg1, [arg2 ...]] -> ...
 *
 * @version 971124
 * @author  <A HREF="http://www.inf.fu-berlin.de/~dahm">M. Dahm</A>
 */
public class INVOKEVIRTUAL
    extends CPInstruction
{

    /**
   * Empty constructor needed for the Class.newInstance() statement in
   * Instruction.readInstruction(). Not to be used otherwise.
   */
    INVOKEVIRTUAL () {

    }


    public INVOKEVIRTUAL (int index) {
        super(INVOKEVIRTUAL, index);
    }

}

