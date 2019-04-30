package  espresso.classfile.classgen;




/** 
 * IF_ICMPGE - Branch if int comparison succeeds
 *
 * Stack: ..., value1, value2 -> ...
 *
 * @version 971124
 * @author  <A HREF="http://www.inf.fu-berlin.de/~dahm">M. Dahm</A>
 */
public class IF_ICMPGE
    extends BranchInstruction
{

    /**
   * Empty constructor needed for the Class.newInstance() statement in
   * Instruction.readInstruction(). Not to be used otherwise.
   */
    IF_ICMPGE () {

    }


    public IF_ICMPGE (InstructionHandle target) {
        super(IF_ICMPGE, target);
    }

}

