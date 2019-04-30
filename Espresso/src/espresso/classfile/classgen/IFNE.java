package  espresso.classfile.classgen;




/** 
 * IFNE - Branch if int comparison with zero succeeds
 *
 * Stack: ..., value1, value2 -> ...
 *
 * @version 971124
 * @author  <A HREF="http://www.inf.fu-berlin.de/~dahm">M. Dahm</A>
 */
public class IFNE
    extends BranchInstruction
{

    /**
   * Empty constructor needed for the Class.newInstance() statement in
   * Instruction.readInstruction(). Not to be used otherwise.
   */
    IFNE () {

    }


    public IFNE (InstructionHandle target) {
        super(IFNE, target);
    }

}

