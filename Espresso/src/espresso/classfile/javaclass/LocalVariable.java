package  espresso.classfile.javaclass;




import  espresso.classfile.Constants;
import  java.io.*;




/**
 * This class represents a local variable within a method. It contains its
 * scope, name, signature and index on the method's frame.
 *
 * @version 970922
 * @author  <A HREF="http://www.inf.fu-berlin.de/~dahm">M. Dahm</A>
 * @see     LocalVariableTable
 */
public final class LocalVariable
    implements Constants
{

    private int start_pc; // Range in which the variable is valid
    private int length;
    private int name_index; // Index in constant pool of variable name
    private int signature_index; // Index of variable signature
    private int slot;
    /* Variable is `slot'th local variable on
				* this method's frame.
				*/
    private ConstantPool constant_pool;


    /**
   * Empty constructor, all attributes have to be defined via `setXXX'
   * methods. Use at your own risk.
   */
    public LocalVariable () {

    }


    /**
   * Initialize from another object. Note that both objects use the same
   * references (shallow copy). Use clone() for a physical copy.
   */
    public LocalVariable (LocalVariable c) {
        this(c.getStartPC(), c.getLength(), c.getNameIndex(), c.getSignatureIndex(), c.getSlot(), c.getConstantPool());
    }


    /**
   * Construct object from file stream.
   * @param file Input stream
   * @throw IOException
   */
    LocalVariable (DataInputStream file, ConstantPool constant_pool)
        throws IOException
    {
        this(file.readUnsignedShort(), file.readUnsignedShort(), file.readUnsignedShort(), file.readUnsignedShort(), file.readUnsignedShort(), constant_pool);
    }


    /**
   * @param start_pc Range in which the variable
   * @param length ... is valid
   * @param name_index Index in constant pool of variable name
   * @param signature_index Index of variable's signature
   * @param slot Variable is `slot'th local variable on the method's frame
   * @param constant_pool Array of constants
   */
    public LocalVariable (int start_pc, int length, int name_index, int signature_index, int slot, ConstantPool constant_pool) {
        this.start_pc =  start_pc;
        this.length =  length;
        this.name_index =  name_index;
        this.signature_index =  signature_index;
        this.slot =  slot;
        this.constant_pool =  constant_pool;
    }


    /**
   * Called by objects that are traversing the nodes of the tree implicitely
   * defined by the contents of a Java class. I.e., the hierarchy of methods,
   * fields, attributes, etc. spawns a tree of objects.
   *
   * @param v Visitor object
   */
    public void accept (Visitor v) {
        v.visitLocalVariable(this);
    }


    /**
   * Dump local variable to file stream in binary format.
   *
   * @param file Output file stream
   * @throw IOException
   */
    public final void dump (DataOutputStream file)
        throws IOException
    {
        file.writeShort(start_pc);
        file.writeShort(length);
        file.writeShort(name_index);
        file.writeShort(signature_index);
        file.writeShort(slot);
    }


    /**
   * @return Constant pool used by this object.
   * @see ConstantPool
   */
    public final ConstantPool getConstantPool () {
        return  constant_pool;
    }


    /**
   * @return Variable is valid within getStartPC() .. getStartPC()+getLength()
   */
    public final int getLength () {
        return  length;
    }


    /**
   * @return Variable name.
   */
    public final String getName () {
        ConstantUtf8 c;
        c =  (ConstantUtf8)constant_pool.getConstant(name_index, CONSTANT_Utf8);
        return  c.getBytes();
    }


    /**
   * @return Index in constant pool of variable name.
   */
    public final int getNameIndex () {
        return  name_index;
    }


    /**
   * @return Signature.
   */
    public final String getSignature () {
        ConstantUtf8 c;
        c =  (ConstantUtf8)constant_pool.getConstant(signature_index, CONSTANT_Utf8);
        return  Utility.signatureToString(c.getBytes());
    }


    /**
   * @return Index in constant pool of variable signature.
   */
    public final int getSignatureIndex () {
        return  signature_index;
    }


    /**
   * @return Variable is `getSlot()'th local variable on this method's frame.
   */
    public final int getSlot () {
        return  slot;
    }


    /**
   * @return Start of range where he variable is valid
   */
    public final int getStartPC () {
        return  start_pc;
    }


    /**
   * @param constant_pool Constant pool to be used for this object.
   * @see ConstantPool
   */
    public final void setConstantPool (ConstantPool constant_pool) {
        this.constant_pool =  constant_pool;
    }


    /**
   * @param length.
   */
    public final void setLength (int length) {
        this.length =  length;
    }


    /**
   * @param name_index.
   */
    public final void setNameIndex (int name_index) {
        this.name_index =  name_index;
    }


    /**
   * @param signature_index.
   */
    public final void setSignatureIndex (int signature_index) {
        this.signature_index =  signature_index;
    }


    /**
   * @param slot.
   */
    public final void setSlot (int slot) {
        this.slot =  slot;
    }


    /**
   * @param start_pc Specify range where the local variable is valid.
   */
    public final void setStartPC (int start_pc) {
        this.start_pc =  start_pc;
    }


    /**
   * @return Resolved string representation.
   */
    public final String toString () {
        String name, signature;
        signature =  getSignature();
        name =  getName();
        return  "LocalVariable(start_pc = " + start_pc + ", length = " + length + ", slot = " + slot + ":" + signature + " " + name + ")";
    }

}

