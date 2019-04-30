package  espresso.classfile.classgen;




import  java.io.*;
import  espresso.classfile.util.ByteSequence;




/** 
 * LOOKUPSWITCH - Switch with unordered set of values
 *
 * @version 980112
 * @author  <A HREF="http://www.inf.fu-berlin.de/~dahm">M. Dahm</A>
 */
public class LOOKUPSWITCH
    extends Select
{

    /**
   * Empty constructor needed for the Class.newInstance() statement in
   * Instruction.readInstruction(). Not to be used otherwise.
   */
    LOOKUPSWITCH () {

    }


    public LOOKUPSWITCH (int[] match, InstructionHandle[] targets, InstructionHandle target) {
        super(LOOKUPSWITCH, match, targets, target);
        length =  (short)(9 + match_length*8);
        /* alignment remainder assumed
					     * 0 here, until dump time. */
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
        out.writeInt(match_length); // npairs

        for (int i =  0; i < match_length; i++) {
            out.writeInt(match[i]); // match-offset pairs
            out.writeInt(indices[i] =  getTargetOffset(targets[i]));
        }
    }


    /**
   * Read needed data (e.g. index) from file.
   */
    protected void initFromFile (ByteSequence bytes, boolean wide)
        throws IOException
    {
        super.initFromFile(bytes, wide);
        match_length =  bytes.readInt();
        length =  (short)(9 + match_length*8);
        fixed_length =  length;
        match =  new int[match_length];
        indices =  new int[match_length];
        targets =  new InstructionHandle[match_length];

        for (int i =  0; i < match_length; i++) {
            match[i] =  bytes.readInt();
            indices[i] =  bytes.readInt();
        }
    }

}

