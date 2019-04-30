package  espresso.classfile.classgen;




import  java.io.*;
import  espresso.classfile.util.ByteSequence;




/** 
 * Abstract super class for instructions dealing with local variables.
 *
 * @version 980204
 * @author  <A HREF="http://www.inf.fu-berlin.de/~dahm">M. Dahm</A>
 */
public abstract class LocalVariableInstruction
    extends Instruction
{

    private int n;
    private boolean wide;
    private short c_tag;


    /**
   * Empty constructor needed for the Class.newInstance() statement in
   * Instruction.readInstruction(). Not to be used otherwise.
   */
    LocalVariableInstruction () {

    }


    /**
   * @param tag Instruction number
   * @param c_tag Instruction number for compact version, ALOAD_0, e.g.
   * @param n local variable index (unsigned short)
   */
    protected LocalVariableInstruction (short tag, short c_tag, int n) {
        super(tag, (short)2);
        this.c_tag =  c_tag;
        setIndex(n);
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

        if (length > 1) { // Otherwise ILOAD_n, instruction, e.g.
            if (wide) out.writeShort(n); else out.writeByte(n);
        }
    }


    /**
   * @return mnemonic for instruction
   */
    public String toString () {
        return  super.toString() + "<" + n + ">";
    }


    /**
   * Read needed data (e.g. index) from file.
   */
    protected void initFromFile (ByteSequence bytes, boolean wide)
        throws IOException
    {
        if (wide) {
            n =  bytes.readUnsignedShort();
            this.wide =  true;
            length =  4;
        } else if (((tag >= ILOAD) && (tag <= ALOAD)) || ((tag >= ISTORE) && (tag <= ASTORE))) {
            n =  bytes.readUnsignedByte();
            length =  2;
        } else { // compact instruction such as ILOAD_2, ASTORE_0
            n =  (tag - ILOAD_0)%4; // Nice trick ... see Constants.java
            length =  1;
        }
    }


    /**
   * @return local variable index  referred by this instruction.
   */
    public final int getIndex () {
        return  n;
    }


    /**
   * Set the local variable index
   */
    public final void setIndex (int n) {
        if ((n < 0) || (n > MAX_SHORT)) throw  new ClassGenException("Illegal value: " + n);

        if (n >= 0 && n <= 3) { // Use more compact instruction xLOAD_n
            this.tag =  (short)(c_tag + n);
            length =  1;
        }

        if (wide =  n > MAX_BYTE) // Need WIDE prefix ?
        length =  4;
        this.n =  n;
    }

}

