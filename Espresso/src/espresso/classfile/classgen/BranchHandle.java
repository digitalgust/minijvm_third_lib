package  espresso.classfile.classgen;




/**
 * BranchHandle is returned by specialized InstructionList.append() whenever a
 * BranchInstruction is appended. This is useful when the target of this
 * instruction is not known at time of creation and must be set later
 * via setTarget().
 *
 * @see InstructionHandle
 * @see Instruction
 * @see InstructionList
 * @version 980202
 * @author  <A HREF="http://www.inf.fu-berlin.de/~dahm">M. Dahm</A>
 */
public class BranchHandle
    extends InstructionHandle
{

    public BranchHandle (BranchInstruction i) {
        super(i);
    }


    /* 
     * We have to copy the (branch) instruction since it may be
     * used elsewhere and we alter it. This is user transparent.
     */
    public void setTarget (InstructionHandle ih) {
        BranchInstruction b =  (BranchInstruction)instruction.copy();
        instruction =  b;
        b.setTarget(ih);
    }

    /* 
     * We don't want to copy in this case.
     */
    public void setTargetNoCopy (InstructionHandle ih) {
        ((BranchInstruction) instruction).setTarget(ih);
    }

}

