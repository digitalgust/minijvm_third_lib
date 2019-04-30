package  espresso.classfile.javaclass;




import  espresso.classfile.Constants;
import  java.io.*;




/** 
 * Abstract super class for Fieldref and Methodref constants.
 *
 * @version 980205
 * @author  <A HREF="http://www.inf.fu-berlin.de/~dahm">M. Dahm</A>
 * @see     ConstantFieldref
 * @see     ConstantMethodref
 * @see     ConstantInterfaceMethodref
 */
public abstract class ConstantCP
    extends Constant
{

    /** References to the constants containing the class and the field signature
   */
    protected int class_index, name_and_type_index;


    /**
   * Initialize from another object.
   */
    public ConstantCP (ConstantCP c) {
        this(c.getTag(), c.getClassIndex(), c.getNameAndTypeIndex());
    }


    /**
   * Initialize instance from file data.
   *
   * @param tag  Constant type tag
   * @param file Input stream
   * @throw IOException
   */
    ConstantCP (byte tag, DataInputStream file)
        throws IOException
    {
        this(tag, file.readUnsignedShort(), file.readUnsignedShort());
    }


    /**
   * @param class_index Reference to the class containing the field
   * @param name_and_type_index and the field signature
   */
    public ConstantCP (byte tag, int class_index, int name_and_type_index) {
        this.tag =  tag;
        this.class_index =  class_index;
        this.name_and_type_index =  name_and_type_index;
    }


    /** 
   * Dump constant field reference to file stream in binary format.
   *
   * @param file Output file stream
   * @throw IOException
   */
    public final void dump (DataOutputStream file)
        throws IOException
    {
        file.writeByte(tag);
        file.writeShort(class_index);
        file.writeShort(name_and_type_index);
    }


    /**
   * @return Reference (index) to class this field belongs to.
   */
    public final int getClassIndex () {
        return  class_index;
    }


    /**
   * @return Reference (index) to signature of the field.
   */
    public final int getNameAndTypeIndex () {
        return  name_and_type_index;
    }


    /**
   * @param class_index.
   */
    public final void setClassIndex (int class_index) {
        this.class_index =  class_index;
    }


    /**
   * @param name_and_type_index.
   */
    public final void setNameAndTypeIndex (int name_and_type_index) {
        this.name_and_type_index =  name_and_type_index;
    }


    /**
   * @return String representation.
   */
    public final String toString () {
        return  super.toString() + "(class_index = " + class_index + ", name_and_type_index = " + name_and_type_index + ")";
    }


    /**
   * Called by objects that are traversing the nodes of the tree implicitely
   * defined by the contents of a Java class. I.e., the hierarchy of methods,
   * fields, attributes, etc. spawns a tree of objects.
   *
   * @param v Visitor object
   */
    public abstract void accept (Visitor v);

}

