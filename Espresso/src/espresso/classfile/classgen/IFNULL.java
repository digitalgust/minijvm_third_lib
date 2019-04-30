package  espresso.classfile.classgen;




/** 
 * IFNULL - Branch if reference is not null
 *
 * Stack: ..., reference -> ...
 *
 * @version 971124
 * @author  <A HREF="http://www.inf.fu-berlin.de/~dahm">M. Dahm</A>
 */
public class IFNULL
    extends BranchInstruction
{

    /**
   * Empty constructor needed for the Class.newInstance() statement in
   * Instruction.readInstruction(). Not to be used otherwise.
   */
    IFNULL () {

    }


    public IFNULL (InstructionHandle target) {
        super(IFNULL, target);
    }

}

