package  espresso.classfile.classgen;




import  java.io.*;
import  espresso.classfile.util.ByteSequence;




/** 
 * Abstract super class for instructions that use an index into the 
 * constant pool such as LDC, INVOKEVIRTUAL, etc.
 *
 * @see ConstantPoolGen
 * @see LDC
 * @see INVOKEVIRTUAL
 *
 * @version 980202
 * @author  <A HREF="http://www.inf.fu-berlin.de/~dahm">M. Dahm</A>
 */
public abstract class CPInstruction
    extends Instruction
{

    protected int index; // index to constant pool


    /**
   * Empty constructor needed for the Class.newInstance() statement in
   * Instruction.readInstruction(). Not to be used otherwise.
   */
    CPInstruction () {

    }


    /**
   * @param index to constant pool
   */
    protected CPInstruction (short tag, int index) {
        super(tag, (short)3);
        setIndex(index);
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
    }


    /**
   * @return mnemonic for instruction
   */
    public String toString () {
        return  super.toString() + "<" + index + ">";
    }


    /**
   * Read needed data (i.e. index) from file.
   */
    protected void initFromFile (ByteSequence bytes, boolean wide)
        throws IOException
    {
        index =  bytes.readUnsignedShort();
        if (index < 0) throw  new ClassGenException("Negative index value: " + index);
        length =  3;
    }


    /**
   * @return index in constant pool referred by this instruction.
   */
    public final int getIndex () {
        return  index;
    }


    /**
   * Set the index to constant pool.
   */
    public final void setIndex (int index) {
        if (index < 0) throw  new ClassGenException("Negative index value: " + index);
        this.index =  index;
    }

}

