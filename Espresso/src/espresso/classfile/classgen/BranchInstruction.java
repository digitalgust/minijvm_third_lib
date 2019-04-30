package  espresso.classfile.classgen;




import  java.io.*;
import  espresso.classfile.util.ByteSequence;




/** 
 * Abstract super class for branching instructions like GOTO, IFEQ, etc..
 * Branch instructions may have a variable length, namely GOTO, JSR, 
 * LOOKUPSWITCH and TABLESWITCH.
 *
 * @see InstructionList
 * @version 971120
 * @author  <A HREF="http://www.inf.fu-berlin.de/~dahm">M. Dahm</A>
 */
public abstract class BranchInstruction
    extends Instruction
{

    protected int index; // Branch target relative to this instruction
    protected InstructionHandle target; // Target object in instruction list


    /**
   * Empty constructor needed for the Class.newInstance() statement in
   * Instruction.readInstruction(). Not to be used otherwise.
   */
    BranchInstruction () {

    }


    /**
   * @param instruction Target instruction to branch to
   */
    protected BranchInstruction (short tag, InstructionHandle target) {
        super(tag, (short)3);
        this.target =  target;
    }


    /**
   * Dump instruction as byte code to stream out.
   * @param out Output stream
   */
    public void dump (DataOutputStream out)
        throws IOException
    {
        out.writeByte(tag);
        index =  getTargetOffset();
        if (Math.abs(index) >= 32767) // too large for short
        throw  new ClassGenException("Branch target offset too large for short");
        out.writeShort(index); // May be negative, i.e. point backwards
    }


    /**
   * @return target of branch instruction
   */
    public InstructionHandle getTarget () {
        return  target;
    }


    /**
   * Set branch target
   */
    public void setTarget (InstructionHandle target) {
        this.target =  target;
    }


    /**
   * @param target branch target
   * @return the offset to  `target' relative to this instruction
   */
    protected int getTargetOffset (Instruction target) {
        if (target == null) throw  new ClassGenException("Target of instruction is invalid null instruction");
        int t =  target.getPosition();
        if (t < 0) throw  new ClassGenException("Invalid branch target position offset");
        return  t - position;
    }


    /**
   * @param target branch target
   * @return the offset to  `target' relative to this instruction
   */
    protected int getTargetOffset (InstructionHandle target) {
        return  getTargetOffset(target.getInstruction());
    }


    /**
   * @return the offset to this instruction's target
   */
    protected int getTargetOffset () {
        return  getTargetOffset(target);
    }


    /**
   * @return mnemonic for instruction
   */
    public String toString () {
        String s =  "null";
        if (target != null) s =  target.getInstruction().toString();
        return  super.toString() + " <" + s + ">@" + index;
    }


    /**
   * Read needed data (e.g. index) from file. Conversion to a InstructionHandle
   * is done in InstructionList(byte[]).
   *
   * @see InstructionList
   */
    protected void initFromFile (ByteSequence bytes, boolean wide)
        throws IOException
    {
        length =  3;
        index =  bytes.readShort();
    }


    /**
   * @return target offset
   */
    final int getIndex () {
        return  index;
    }

}

