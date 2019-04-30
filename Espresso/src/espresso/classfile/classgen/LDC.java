package  espresso.classfile.classgen;




import  java.io.*;
import  espresso.classfile.util.ByteSequence;




/** 
 * LDC - Push item from constant pool
 *
 * Stack: ... -> ..., item.word1, item.word2
 *
 * @version 971120
 * @author  <A HREF="http://www.inf.fu-berlin.de/~dahm">M. Dahm</A>
 */
public class LDC
    extends CPInstruction
{

    /**
   * Empty constructor needed for the Class.newInstance() statement in
   * Instruction.readInstruction(). Not to be used otherwise.
   */
    LDC () {

    }


    public LDC (int index) {
        super(LDC_W, index);
        if (index <= MAX_BYTE) { // Fits in one byte
            tag =  LDC;
            length =  2;
        }
    }


    /**
   * Dump instruction as byte code to stream out.
   * @param out Output stream
   */
    public void dump (DataOutputStream out)
        throws IOException
    {
        out.writeByte(tag);
        if (length == 2) out.writeByte(index); else // Applies for LDC_W
         out.writeShort(index);
    }


    /**
   * Read needed data (e.g. index) from file.
   */
    protected void initFromFile (ByteSequence bytes, boolean wide)
        throws IOException
    {
        length =  2;
        index =  bytes.readUnsignedByte();
    }

}

