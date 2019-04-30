package  espresso.classfile.classgen;




import  java.io.*;
import  espresso.classfile.util.ByteSequence;




/**
 * IINC - Increment local variable by constant
 *
 * @version 971120
 * @author  <A HREF="http://www.inf.fu-berlin.de/~dahm">M. Dahm</A>
 */
public class IINC
    extends Instruction
{

    private boolean wide;
    private int n, c;


    /**
   * Empty constructor needed for the Class.newInstance() statement in
   * Instruction.readInstruction(). Not to be used otherwise.
   */
    IINC () {

    }


    public IINC (int n, int c) {
        super(IINC, (short)3);
        if (wide =  ((n > MAX_SHORT) || (Math.abs(c) > Byte.MAX_VALUE))) length =  6; // wide byte included
        this.n =  n;
        this.c =  c;
    }


    /**
   * Dump instruction as byte code to stream out.
   * @param out Output stream
   */
    public void dump (DataOutputStream out)
        throws IOException
    {
        if (wide) // Need WIDE prefix ?
        out.writeByte(WIDE);
        out.writeByte(tag);

        if (wide) {
            out.writeShort(n);
            out.writeShort(c);
        } else {
            out.writeByte(n);
            out.writeByte(c);
        }
    }


    /**
   * Read needed data (e.g. index) from file.
   */
    protected void initFromFile (ByteSequence bytes, boolean wide)
        throws IOException
    {
        if (wide) {
            length =  6;
            n =  bytes.readUnsignedShort();
            c =  bytes.readShort();
        } else {
            length =  3;
            n =  bytes.readUnsignedByte();
            c =  bytes.readByte();
        }
    }


    /**
   * @return mnemonic for instruction
   */
    public String toString () {
        return  super.toString() + " " + n + ":" + c;
    }

}

