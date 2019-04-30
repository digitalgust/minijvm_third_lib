package  espresso.classfile.classgen;




import  java.io.*;
import  espresso.classfile.util.ByteSequence;




/** 
 * Select - Abstract super class for LOOKUPSWITCH and TABLESWITCH instructions.
 *
 * @version 980112
 * @author  <A HREF="http://www.inf.fu-berlin.de/~dahm">M. Dahm</A>
 * @see LOOKUPSWITCH
 * @see TABLESWITCH
 * @see InstructionList
 */
abstract class Select
    extends BranchInstruction
    implements VariableLengthInstruction
{

    protected int[] match; // matches, i.e. case 1: ...
    protected int[] indices; // target offsets
    protected InstructionHandle[] targets; // target objects in instruction list
    protected int fixed_length; // fixed length defined by subclasses
    protected int match_length; // number of cases
    private int padding =  0; // number of pad bytes for alignment


    /**
   * Empty constructor needed for the Class.newInstance() statement in
   * Instruction.readInstruction(). Not to be used otherwise.
   */
    Select () {

    }


    /**
   * (Match, target) pairs for switch.
   * `Match' and `targets' must have the same length of course.
   *
   * @param match array of matching values
   * @param targets instruction targets
   * @param target default instruction target
   */
    Select (short tag, int[] match, InstructionHandle[] targets, InstructionHandle target) {
        super(tag, target);
        this.match =  match;
        this.targets =  targets;
        if ((match_length =  match.length) != targets.length) throw  new ClassGenException("Match and target array have not the same length");
        indices =  new int[match_length];
    }


    /**
   * Since this is a variable length instruction, it may shift the following
   * instructions which then need to update their position.
   *
   * Called by InstructionList.setPositions when setting the position for every
   * instruction. In the presence of variable length instructions `setPositions'
   * performs multiple passes over the instruction list to calculate the
   * correct (byte) positions and offsets by calling this function.
   *
   * @param offset additional offset caused by preceding (variable length) instructions
   * @param max_offset the maximum offset that may be caused by these instructions
   * @return additional offset caused by possible change of this instruction's length
   */
    protected int updatePosition (int offset, int max_offset) {
        position +=  offset; // Additional offset caused by preceding SWITCHs, GOTOs, etc.
        padding =  (4 - ((position + 1)%4))%4; // Alignment on 4-byte-boundary
        length =  (short)(fixed_length + padding); // Update length
        return  padding;
    }


    /**
   * Dump instruction as byte code to stream out.
   * @param out Output stream
   */
    public void dump (DataOutputStream out)
        throws IOException
    {
        out.writeByte(tag);
        for (int i =  0; i < padding; i++) // Padding bytes
        out.writeByte(0);
        index =  getTargetOffset(); // Write default target offset
        out.writeInt(index);
    }


    /**
   * Read needed data (e.g. index) from file.
   */
    protected void initFromFile (ByteSequence bytes, boolean wide)
        throws IOException
    {
        padding =  (4 - (bytes.getIndex()%4))%4; // Compute number of pad bytes

        for (int i =  0; i < padding; i++) {
            byte b;
            if ((b =  bytes.readByte()) != 0) throw  new ClassGenException("Padding byte != 0: " + b);
        }

        // Default branch target common for both cases (TABLESWITCH, LOOKUPSWITCH)
        index =  bytes.readInt();
    }


    /**
   * @return mnemonic for instruction
   */
    public String toString () {
        StringBuffer buf =  new StringBuffer(super.toString());

        for (int i =  0; i < match_length; i++) {
            String s =  "null";
            if (targets[i] != null) s =  targets[i].getInstruction().toString();
            buf.append("(" + match[i] + ", " + s + " = {" + indices[i] + "})");
        }

        return  buf.toString();
    }


    /**
   * @return array of match indices
   */
    public int[] getMatchs () {
        return  match;
    }


    /**
   * @return array of match target offsets
   */
    public int[] getIndices () {
        return  indices;
    }


    /**
   * @return array of match targets
   */
    public InstructionHandle[] getTargets () {
        return  targets;
    }

}

