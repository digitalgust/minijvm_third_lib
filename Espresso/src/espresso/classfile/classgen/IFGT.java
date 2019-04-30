package  espresso.classfile.classgen;




/** 
 * IFGT - Branch if int comparison with zero succeeds
 *
 * Stack: ..., value1, value2 -> ...
 *
 * @version 971124
 * @author  <A HREF="http://www.inf.fu-berlin.de/~dahm">M. Dahm</A>
 */
public class IFGT
    extends BranchInstruction
{

    /**
   * Empty constructor needed for the Class.newInstance() statement in
   * Instruction.readInstruction(). Not to be used otherwise.
   */
    IFGT () {

    }


    public IFGT (InstructionHandle target) {
        super(IFGT, target);
    }

}

