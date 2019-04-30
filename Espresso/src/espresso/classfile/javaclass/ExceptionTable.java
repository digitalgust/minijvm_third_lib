package  espresso.classfile.javaclass;




import  espresso.classfile.Constants;
import  java.io.*;




/** 
 * Class is derived from <em>Attribute</em> and represents the table
 * of exceptions that are thrown by a method. This attribute may be used
 * once per method.
 *
 * @version 970922
 * @author  <A HREF="http://www.inf.fu-berlin.de/~dahm">M. Dahm</A>
 * @see     Code
 * @see     Attribute 
 */
public final class ExceptionTable
    extends Attribute
{

    private int number_of_exceptions; // Table of indices into
    private int[] exception_index_table; // constant pool
    private ConstantPool constant_pool;


    /**
   * Empty constructor, all attributes have to be defined via `setXXX'
   * methods. Use at your own risk.
   */
    public ExceptionTable () {
        tag =  ATTR_EXCEPTIONS;
    }


    /**
   * Initialize from another object. Note that both objects use the same
   * references (shallow copy). Use clone() for a physical copy.
   */
    public ExceptionTable (ExceptionTable c) {
        this(c.getNameIndex(), c.getLength(), c.getExceptionIndexTable(), c.getConstantPool());
    }


    /**
   * @param name_index Index in constant pool
   * @param length Content length in bytes
   * @param exception_index_table Table of indices in constant pool
   * @param constant_pool Array of constants
   */
    public ExceptionTable (int name_index, int length, int[] exception_index_table, ConstantPool constant_pool) {
        this();
        this.name_index =  name_index;
        this.length =  length;
        this.constant_pool =  constant_pool;
        setExceptionIndexTable(exception_index_table);
    }


    /**
   * Construct object from file stream.
   * @param name_index Index in constant pool
   * @param length Content length in bytes
   * @param file Input stream
   * @param constant_pool Array of constants
   * @throw IOException
   */
    ExceptionTable (int name_index, int length, DataInputStream file, ConstantPool constant_pool)
        throws IOException
    {
        this(name_index, length, (int[])null, constant_pool);
        number_of_exceptions =  file.readUnsignedShort();
        exception_index_table =  new int[number_of_exceptions];
        for (int i =  0; i < number_of_exceptions; i++) exception_index_table[i] =  file.readUnsignedShort();
    }


    /**
   * Called by objects that are traversing the nodes of the tree implicitely
   * defined by the contents of a Java class. I.e., the hierarchy of methods,
   * fields, attributes, etc. spawns a tree of objects.
   *
   * @param v Visitor object
   */
    public void accept (Visitor v) {
        v.visitExceptionTable(this);
    }


    /**
   * Dump exceptions attribute to file stream in binary format.
   *
   * @param file Output file stream
   * @throw IOException
   */
    public final void dump (DataOutputStream file)
        throws IOException
    {
        super.dump(file);
        file.writeShort(number_of_exceptions);
        for (int i =  0; i < number_of_exceptions; i++) file.writeShort(exception_index_table[i]);
    }


    /**
   * @return Constant pool used by this object.
   * @see ConstantPool
   */
    public final ConstantPool getConstantPool () {
        return  constant_pool;
    }


    /**
   * @return Array of indices into constant pool of thrown exceptions.
   */
    public final int[] getExceptionIndexTable () {
        return  exception_index_table;
    }


    /**
   * @return Length of exception table.
   */
    public final int getNumberOfExceptions () {
        return  number_of_exceptions;
    }


    /**
   * @param constant_pool Constant pool to be used for this object.
   * @see ConstantPool
   */
    public final void setConstantPool (ConstantPool constant_pool) {
        this.constant_pool =  constant_pool;
    }


    /**
   * @param exception_index_table.
   * Also redefines number_of_exceptions according to table length.
   */
    public final void setExceptionIndexTable (int[] exception_index_table) {
        this.exception_index_table =  exception_index_table;
        number_of_exceptions =  (exception_index_table == null)? 0 : exception_index_table.length;
    }


    /**
   * @return String representation, i.e. a list of thrown exceptions.
   */
    public final String toString () {
        StringBuffer buf =  new StringBuffer("");
        String str;

        for (int i =  0; i < number_of_exceptions; i++) {
            str =  constant_pool.getConstantString(exception_index_table[i], CONSTANT_Class);
            buf.append(Utility.compactClassName(str));
            if (i < number_of_exceptions - 1) buf.append(", ");
        }

        return  buf.toString();
    }

}

