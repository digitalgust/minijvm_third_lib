package  espresso.classfile.javaclass;




import  espresso.classfile.Constants;
import  java.io.*;




/**
 * This class is derived from <em>Attribute</em> and represents a constant 
 * value, i.e. a default value for initializing a class field.
 * This class is instantiated by the <em>Attribute.readAttribute()</em> method.
 *
 * @version 970922
 * @author  <A HREF="http://www.inf.fu-berlin.de/~dahm">M. Dahm</A>
 * @see     Attribute
 */
public final class ConstantValue
    extends Attribute
{

    private int constantvalue_index;
    private ConstantPool constant_pool;


    /**
   * Empty constructor, all attributes have to be defined via `setXXX'
   * methods. Use at your own risk.
   */
    public ConstantValue () {
        tag =  ATTR_CONSTANT_VALUE;
    }


    /**
   * Initialize from another object. Note that both objects use the same
   * references (shallow copy). Use clone() for a physical copy.
   */
    public ConstantValue (ConstantValue c) {
        this(c.getNameIndex(), c.getLength(), c.getConstantValueIndex(), c.getConstantPool());
    }


    /**
   * Construct object from file stream.
   * @param name_index Name index in constant pool
   * @param length Content length in bytes
   * @param file Input stream
   * @param constant_pool Array of constants
   * @throw IOException
   */
    ConstantValue (int name_index, int length, DataInputStream file, ConstantPool constant_pool)
        throws IOException
    {
        this(name_index, length, (int)file.readUnsignedShort(), constant_pool);
    }


    /**
   * @param name_index Name index in constant pool
   * @param length Content length in bytes
   * @param constantvalue_index Index in constant pool
   * @param constant_pool Array of constants
   */
    public ConstantValue (int name_index, int length, int constantvalue_index, ConstantPool constant_pool) {
        this();
        this.name_index =  name_index;
        this.length =  length;
        this.constantvalue_index =  constantvalue_index;
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
        v.visitConstantValue(this);
    }


    /**
   * Dump constant value attribute to file stream on binary format.
   *
   * @param file Output file stream
   * @throw IOException
   */
    public final void dump (DataOutputStream file)
        throws IOException
    {
        super.dump(file);
        file.writeShort(constantvalue_index);
    }


    /**
   * @return Constant pool used by this object.
   * @see ConstantPool
   */
    public final ConstantPool getConstantPool () {
        return  constant_pool;
    }


    /**
   * @return Index in constant pool of constant value.
   */
    public final int getConstantValueIndex () {
        return  constantvalue_index;
    }


    /**
   * @param constant_pool Constant pool to be used for this object.
   * @see ConstantPool
   */
    public final void setConstantPool (ConstantPool constant_pool) {
        this.constant_pool =  constant_pool;
    }


    /**
   * @param constantvalue_index.
   */
    public final void setConstantValueIndex (int constantvalue_index) {
        this.constantvalue_index =  constantvalue_index;
    }


    /**
   * @return String representation of constant value.
   */
    public final String toString ()
        throws InternalError
    {
        Constant c =  constant_pool.getConstant(constantvalue_index);
        String buf;
        int i;

        // Print constant to string depending on its type
        switch (c.getTag()) {
            case CONSTANT_Long:
                buf =  "" + ((ConstantLong)c).getBytes();
                break;

            case CONSTANT_Float:
                buf =  "" + ((ConstantFloat)c).getBytes();
                break;

            case CONSTANT_Double:
                buf =  "" + ((ConstantDouble)c).getBytes();
                break;

            case CONSTANT_Integer:
                buf =  "" + ((ConstantInteger)c).getBytes();
                break;

            case CONSTANT_String:
                i =  ((ConstantString)c).getStringIndex();
                c =  constant_pool.getConstant(i, CONSTANT_Utf8);

				/* Adding quotes to this constant seems to be wrong - S.M.Pericas */
                // buf =  "\"" + ((ConstantUtf8)c).getBytes() + "\"";
                buf =  ((ConstantUtf8)c).getBytes();

                break;

            default:
                throw  new InternalError("Type of ConstValue invalid: " + c);
        }

        return  buf;
    }

}

