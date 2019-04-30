package  espresso.classfile.classgen;




import  java.io.*;
import  espresso.classfile.util.ByteSequence;




/** 
 * NEWARRAY -  Create new array of basic type (int, short, ...)
 * Stack: ..., type -> ..., arrayref
 * type mus be one of T_INT, T_SHORT, ...
 * 
 * @version 971205
 * @author  <A HREF="http://www.inf.fu-berlin.de/~dahm">M. Dahm</A>
 */
public class NEWARRAY
    extends Instruction
{

    private byte type;


    /**
   * Empty constructor needed for the Class.newInstance() statement in
   * Instruction.readInstruction(). Not to be used otherwise.
   */
    NEWARRAY () {

    }


    public NEWARRAY (byte type) {
        super(NEWARRAY, (short)2);
        this.type =  type;
    }


    /**
   * Dump instruction as byte code to stream out.
   * @param out Output stream
   */
    public void dump (DataOutputStream out)
        throws IOException
    {
        out.writeByte(tag);
        out.writeByte(type);
    }


    /**
   * @return mnemonic for instruction
   */
    public String toString () {
        return  super.toString() + " " + TYPE_NAMES[type];
    }


    /**
   * Read needed data (e.g. index) from file.
   */
    protected void initFromFile (ByteSequence bytes, boolean wide)
        throws IOException
    {
        type =  bytes.readByte();
    }

}

