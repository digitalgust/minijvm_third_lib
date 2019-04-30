package  espresso.classfile.classgen;




import  java.io.*;




/** 
 * GOTO - Branch always (offset, not address)
 *
 * @version 971203
 * @author  <A HREF="http://www.inf.fu-berlin.de/~dahm">M. Dahm</A>
 */
public class GOTO
    extends BranchInstruction
    implements VariableLengthInstruction
{

    /**
   * Empty constructor needed for the Class.newInstance() statement in
   * Instruction.readInstruction(). Not to be used otherwise.
   */
    GOTO () {

    }


    public GOTO (InstructionHandle target) {
        super(GOTO, target);
    }


    /**
   * Dump instruction as byte code to stream out.
   * @param out Output stream
   */
    public void dump (DataOutputStream out)
        throws IOException
    {
        index =  getTargetOffset();

        if (tag == GOTO) super.dump(out); else { // GOTO_W
            index =  getTargetOffset();
            out.writeByte(tag);
            out.writeInt(index);
        }
    }


    /** Called in pass 2 of InstructionList.setPositions() in order to update
   * the branch target, that may shift due to variable length instructions.
   */
    protected int updatePosition (int offset, int max_offset) {
        int i =  getTargetOffset(); // Depending on old position value
        position +=  offset; // Position may be shifted by preceding expansions

        if (Math.abs(i) >= (32767 - max_offset)) { // to large for short (estimate)
            tag =  GOTO_W;
            length =  5;
            return  2; // 5 - 3
        }

        return  0;
    }

}

