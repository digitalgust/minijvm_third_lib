package  espresso.classfile.classgen;




/** 
 * IF_ICMPLT - Branch if int comparison succeeds
 *
 * Stack: ..., value1, value2 -> ...
 *
 * @version 971124
 * @author  <A HREF="http://www.inf.fu-berlin.de/~dahm">M. Dahm</A>
 */
public class IF_ICMPLT
    extends BranchInstruction
{

    /**
   * Empty constructor needed for the Class.newInstance() statement in
   * Instruction.readInstruction(). Not to be used otherwise.
   */
    IF_ICMPLT () {

    }


    public IF_ICMPLT (InstructionHandle target) {
        super(IF_ICMPLT, target);
    }

}

