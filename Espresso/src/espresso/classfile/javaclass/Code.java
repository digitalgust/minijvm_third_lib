package  espresso.classfile.javaclass;




import  espresso.classfile.Constants;
import  java.io.*;




/** 
 * This class is derived from <em>Attribute</em> and represents a
 * code chunk. It is instantiated by the
 * <em>Attribute.readAttribute()</em> method. A <em>Code</em>
 * attribute contains informations about operand stack, local variables,
 * byte code and the exceptions handled within this method.
 *
 * This attribute has attributes itself, namely <em>LineNumberTable</em> which
 * is used for debugging purposes and <em>LocalVariableTable</em> which 
 * contains information about the local variables.
 *
 * @version 970922
 * @author  <A HREF="http://www.inf.fu-berlin.de/~dahm">M. Dahm</A>
 * @see     Attribute
 * @see     CodeException
 * @see     LineNumberTable
 * @see     LocalVariableTable 
*/
public final class Code
    extends Attribute
{

    private int max_stack; // Maximum size of stack used by this method
    private int max_locals; // Number of local variables
    private int code_length; // Length of code in bytes
    private byte[] code; // Actual byte code
    private int exception_table_length;
    private CodeException[] exception_table; // Table of handled exceptions
    private int attributes_count; // Attributes of code: LineNumber
    private Attribute[] attributes; // or LocalVariable
    private ConstantPool constant_pool;


    /**
   * Empty constructor, all attributes have to be defined via `setXXX'
   * methods. Use at your own risk.
   */
    public Code () {
        tag =  ATTR_CODE;
    }


    /**
   * Initialize from another object. Note that both objects use the same
   * references (shallow copy). Use clone() for a physical copy.
   */
    public Code (Code c) {
        this(c.getNameIndex(), c.getLength(), c.getMaxStack(), c.getMaxLocals(), c.getCode(), c.getExceptionTable(), c.getAttributes(), c.getConstantPool());
    }


    /**
   * @param name_index Index pointing to the name <em>Code</em>
   * @param length Content length in bytes
   * @param file Input stream
   * @param constant_pool Array of constants
   */
    Code (int name_index, int length, DataInputStream file, ConstantPool constant_pool)
        throws IOException
    {
        // Initialize with some default values which will be overwritten later
        this(name_index, length, file.readUnsignedShort(), file.readUnsignedShort(), (byte[])null, (CodeException[])null, (Attribute[])null, constant_pool);
        code_length =  file.readInt();
        code =  new byte[code_length]; // Read byte code
        file.readFully(code);
        /* Read exception table that contains all regions where an exception
     * handler is active, i.e. a try { ... } catch() block.
     */
        exception_table_length =  file.readUnsignedShort();
        exception_table =  new CodeException[exception_table_length];
        for (int i =  0; i < exception_table_length; i++) exception_table[i] =  new CodeException(file);
        /* Read all attributes, currently `LineNumberTable' and
     * `LocalVariableTable'
     */
        attributes_count =  file.readUnsignedShort();
        attributes =  new Attribute[attributes_count];
        for (int i =  0; i < attributes_count; i++) attributes[i] =  Attribute.readAttribute(file, constant_pool);
    }


    /**
   * @param name_index Index pointing to the name <em>Code</em>
   * @param length Content length in bytes
   * @param max_stack Maximum size of stack
   * @param max_locals Number of local variables
   * @param code Actual byte code
   * @param exception_table Table of handled exceptions
   * @param attributes Attributes of code: LineNumber or LocalVariable
   * @param constant_pool Array of constants
   */
    public Code (int name_index, int length, int max_stack, int max_locals, byte[] code, CodeException[] exception_table, Attribute[] attributes, ConstantPool constant_pool) {
        this();
        this.name_index =  name_index;
        this.length =  length;
        this.max_stack =  max_stack;
        this.max_locals =  max_locals;
        this.constant_pool =  constant_pool;
        setCode(code);
        setExceptionTable(exception_table);
        setAttributes(attributes);
    }


    /**
   * Called by objects that are traversing the nodes of the tree implicitely
   * defined by the contents of a Java class. I.e., the hierarchy of methods,
   * fields, attributes, etc. spawns a tree of objects.
   *
   * @param v Visitor object
   */
    public void accept (Visitor v) {
        v.visitCode(this);
    }


    /**
   * Dump code attribute to file stream in binary format.
   *
   * @param file Output file stream
   * @throw IOException
   */
    public final void dump (DataOutputStream file)
        throws IOException
    {
        super.dump(file);
        file.writeShort(max_stack);
        file.writeShort(max_locals);
        file.writeInt(code_length);
        file.write(code, 0, code_length);
        file.writeShort(exception_table_length);
        for (int i =  0; i < exception_table_length; i++) exception_table[i].dump(file);
        file.writeShort(attributes_count);
        for (int i =  0; i < attributes_count; i++) attributes[i].dump(file);
    }


    /**
   * @return Collection of code attributes.
   * @see Attribute
   */
    public final Attribute[] getAttributes () {
        return  attributes;
    }


    /**
   * @return Actual byte code of the method.
   */
    public final byte[] getCode () {
        return  code;
    }


    /**
   * @return Constant pool used by this object.
   * @see ConstantPool
   */
    public final ConstantPool getConstantPool () {
        return  constant_pool;
    }


    /**
   * @return Table of handled exceptions.
   * @see CodeException
   */
    public final CodeException[] getExceptionTable () {
        return  exception_table;
    }


    /**
   * @return Number of local variables.
   */
    public final int getMaxLocals () {
        return  max_locals;
    }


    /**
   * @return Maximum size of stack used by this method.
   */
    public final int getMaxStack () {
        return  max_stack;
    }


    /**
   * @param attributes.
   */
    public final void setAttributes (Attribute[] attributes) {
        this.attributes =  attributes;
        attributes_count =  (attributes == null)? 0 : attributes.length;
    }


    /**
   * @param code byte code
   */
    public final void setCode (byte[] code) {
        this.code =  code;
        code_length =  (code == null)? 0 : code.length;
    }


    /**
   * @param constant_pool Constant pool to be used for this object.
   * @see ConstantPool
   */
    public final void setConstantPool (ConstantPool constant_pool) {
        this.constant_pool =  constant_pool;
    }


    /**
   * @param exception_table exception table
   */
    public final void setExceptionTable (CodeException[] exception_table) {
        this.exception_table =  exception_table;
        exception_table_length =  (exception_table == null)? 0 : exception_table.length;
    }


    /**
   * @param max_locals maximum number of local variables
   */
    public final void setMaxLocals (int max_locals) {
        this.max_locals =  max_locals;
    }


    /**
   * @param max_stack maximum stack size
   */
    public final void setMaxStack (int max_stack) {
        this.max_stack =  max_stack;
    }


    /**
   * @return String representation of code chunk.
   */
    public final String toString () {
        StringBuffer buf;
        buf =  new StringBuffer("Code(max_stack = " + max_stack + ", max_locals = " + max_locals + ", code_length = " + code_length + ", code = \n" + Utility.codeToString(code, constant_pool, 0, -1) + '\n');

        if (exception_table_length > 0) {
            buf.append(", exception_table = \n");
            for (int i =  0; i < exception_table_length; i++) buf.append(exception_table[i].toString() + "\n");
        }

        if (attributes_count > 0) {
            buf.append(", " + attributes_count + " attributes = \n");
            for (int i =  0; i < attributes_count; i++) buf.append(attributes[i].toString() + "\n");
        }

        buf.append(")");
        return  buf.toString();
    }

}

