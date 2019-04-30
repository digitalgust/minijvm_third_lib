package  espresso.classfile.classgen;




import  espresso.classfile.Constants;
import  espresso.classfile.javaclass.*;




/** 
 * This class represents a local variable within a method. It contains its
 * scope, name and type. The generated LocalVariable object can be obtained
 * with getLocalVariable which needs the instruction list and the constant
 * pool as parameters.
 *
 * @version 980115
 * @author  <A HREF="http://www.inf.fu-berlin.de/~dahm">M. Dahm</A>
 * @see     LocalVariable
 * @see     Instruction
 * @see     MethodGen
 */
public final class LocalVariableGen
    implements Constants
{

    private int slot;
    private String name;
    private ClassGenType type;
    private InstructionHandle start, end;
    private ConstantPoolGen cp;


    /**
   * Generate a local variable that with index `slot'. Note that double and long
   * variables need two slots. Slot indices have to be provided by the user.
   *
   * @param slot index of local variable
   * @param name its name
   * @param type its type
   * @param start from where the instruction is valid (null means from the start)
   * @param end until where the instruction is valid (null means to the end)
   */
    public LocalVariableGen (int slot, String name, ClassGenType type, InstructionHandle start, InstructionHandle end) {
        if ((slot < 0) || (slot > MAX_SHORT)) throw  new ClassGenException("Invalid slot index: " + slot);
        this.name =  name;
        this.type =  type;
        this.slot =  slot;
        this.start =  start;
        this.end =  end;
    }


    /**
   * Get LocalVariable object. If `start' is null it is set to the start of the method,
   * accordingly `end' points to the end of the instruction list if it was null.
   *
   * This relies on that the instruction list has already been dumped to byte code or
   * or that the `setPositions' methods has been called for the instruction list.
   *
   * @param il instruction list (byte code) which this variable belongs to
   * @param cp constant pool
   */
    public LocalVariable getLocalVariable (InstructionList il, ConstantPoolGen cp) {
        start =  (start == null)? il.getStart() : start; // Default scope from start
        end =  (end == null)? il.getEnd() : end; // to end
        int start_pc =  start.getInstruction().getPosition();
        int length =  end.getInstruction().getPosition() - start_pc; // TODO ?
        int name_index =  cp.addUtf8(name);
        int signature_index =  cp.addUtf8(type.getSignature());
        return  new LocalVariable(start_pc, length, name_index, signature_index, slot, cp.getConstantPool());
    }


    public void setSlot (int slot) {
        this.slot =  slot;
    }


    public int getSlot () {
        return  slot;
    }


    public void setName (String name) {
        this.name =  name;
    }


    public String getName () {
        return  name;
    }


    public void setType (ClassGenType type) {
        this.type =  type;
    }


    public ClassGenType getType () {
        return  type;
    }


    public void setStart (InstructionHandle start) {
        this.start =  start;
    }


    public void setEnd (InstructionHandle end) {
        this.end =  end;
    }


    public InstructionHandle getStart () {
        return  start;
    }


    public InstructionHandle getEnd () {
        return  end;
    }

}

