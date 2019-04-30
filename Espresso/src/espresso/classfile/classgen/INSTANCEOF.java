package  espresso.classfile.classgen;




/** 
 * INSTANCEOF - Determine if object is of given type
 * Stack: ..., objectref -> ..., result
 *
 * @version 971124
 * @author  <A HREF="http://www.inf.fu-berlin.de/~dahm">M. Dahm</A>
 */
public class INSTANCEOF
    extends CPInstruction
{

    /**
   * Empty constructor needed for the Class.newInstance() statement in
   * Instruction.readInstruction(). Not to be used otherwise.
   */
    INSTANCEOF () {

    }


    public INSTANCEOF (int index) {
        super(INSTANCEOF, index);
    }

}

