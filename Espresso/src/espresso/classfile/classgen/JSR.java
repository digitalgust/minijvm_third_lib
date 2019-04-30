package  espresso.classfile.classgen;




import  java.io.*;




/** 
 * JSR - Jump to subroutine
 *
 * @version 971203
 * @author  <A HREF="http://www.inf.fu-berlin.de/~dahm">M. Dahm</A>
 */
public class JSR
    extends BranchInstruction
    implements VariableLengthInstruction
{

    /**
   * Empty constructor needed for the Class.newInstance() statement in
   * Instruction.readInstruction(). Not to be used otherwise.
   */
    JSR () {

    }


    public JSR (InstructionHandle target) {
        super(JSR, target);
    }


    /**
   * Dump instruction as byte code to stream out.
   * @param out Output stream
   */
    public void dump (DataOutputStream out)
        throws IOException
    {
        index =  getTargetOffset();

        if (tag == JSR) super.dump(out); else { // JSR_W
            index =  getTargetOffset();
            out.writeByte(tag);
            out.writeInt(index);
        }
    }


    protected int updatePosition (int offset, int max_offset) {
        int i =  getTargetOffset(); // Depending on old position value
        position +=  offset; // Position may be shifted by preceding expansions

        if (Math.abs(i) >= (32767 - max_offset)) { // to large for short (estimate)
            tag =  JSR_W;
            length =  5;
            return  2; // 5 - 3
        }

        return  0;
    }

}

