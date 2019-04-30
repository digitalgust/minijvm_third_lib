package  espresso.classfile.classgen;




/** 
 * INVOKESPECIAL - Invoke instance method; special handling for superclass, private
 * and instance initialization method invocations
 *
 * Stack: ..., objectref, [arg1, [arg2 ...]] -> ...
 *
 * @version 971124
 * @author  <A HREF="http://www.inf.fu-berlin.de/~dahm">M. Dahm</A>
 */
public class INVOKESPECIAL
    extends CPInstruction
{

    /**
   * Empty constructor needed for the Class.newInstance() statement in
   * Instruction.readInstruction(). Not to be used otherwise.
   */
    INVOKESPECIAL () {

    }


    public INVOKESPECIAL (int index) {
        super(INVOKESPECIAL, index);
    }

}

