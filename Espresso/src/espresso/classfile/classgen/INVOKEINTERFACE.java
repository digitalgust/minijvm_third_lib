package  espresso.classfile.classgen;




import  java.io.*;
import  espresso.classfile.util.ByteSequence;




/** 
 * INVOKEINTERFACE - Invoke interface method
 * Stack: ..., objectref, [arg1, [arg2 ...]] -> ...
 *
 * @version 971124
 * @author  <A HREF="http://www.inf.fu-berlin.de/~dahm">M. Dahm</A>
 */
public final class INVOKEINTERFACE
    extends CPInstruction
{

    private int nargs; // Number of arguments on stack


    /**
   * Empty constructor needed for the Class.newInstance() statement in
   * Instruction.readInstruction(). Not to be used otherwise.
   */
    INVOKEINTERFACE () {

    }


    public INVOKEINTERFACE (int index, int nargs) {
        super(INVOKEINTERFACE, index);
        length =  5;
        if (nargs < 1) throw  new ClassGenException("Number of arguments must be > 0 " + nargs);
        this.nargs =  nargs;
    }


    /**
   * Dump instruction as byte code to stream out.
   * @param out Output stream
   */
    public void dump (DataOutputStream out)
        throws IOException
    {
        out.writeByte(tag);
        out.writeShort(index);
        out.writeByte(nargs);
        out.writeByte(0);
    }


    /**
   * Read needed data (i.e. index) from file.
   */
    protected void initFromFile (ByteSequence bytes, boolean wide)
        throws IOException
    {
        super.initFromFile(bytes, wide);
        length =  5;
        nargs =  bytes.readUnsignedByte();
        bytes.readByte(); // Skip 0 byte
    }

}

