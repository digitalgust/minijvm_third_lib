package  espresso.classfile.classgen;




import  java.io.*;
import  espresso.classfile.util.ByteSequence;




/**
 * SIPUSH - Push short
 *
 * Stack: ... -> ..., value
 *
 * @version 971119
 * @author  <A HREF="http://www.inf.fu-berlin.de/~dahm">M. Dahm</A>
 */
public class SIPUSH
    extends Instruction
{

    private short b;


    /**
   * Empty constructor needed for the Class.newInstance() statement in
   * Instruction.readInstruction(). Not to be used otherwise.
   */
    SIPUSH () {

    }


    public SIPUSH (short b) {
        super(SIPUSH, (short)3);
        this.b =  b;
    }


    /**
   * Dump instruction as short code to stream out.
   */
    public void dump (DataOutputStream out)
        throws IOException
    {
        super.dump(out);
        out.writeShort(b);
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
        length =  3;
        b =  bytes.readShort();
    }

}

