package  espresso.classfile.classgen;




import  java.io.*;
import  espresso.classfile.util.ByteSequence;




/** 
 * GOTO_W - Branch always (offset, not address)
 *
 * @version 971203
 * @author  <A HREF="http://www.inf.fu-berlin.de/~dahm">M. Dahm</A>
 */
public class GOTO_W
    extends BranchInstruction
{

    /**
   * Empty constructor needed for the Class.newInstance() statement in
   * Instruction.readInstruction(). Not to be used otherwise.
   */
    GOTO_W () {

    }


    public GOTO_W (InstructionHandle target) {
        super(GOTO_W, target);
        length =  5;
    }


    /**
   * Dump instruction as byte code to stream out.
   * @param out Output stream
   */
    public void dump (DataOutputStream out)
        throws IOException
    {
        index =  getTargetOffset();
        out.writeByte(tag);
        out.writeInt(index);
    }


    /**
   * Read needed data (e.g. index) from file.
   */
    protected void initFromFile (ByteSequence bytes, boolean wide)
        throws IOException
    {
        index =  bytes.readInt();
    }

}

