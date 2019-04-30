package  espresso.classfile.classgen;




import  java.io.*;
import  espresso.classfile.util.ByteSequence;




/** 
 * BIPUSH - Push byte
 *
 * Stack: ... -> ..., value
 *
 * @version 971119
 * @author  <A HREF="http://www.inf.fu-berlin.de/~dahm">M. Dahm</A>
 */
public class BIPUSH
    extends Instruction
{

    private byte b;


    /**
   * Empty constructor needed for the Class.newInstance() statement in
   * Instruction.readInstruction(). Not to be used otherwise.
   */
    BIPUSH () {

    }


    public BIPUSH (byte b) {
        super(BIPUSH, (short)2);
        this.b =  b;
    }


    /**
   * Dump instruction as byte code to stream out.
   */
    public void dump (DataOutputStream out)
        throws IOException
    {
        super.dump(out);
        out.writeByte(b);
    }


    /**
   * @return mnemonic for instruction
   */
    public String toString () {
        return  super.toString() + " " + b;
    }


    /**
   * Read needed data (e.g. index) from file.
   */
    protected void initFromFile (ByteSequence bytes, boolean wide)
        throws IOException
    {
        length =  2;
        b =  bytes.readByte();
    }

}

