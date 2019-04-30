package  espresso.classfile.classgen;




import  java.io.*;




/** 
 * Wrapper class for push operations, which are implemented either as BIPUSH,
 * LDC or xCONST_n instructions.
 *
 * @version 971208
 * @author  <A HREF="http://www.inf.fu-berlin.de/~dahm">M. Dahm</A>
 */
public final class PUSH
    implements CompoundInstruction, VariableLengthInstruction
{

    private Instruction instruction;


    /**
   * This constructor also applies for values of type short, char, byte (and boolean).
   *
   * @param cp Constant pool
   * @param value to be pushed 
   */
    public PUSH (ConstantPoolGen cp, int value) {
        if ((value >= -1) && (value <= 5)) // Use ICONST_n
        instruction =  new ICONST(value); else if ((value >= -128) && (value <= 127)) // Use BIPUSH
        instruction =  new BIPUSH((byte)value); else if ((value >= -32768) && (value <= 32767)) // Use SIPUSH
        instruction =  new SIPUSH((short)value); else // If everything fails create a Constant pool entry
         instruction =  new LDC(cp.addInteger(value));
    }


    /**
   * @param cp Constant pool
   * @param value to be pushed 
   */
    public PUSH (ConstantPoolGen cp, float value) {
        if ((value == 0.0) || (value == 1.0) || (value == 2.0)) instruction =  new FCONST(value); else // Create a Constant pool entry
         instruction =  new LDC(cp.addFloat(value));
    }


    /**
   * @param cp Constant pool
   * @param value to be pushed 
   */
    public PUSH (ConstantPoolGen cp, long value) {
        if ((value == 0) || (value == 1)) instruction =  new LCONST(value); else // Create a Constant pool entry
         instruction =  new LDC2_W(cp.addLong(value));
    }


    /**
   * @param cp Constant pool
   * @param value to be pushed 
   */
    public PUSH (ConstantPoolGen cp, double value) {
        if ((value == 0.0) || (value == 1.0)) instruction =  new DCONST(value); else // Create a Constant pool entry
         instruction =  new LDC2_W(cp.addDouble(value));
    }


    /**
   * @param cp Constant pool
   * @param value to be pushed 
   */
    public PUSH (ConstantPoolGen cp, String value) {
        if (value == null) instruction =  new ACONST_NULL(); else 
        // Create a Constant pool entry
        instruction =  new LDC(cp.addString(value));
    }


    public final InstructionList getInstructionList () {
        return  new InstructionList(instruction);
    }


    public final Instruction getInstruction () {
        return  instruction;
    }


    /**
   * @return mnemonic for instruction
   */
    public String toString () {
        return  instruction.toString() + " (PUSH)";
    }

}

