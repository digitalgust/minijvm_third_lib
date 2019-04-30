package  espresso.classfile.classgen;




import  java.io.*;
import  espresso.classfile.util.ByteSequence;




/** 
 * TABLESWITCH - Switch within given range of values, i.e. low..high
 *
 * @version 980112
 * @author  <A HREF="http://www.inf.fu-berlin.de/~dahm">M. Dahm</A>
 */
public class TABLESWITCH
    extends Select
{

    /**
   * Empty constructor needed for the Class.newInstance() statement in
   * Instruction.readInstruction(). Not to be used otherwise.
   */
    TABLESWITCH () {

    }


    public TABLESWITCH (int[] match, InstructionHandle[] targets, InstructionHandle target) {
        super(TABLESWITCH, match, targets, target);
        length =  (short)(13 + match_length*4);
        /* Alignment remainder assumed
					      * 0 here, until dump time */
        fixed_length =  length;
    }


    /**
   * Dump instruction as byte code to stream out.
   * @param out Output stream
   */
    public void dump (DataOutputStream out)
        throws IOException
    {
        super.dump(out);
        out.writeInt(match[0]); // low
        out.writeInt(match[match_length - 1]); // high
        for (int i =  0; i < match_length; i++) // jump offsets
        out.writeInt(indices[i] =  getTargetOffset(targets[i]));
    }


    /**
   * Read needed data (e.g. index) from file.
   */
    protected void initFromFile (ByteSequence bytes, boolean wide)
        throws IOException
    {
        super.initFromFile(bytes, wide);
        int low =  bytes.readInt();
        int high =  bytes.readInt();
        match_length =  high - low + 1;
        length =  (short)(13 + match_length*4);
        fixed_length =  length;
        match =  new int[match_length];
        indices =  new int[match_length];
        targets =  new InstructionHandle[match_length];
        for (int i =  low; i <= high; i++) match[i - low] =  i;

        for (int i =  0; i < match_length; i++) {
            indices[i] =  bytes.readInt();
        }
    }

}

