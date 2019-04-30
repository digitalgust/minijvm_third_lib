package  espresso.classfile.classgen;




/** 
 * IF_ACMPEQ - Branch if reference comparison succeeds
 *
 * Stack: ..., value1, value2 -> ...
 *
 * @version 971124
 * @author  <A HREF="http://www.inf.fu-berlin.de/~dahm">M. Dahm</A>
 */
public class IF_ACMPEQ
    extends BranchInstruction
{

    /**
   * Empty constructor needed for the Class.newInstance() statement in
   * Instruction.readInstruction(). Not to be used otherwise.
   */
    IF_ACMPEQ () {

    }


    public IF_ACMPEQ (InstructionHandle target) {
        super(IF_ACMPEQ, target);
    }

}

