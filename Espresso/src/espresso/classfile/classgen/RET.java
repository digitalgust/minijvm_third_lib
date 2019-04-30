package  espresso.classfile.classgen;




import  java.io.*;
import  espresso.classfile.util.ByteSequence;




/** 
 * RET - Return from subroutine
 *
 * Stack: ..., -> ..., address
 *
 * @version 980121
 * @author  <A HREF="http://www.inf.fu-berlin.de/~dahm">M. Dahm</A>
 */
public class RET
    extends Instruction
{

    private boolean wide;


    /**
   * Empty constructor needed for the Class.newInstance() statement in
   * Instruction.readInstruction(). Not to be used otherwise.
   */
    RET () {

    }


    private int index; // index to local variable containg the return address


    public RET (int index) {
        super(RET, (short)2);
        this.index =  index;
        if (wide =  index > MAX_BYTE) length =  4; // Including the wide byte
    }


    /**
   * Dump instruction as byte code to stream out.
   * @param out Output stream
   */
    public void dump (DataOutputStream out)
        throws IOException
    {
        if (wide) out.writeByte(WIDE);
        out.writeByte(tag);
        if (wide) out.writeShort(index); else out.writeByte(index);
    }


    /**
   * Read needed data (e.g. index) from file.
   */
    protected void initFromFile (ByteSequence bytes, boolean wide)
        throws IOException
    {
        if (wide) {
            index =  bytes.readUnsignedShort();
            length =  4;
        } else {
            index =  bytes.readUnsignedByte();
            length =  2;
        }
    }

}

