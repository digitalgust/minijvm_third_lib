package  espresso.classfile.javaclass;




import  espresso.classfile.Constants;
import  java.io.*;




/**
 * This class is derived from <em>Attribute</em> and represents a table of 
 * line numbers for debugging purposes. This attribute is used by the 
 * <em>Code</em> attribute. It contains pairs of PCs and line numbers.
 *
 * @version 970922
 * @author  <A HREF="http://www.inf.fu-berlin.de/~dahm">M. Dahm</A>
 * @see     Code
 * @see     LineNumber
 */
public final class LineNumberTable
    extends Attribute
{

    private int line_number_table_length;
    private LineNumber[] line_number_table; // Table of line/numbers pairs
    private ConstantPool constant_pool;


    /**
   * Empty constructor, all attributes have to be defined via `setXXX'
   * methods. Use at your own risk.
   */
    public LineNumberTable () {
        tag =  ATTR_LINE_NUMBER_TABLE;
    }


    /**
   * Initialize from another object. Note that both objects use the same
   * references (shallow copy). Use clone() for a physical copy.
   */
    public LineNumberTable (LineNumberTable c) {
        this(c.getNameIndex(), c.getLength(), c.getLineNumberTable(), c.getConstantPool());
    }


    /*
   * @param name_index Index of name
   * @param length Content length in bytes
   * @param line_number_table Table of line/numbers pairs
   * @param constant_pool Array of constants
   */
    public LineNumberTable (int name_index, int length, LineNumber[] line_number_table, ConstantPool constant_pool) {
        this();
        this.name_index =  name_index;
        this.length =  length;
        this.constant_pool =  constant_pool;
        setLineNumberTable(line_number_table);
    }


    /**
   * Construct object from file stream.
   * @param name_index Index of name
   * @param length Content length in bytes
   * @param file Input stream
   * @throw IOException
   * @param constant_pool Array of constants
   */
    LineNumberTable (int name_index, int length, DataInputStream file, ConstantPool constant_pool)
        throws IOException
    {
        this(name_index, length, (LineNumber[])null, constant_pool);
        line_number_table_length =  (file.readUnsignedShort());
        line_number_table =  new LineNumber[line_number_table_length];
        for (int i =  0; i < line_number_table_length; i++) line_number_table[i] =  new LineNumber(file);
    }


    /**
   * Called by objects that are traversing the nodes of the tree implicitely
   * defined by the contents of a Java class. I.e., the hierarchy of methods,
   * fields, attributes, etc. spawns a tree of objects.
   *
   * @param v Visitor object
   */
    public void accept (Visitor v) {
        v.visitLineNumberTable(this);
    }


    /**
   * Dump line number table attribute to file stream in binary format.
   *
   * @param file Output file stream
   * @throw IOException
   */
    public final void dump (DataOutputStream file)
        throws IOException
    {
        super.dump(file);
        file.writeShort(line_number_table_length);
        for (int i =  0; i < line_number_table_length; i++) line_number_table[i].dump(file);
    }


    /**
   * @return Constant pool used by this object.
   * @see ConstantPool
   */
    public final ConstantPool getConstantPool () {
        return  constant_pool;
    }


    /**
   * @return Array of (pc offset, line number) pairs.
   */
    public final LineNumber[] getLineNumberTable () {
        return  line_number_table;
    }


    /**
   * @param constant_pool Constant pool to be used for this object.
   * @see ConstantPool
   */
    public final void setConstantPool (ConstantPool constant_pool) {
        this.constant_pool =  constant_pool;
    }


    /**
   * @param line_number_table.
   */
    public final void setLineNumberTable (LineNumber[] line_number_table) {
        this.line_number_table =  line_number_table;
        line_number_table_length =  (line_number_table == null)? 0 : line_number_table.length;
    }


    /**
   * @return String representation.
   */
    public final String toString () {
        StringBuffer buf =  new StringBuffer("");

        for (int i =  0; i < line_number_table_length; i++) {
            buf.append(line_number_table[i].toString());
            if (i < line_number_table_length - 1) buf.append(", ");
        }

        return  buf.toString();
    }

}

