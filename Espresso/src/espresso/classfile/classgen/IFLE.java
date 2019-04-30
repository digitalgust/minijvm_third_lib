package  espresso.classfile.classgen;




/** 
 * IFLE - Branch if int comparison with zero succeeds
 *
 * Stack: ..., value1, value2 -> ...
 *
 * @version 971124
 * @author  <A HREF="http://www.inf.fu-berlin.de/~dahm">M. Dahm</A>
 */
public class IFLE
    extends BranchInstruction
{

    /**
   * Empty constructor needed for the Class.newInstance() statement in
   * Instruction.readInstruction(). Not to be used otherwise.
   */
    IFLE () {

    }


    public IFLE (InstructionHandle target) {
        super(IFLE, target);
    }

}

