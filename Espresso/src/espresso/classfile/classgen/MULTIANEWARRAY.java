package  espresso.classfile.classgen;




import  java.io.*;




/** 
 * MULTIANEWARRAY - Create new mutidimensional array of references
 * Stack: ..., count1, [count2, ...] -> ..., arrayref
 *
 * @version 971204
 * @author  <A HREF="http://www.inf.fu-berlin.de/~dahm">M. Dahm</A>
 */
public class MULTIANEWARRAY
    extends CPInstruction
{

    /**
   * Empty constructor needed for the Class.newInstance() statement in
   * Instruction.readInstruction(). Not to be used otherwise.
   */
    MULTIANEWARRAY () {

    }


    private short dimensions;


    public MULTIANEWARRAY (int index, short dimensions) {
        super(MULTIANEWARRAY, index);
        if (dimensions < 1) throw  new ClassGenException("Invalid dimensions value: " + dimensions);
        this.dimensions =  dimensions;
        length =  4;
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
        out.writeByte(dimensions);
    }


    /**
   * @return mnemonic for instruction
   */
    public String toString () {
        return  super.toString() + " " + index + " " + dimensions;
    }

}

