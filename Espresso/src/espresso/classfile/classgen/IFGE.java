package  espresso.classfile.classgen;




/** 
 * IFGE - Branch if int comparison with zero succeeds
 *
 * Stack: ..., value1, value2 -> ...
 *
 * @version 971124
 * @author  <A HREF="http://www.inf.fu-berlin.de/~dahm">M. Dahm</A>
 */
public class IFGE
    extends BranchInstruction
{

    /**
   * Empty constructor needed for the Class.newInstance() statement in
   * Instruction.readInstruction(). Not to be used otherwise.
   */
    IFGE () {

    }


    public IFGE (InstructionHandle target) {
        super(IFGE, target);
    }

}

