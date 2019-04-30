package  espresso.classfile.classgen;




/** 
 * IF_ICMPGT - Branch if int comparison succeeds
 *
 * Stack: ..., value1, value2 -> ...
 *
 * @version 971124
 * @author  <A HREF="http://www.inf.fu-berlin.de/~dahm">M. Dahm</A>
 */
public class IF_ICMPGT
    extends BranchInstruction
{

    /**
   * Empty constructor needed for the Class.newInstance() statement in
   * Instruction.readInstruction(). Not to be used otherwise.
   */
    IF_ICMPGT () {

    }


    public IF_ICMPGT (InstructionHandle target) {
        super(IF_ICMPGT, target);
    }

}

