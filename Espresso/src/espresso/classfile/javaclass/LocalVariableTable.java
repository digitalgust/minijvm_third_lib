package  espresso.classfile.javaclass;




import  espresso.classfile.Constants;
import  java.io.*;




/**
 * This class is derived from <em>Attribute</em> and represents colection of local 
 * variables in a method. This attribute is used by the <em>Code</em> attribute.
 *
 * @version 970922
 * @author  <A HREF="http://www.inf.fu-berlin.de/~dahm">M. Dahm</A>
 * @see     Code
 * @see     LocalVariable
 */
public class LocalVariableTable
    extends Attribute
{

    private int local_variable_table_length; // Table of local
    private LocalVariable[] local_variable_table; // variables
    private ConstantPool constant_pool;


    /**
   * Empty constructor, all attributes have to be defined via `setXXX'
   * methods. Use at your own risk.
   */
    public LocalVariableTable () {
        tag =  ATTR_LOCAL_VARIABLE_TABLE;
    }


    /**
   * Initialize from another object. Note that both objects use the same
   * references (shallow copy). Use clone() for a physical copy.
   */
    public LocalVariableTable (LocalVariableTable c) {
        this(c.getNameIndex(), c.getLength(), c.getLocalVariableTable(), c.getConstantPool());
    }


    /**
   * @param name_index Index in constant pool to `LocalVariableTable'
   * @param length Content length in bytes
   * @param local_variable_table Table of local variables
   * @param constant_pool Array of constants
   */
    public LocalVariableTable (int name_index, int length, LocalVariable[] local_variable_table, ConstantPool constant_pool) {
        this();
        this.name_index =  name_index;
        this.length =  length;
        this.constant_pool =  constant_pool;
        setLocalVariableTable(local_variable_table);
    }


    /**
   * Construct object from file stream.
   * @param name_index Index in constant pool
   * @param length Content length in bytes
   * @param file Input stream
   * @param constant_pool Array of constants
   * @throw IOException
   */
    LocalVariableTable (int name_index, int length, DataInputStream file, ConstantPool constant_pool)
        throws IOException
    {
        this(name_index, length, (LocalVariable[])null, constant_pool);
        local_variable_table_length =  (file.readUnsignedShort());
        local_variable_table =  new LocalVariable[local_variable_table_length];
        for (int i =  0; i < local_variable_table_length; i++) local_variable_table[i] =  new LocalVariable(file, constant_pool);
    }


    /**
   * Called by objects that are traversing the nodes of the tree implicitely
   * defined by the contents of a Java class. I.e., the hierarchy of methods,
   * fields, attributes, etc. spawns a tree of objects.
   *
   * @param v Visitor object
   */
    public void accept (Visitor v) {
        v.visitLocalVariableTable(this);
    }


    /**
   * Dump local variable table attribute to file stream in binary format.
   *
   * @param file Output file stream
   * @throw IOException
   */
    public final void dump (DataOutputStream file)
        throws IOException
    {
        super.dump(file);
        file.writeShort(local_variable_table_length);
        for (int i =  0; i < local_variable_table_length; i++) local_variable_table[i].dump(file);
    }


    /**
   * @return Constant pool used by this object.
   * @see ConstantPool
   */
    public final ConstantPool getConstantPool () {
        return  constant_pool;
    }


    /**
   * @return Array of local variables of method.
   */
    public final LocalVariable[] getLocalVariableTable () {
        return  local_variable_table;
    }


    /**
   * @param constant_pool Constant pool to be used for this object.
   * @see ConstantPool
   */
    public final void setConstantPool (ConstantPool constant_pool) {
        this.constant_pool =  constant_pool;
    }


    public final void setLocalVariableTable (LocalVariable[] local_variable_table) {
        this.local_variable_table =  local_variable_table;
        local_variable_table_length =  (local_variable_table == null)? 0 : local_variable_table.length;
    }


    /**
   * @return String representation.
   */
    public final String toString () {
        StringBuffer buf =  new StringBuffer("");

        for (int i =  0; i < local_variable_table_length; i++) {
            buf.append(local_variable_table[i].toString());
            if (i < local_variable_table_length - 1) buf.append(", ");
        }

        return  buf.toString();
    }

}

