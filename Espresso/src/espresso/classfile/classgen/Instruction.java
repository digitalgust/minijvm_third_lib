package  espresso.classfile.classgen;




import  espresso.classfile.Constants;
import  espresso.classfile.javaclass.*;
import  java.io.*;
import  espresso.classfile.util.ByteSequence;




/** 
 * This abstract class is the super class for all java byte codes.
 *
 * @version 980202
 * @author  <A HREF="http://www.inf.fu-berlin.de/~dahm">M. Dahm</A>
 */
public abstract class Instruction
    implements Constants, Cloneable
{

    protected int position =  -1; // Index in byte stream.
    protected short length =  1; // Length of instruction in bytes 
    protected short tag =  -1; // Opcode number


    /**
   * Empty constructor needed for the Class.newInstance() statement in
   * Instruction.readInstruction(). Not to be used otherwise.
   */
    Instruction () {

    }


    public Instruction (short tag, short length) {
        this.length =  length;
        this.tag =  tag;
    }


    /**
   * Dump instruction as byte code to stream out.
   * @param out Output stream
   */
    public void dump (DataOutputStream out)
        throws IOException
    {
        out.writeByte(tag); // Common for all instructions
    }


    /**
   * Called by InstructionList.setPositions when setting the position for every
   * instruction. In the presence of variable length instructions `setPositions'
   * performs multiple passes over the instruction list to calculate the
   * correct (byte) positions and offsets by calling this function.
   *
   * @param offset additional offset caused by preceding (variable length) instructions
   * @param max_offset the maximum offset that may be caused by these instructions
   * @return additional offset caused by possible change of this instruction's length
   */
    protected int updatePosition (int offset, int max_offset) {
        position +=  offset;
        return  0;
    }


    /**
   * @return mnemonic for instruction
   */
    public String toString () {
        return  position + " " + OPCODE_NAMES[tag] + "[" + tag + "](" + length + ")";
    }


    /**
   * Use with caution, since `BranchInstruction's have a `target' reference which
   * is not copied correctly (only basic types are). This also applies for 
   * `Select' instructions with their multiple branch targets.
   *
   * Used by BranchHandle.setTarget()
   * @see BranchHandle
   *
   * @return (shallow) copy of an instruction
   */
    Instruction copy () {
        Instruction i =  null;

        try {
            i =  (Instruction)clone();
        } catch (CloneNotSupportedException e) {
            System.err.println(e);
        }

        return  i;
    }


    /**
   * Read needed data (e.g. index) from file.
   */
    protected void initFromFile (ByteSequence bytes, boolean wide)
        throws IOException
    {

    }


    /**
   * Read an instruction from (byte code) input stream and return the
   * appropiate object.
   *
   * @param file file to read from
   * @return instruction object being read
   */
    public final static Instruction readInstruction (ByteSequence bytes)
        throws IOException
    {
        boolean wide =  false;
        int pos =  bytes.getIndex(); // offset in byte stream
        short tag =  (short)bytes.readUnsignedByte();
        Instruction obj =  null;

        if (tag == WIDE) { // Read next tag after wide byte
            wide =  true;
            tag =  (short)bytes.readUnsignedByte();
        }

        String name =  OPCODE_NAMES[tag].toUpperCase();

        /* ICONST_0, etc. will be shortened to ICONST, etc., since ICONST_0, ...
     * are not implemented (directly).
     */
        try {
            int len =  name.length();
            char ch1 =  name.charAt(len - 2), ch2 =  name.charAt(len - 1);
            if ((ch1 == '_') && (ch2 >= '0') && (ch2 <= '5')) name =  name.substring(0, len - 2);
            if (name.equals("ICONST_M1")) name =  "ICONST";
        } catch (StringIndexOutOfBoundsException e) {
            System.err.println(e);
        }

        String class_name =  "espresso.classfile.classgen." + name;

        /* Find appropiate class, instantiate an (empty) instruction object
     * and initialize it by hand.
     */
        try {
            Class clazz =  Class.forName(class_name);
            obj =  (Instruction)clazz.newInstance();
            obj.setTag(tag);
            obj.setPosition(pos);
            obj.initFromFile(bytes, wide); // Do further initializations, if any
        } catch (Exception e) {
            throw  new ClassGenException(e.toString());
        }

        return  obj;
    }


    /**
   * @return Number of words consumed from stack by this instruction
   */
    public int consumeStack () {
        return  CONSUME_STACK[tag];
    }


    /**
   * @return Number of words produced onto stack by this instruction
   */
    public int produceStack () {
        return  PRODUCE_STACK[tag];
    }


    /**
   * @return opcode number
   */
    public short getTag () {
        return  tag;
    }


    /**
   * @return length (in bytes) of instruction
   */
    public int getLength () {
        return  length;
    }


    /**
   * @return (absolute) position in byte code of this instruction in the current
   * method.
   */
    public int getPosition () {
        return  position;
    }


    /**
   * Update position of instruction
   */
    public void setPosition (int position) {
        this.position =  position;
    }


    /**
   * Needed in readInstruction.
   */
    private void setTag (short tag) {
        this.tag =  tag;
    }

}

