package  espresso.classfile.classgen;




/** 
 * IFEQ - Branch if int comparison with zero succeeds
 *
 * Stack: ..., value1, value2 -> ...
 *
 * @version 971124
 * @author  <A HREF="http://www.inf.fu-berlin.de/~dahm">M. Dahm</A>
 */
public class IFEQ
    extends BranchInstruction
{

    /**
   * Empty constructor needed for the Class.newInstance() statement in
   * Instruction.readInstruction(). Not to be used otherwise.
   */
    IFEQ () {

    }


    public IFEQ (InstructionHandle target) {
        super(IFEQ, target);
    }

}

