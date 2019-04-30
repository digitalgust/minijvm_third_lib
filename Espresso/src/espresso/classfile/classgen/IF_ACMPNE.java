package  espresso.classfile.classgen;




/** 
 * IF_ACMPNE - Branch if reference comparison doesn't succeed
 *
 * Stack: ..., value1, value2 -> ...
 *
 * @version 971124
 * @author  <A HREF="http://www.inf.fu-berlin.de/~dahm">M. Dahm</A>
 */
public class IF_ACMPNE
    extends BranchInstruction
{

    /**
   * Empty constructor needed for the Class.newInstance() statement in
   * Instruction.readInstruction(). Not to be used otherwise.
   */
    IF_ACMPNE () {

    }


    public IF_ACMPNE (InstructionHandle target) {
        super(IF_ACMPNE, target);
    }

}

