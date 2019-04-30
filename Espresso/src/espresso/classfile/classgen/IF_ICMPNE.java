package  espresso.classfile.classgen;




/** 
 * IF_ICMPNE - Branch if int comparison doesn't succeed
 *
 * Stack: ..., value1, value2 -> ...
 *
 * @version 971124
 * @author  <A HREF="http://www.inf.fu-berlin.de/~dahm">M. Dahm</A>
 */
public class IF_ICMPNE
    extends BranchInstruction
{

    /**
   * Empty constructor needed for the Class.newInstance() statement in
   * Instruction.readInstruction(). Not to be used otherwise.
   */
    IF_ICMPNE () {

    }


    public IF_ICMPNE (InstructionHandle target) {
        super(IF_ICMPNE, target);
    }

}

