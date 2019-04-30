package  espresso.classfile.javaclass;




import  espresso.classfile.Constants;
import  java.io.*;




/** 
 * This class is derived from the abstract 
 * <A HREF="DE.fub.inf.JVM.JavaClass.Constant.html">Constant</A> class 
 * and represents a reference to the name and signature
 * of a field or method.
 *
 * @version 971114
 * @author  <A HREF="http://www.inf.fu-berlin.de/~dahm">M. Dahm</A>
 * @see     Constant
 */
public final class ConstantNameAndType
    extends Constant
{

    private int name_index, // Name of field/method
    signature_index; // and its signature.


    /**
   * Empty constructor, all attributes have to be defined via `setXXX'
   * methods. Use at your own risk.
   */
    public ConstantNameAndType () {
        tag =  CONSTANT_NameAndType;
    }


    /**
   * Initialize from another object.
   */
    public ConstantNameAndType (ConstantNameAndType c) {
        this(c.getNameIndex(), c.getSignatureIndex());
    }


    /**
   * Initialize instance from file data.
   *
   * @param file Input stream
   * @throw IOException
   */
    ConstantNameAndType (DataInputStream file)
        throws IOException
    {
        this((int)file.readUnsignedShort(), (int)file.readUnsignedShort());
    }


    /**
   * @param name_index Name of field/method
   * @param signature_index and its signature
   */
    public ConstantNameAndType (int name_index, int signature_index) {
        this();
        this.name_index =  name_index;
        this.signature_index =  signature_index;
    }


    /**
   * Called by objects that are traversing the nodes of the tree implicitely
   * defined by the contents of a Java class. I.e., the hierarchy of methods,
   * fields, attributes, etc. spawns a tree of objects.
   *
   * @param v Visitor object
   */
    public void accept (Visitor v) {
        v.visitConstantNameAndType(this);
    }


    /**
   * Dump name and signature index to file stream in binary format.
   *
   * @param file Output file stream
   * @throw IOException
   */
    public final void dump (DataOutputStream file)
        throws IOException
    {
        file.writeByte(tag);
        file.writeShort(name_index);
        file.writeShort(signature_index);
    }


    /**
   * @return Name index in constant pool of field/method name.
   */
    public final int getNameIndex () {
        return  name_index;
    }


    /**
   * @return Index in constant pool of field/method signature.
   */
    public final int getSignatureIndex () {
        return  signature_index;
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
   * @return String representation
   */
    public final String toString () {
        return  super.toString() + "(name_index = " + name_index + ", signature_index = " + signature_index + ")";
    }

}

