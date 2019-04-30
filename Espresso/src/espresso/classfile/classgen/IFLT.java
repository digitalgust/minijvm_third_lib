package  espresso.classfile.classgen;




/** 
 * IFLT - Branch if int comparison with zero succeeds
 *
 * Stack: ..., value1, value2 -> ...
 *
 * @version 971124
 * @author  <A HREF="http://www.inf.fu-berlin.de/~dahm">M. Dahm</A>
 */
public class IFLT
    extends BranchInstruction
{

    /**
   * Empty constructor needed for the Class.newInstance() statement in
   * Instruction.readInstruction(). Not to be used otherwise.
   */
    IFLT () {

    }


    public IFLT (InstructionHandle target) {
        super(IFLT, target);
    }

}

