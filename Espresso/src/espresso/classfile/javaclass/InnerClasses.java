package  espresso.classfile.javaclass;




import  espresso.classfile.Constants;
import  java.io.*;




/**
 * This class is derived from <em>Attribute</em> and denotes that this class
 * is an Inner class of another.
 * to the source file of this class.
 * It is instantiated from the <em>Attribute.readAttribute()</em> method.
 *
 * @version 970922
 * @author  <A HREF="http://www.inf.fu-berlin.de/~dahm">M. Dahm</A>
 * @see     Attribute
 */
public final class InnerClasses
    extends Attribute
{

    private InnerClass[] inner_classes;
    private ConstantPool constant_pool;
    private int number_of_classes;


    /**
   * Empty constructor, all attributes have to be defined via `setXXX'
   * methods. Use at your own risk.
   */
    public InnerClasses () {
        tag =  ATTR_INNER_CLASSES;
    }


    /**
   * Initialize from another object. Note that both objects use the same
   * references (shallow copy). Use clone() for a physical copy.
   */
    public InnerClasses (InnerClasses c) {
        this(c.getNameIndex(), c.getLength(), c.getInnerClasses(), c.getConstantPool());
    }


    /**
   * @param name_index Index in constant pool to CONSTANT_Utf8
   * @param length Content length in bytes
   * @param inner_classes array of inner classes attributes
   * @param constant_pool Array of constants
   * @param sourcefile_index Index in constant pool to CONSTANT_Utf8
   */
    public InnerClasses (int name_index, int length, InnerClass[] inner_classes, ConstantPool constant_pool) {
        this();
        this.name_index =  name_index;
        this.length =  length;
        this.constant_pool =  constant_pool;
        setInnerClasses(inner_classes);
    }


    /**
   * Construct object from file stream.
   *
   * @param name_index Index in constant pool to CONSTANT_Utf8
   * @param length Content length in bytes
   * @param file Input stream
   * @param constant_pool Array of constants
   * @throw IOException
   */
    InnerClasses (int name_index, int length, DataInputStream file, ConstantPool constant_pool)
        throws IOException
    {
        this(name_index, length, (InnerClass[])null, constant_pool);
        number_of_classes =  file.readUnsignedShort();
        inner_classes =  new InnerClass[number_of_classes];
        for (int i =  0; i < number_of_classes; i++) inner_classes[i] =  new InnerClass(file);
    }


    /**
   * Called by objects that are traversing the nodes of the tree implicitely
   * defined by the contents of a Java class. I.e., the hierarchy of methods,
   * fields, attributes, etc. spawns a tree of objects.
   *
   * @param v Visitor object
   */
    public void accept (Visitor v) {
        v.visitInnerClasses(this);
    }


    /**
   * Dump source file attribute to file stream in binary format.
   *
   * @param file Output file stream
   * @throw IOException
   */
    public final void dump (DataOutputStream file)
        throws IOException
    {
        super.dump(file);
        file.writeShort(number_of_classes);
        for (int i =  0; i < number_of_classes; i++) inner_classes[i].dump(file);
    }


    /**
   * @return Constant pool used by this object.
   * @see ConstantPool
   */
    public final ConstantPool getConstantPool () {
        return  constant_pool;
    }


    /**
   * @return Index in constant pool of source file name.
   */
    public final InnerClass[] getInnerClasses () {
        return  inner_classes;
    }


    /**
   * @param constant_pool Constant pool to be used for this object.
   * @see ConstantPool
   */
    public final void setConstantPool (ConstantPool constant_pool) {
        this.constant_pool =  constant_pool;
    }


    /**
   * @param inner_classes.
   */
    public final void setInnerClasses (InnerClass[] inner_classes) {
        this.inner_classes =  inner_classes;
        number_of_classes =  (inner_classes == null)? 0 : inner_classes.length;
    }


    /**
   * @return String representation.
   */
    public final String toString () {
        StringBuffer buf =  new StringBuffer();
        for (int i =  0; i < number_of_classes; i++) buf.append(inner_classes[i].toString(constant_pool) + "\n");
        return  buf.toString();
    }

}

