package  espresso.classfile.javaclass;




import  espresso.classfile.Constants;
import  java.io.*;
import  java.util.*;




/**
 * This class is derived from <em>Attribute</em> and represents a reference to an
 * unknown (i.e. unimplemented) attribute of this class.
 * It is instantiated from the <em>Attribute.readAttribute()</em> method.
 *
 * @version 971017
 * @author  <A HREF="http://www.inf.fu-berlin.de/~dahm">M. Dahm</A>
 * @see     Attribute
 */
public final class Unknown
    extends Attribute
{

    private byte[] bytes;
    private ConstantPool constant_pool;
    private String name;
    private static Hashtable unknown_attributes =  new Hashtable();


    /** @return array of unknown attributes, but just one for each kind.
   */
    static Unknown[] getUnknownAttributes () {
        Unknown[] unknowns =  new Unknown[unknown_attributes.size()];
        Enumeration entries =  unknown_attributes.elements();
        for (int i =  0; entries.hasMoreElements(); i++) unknowns[i] =  (Unknown)entries.nextElement();
        unknown_attributes =  new Hashtable();
        return  unknowns;
    }


    /**
   * Empty constructor, all attributes have to be defined via `setXXX'
   * methods. Use at your own risk.
   */
    public Unknown () {
        tag =  ATTR_UNKNOWN;
        unknown_attributes =  new Hashtable();
    }


    /**
   * Initialize from another object. Note that both objects use the same
   * references (shallow copy). Use clone() for a physical copy.
   */
    public Unknown (Unknown c) {
        this(c.getNameIndex(), c.getLength(), c.getBytes(), c.getConstantPool());
    }


    /**
   * Create a non-standard attribute.
   *
   * @param name_index Index in constant pool
   * @param length Content length in bytes
   * @param bytes Attribute contents
   * @param constant_pool Array of constants
   */
    public Unknown (int name_index, int length, byte[] bytes, ConstantPool constant_pool) {
        this();
        this.name_index =  name_index;
        this.length =  length;
        this.bytes =  bytes;
        this.constant_pool =  constant_pool;
        name =  ((ConstantUtf8)constant_pool.getConstant(name_index, CONSTANT_Utf8)).getBytes();
        unknown_attributes.put(name, this);
    }


    /**
   * Construct object from file stream.
   * @param name_index Index in constant pool
   * @param length Content length in bytes
   * @param file Input stream
   * @param constant_pool Array of constants
   * @throw IOException
   */
    Unknown (int name_index, int length, DataInputStream file, ConstantPool constant_pool)
        throws IOException
    {
        this(name_index, length, (byte[])null, constant_pool);
        if (length > 0) {
            bytes =  new byte[length];
            file.readFully(bytes);
        }
    }


    /**
   * Called by objects that are traversing the nodes of the tree implicitely
   * defined by the contents of a Java class. I.e., the hierarchy of methods,
   * fields, attributes, etc. spawns a tree of objects.
   *
   * @param v Visitor object
   */
    public void accept (Visitor v) {
        v.visitUnknown(this);
    }


    /**
   * Dump unknown bytes to file stream.
   *
   * @param file Output file stream
   * @throw IOException
   */
    public final void dump (DataOutputStream file)
        throws IOException
    {
        super.dump(file);
        if (length > 0) file.write(bytes, 0, length);
    }


    /**
   * @return data bytes.
   */
    public final byte[] getBytes () {
        return  bytes;
    }


    /**
   * @return name of attribute.
   */
    public final String getName () {
        return  name;
    }


    /**
   * @return Constant pool used by this object.
   * @see ConstantPool
   */
    public final ConstantPool getConstantPool () {
        return  constant_pool;
    }


    /**
   * @param bytes.
   */
    public final void setBytes (byte[] bytes) {
        this.bytes =  bytes;
    }


    /**
   * @param constant_pool Constant pool to be used for this object.
   * @see ConstantPool
   */
    public final void setConstantPool (ConstantPool constant_pool) {
        this.constant_pool =  constant_pool;
    }


    /**
   * @return String representation.
   */
    public final String toString () {
        String hex =  Utility.toHexString(bytes);
        if (hex.length() > 20) hex =  hex.substring(0, 20) + "... (truncated)";
        return  "(Unknown attribute " + name + ": " + hex + ")";
    }

}

