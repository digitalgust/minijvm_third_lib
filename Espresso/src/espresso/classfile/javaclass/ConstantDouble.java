package  espresso.classfile.javaclass;




import  espresso.classfile.Constants;
import  java.io.*;




/** 
 * This class is derived from the abstract 
 * <A HREF="DE.fub.inf.JVM.JavaClass.Constant.html">Constant</A> class 
 * and represents a reference to a Double object.
 *
 * @version 970922
 * @author  <A HREF="http://www.inf.fu-berlin.de/~dahm">M. Dahm</A>
 * @see     Constant
 */
public final class ConstantDouble
    extends Constant
{

    private double bytes;


    /**
   * Empty constructor, all attributes have to be defined via `setXXX'
   * methods. Use at your own risk.
   */
    public ConstantDouble () {
        tag =  CONSTANT_Double;
    }


    /** 
   * @param bytes Data
   */
    public ConstantDouble (double bytes) {
        this();
        this.bytes =  bytes;
    }


    /**
   * Initialize from another object.
   */
    public ConstantDouble (ConstantDouble c) {
        this(c.getBytes());
    }


    /** 
   * Initialize instance from file data.
   *
   * @param file Input stream
   * @throw IOException
   */
    ConstantDouble (DataInputStream file)
        throws IOException
    {
        this(file.readDouble());
    }


    /**
   * Called by objects that are traversing the nodes of the tree implicitely
   * defined by the contents of a Java class. I.e., the hierarchy of methods,
   * fields, attributes, etc. spawns a tree of objects.
   *
   * @param v Visitor object
   */
    public void accept (Visitor v) {
        v.visitConstantDouble(this);
    }


    /**
   * Dump constant double to file stream in binary format.
   *
   * @param file Output file stream
   * @throw IOException
   */
    public final void dump (DataOutputStream file)
        throws IOException
    {
        file.writeByte(tag);
        file.writeDouble(bytes);
    }


    /**
   * @return data, i.e. 8 bytes.
   */
    public final double getBytes () {
        return  bytes;
    }


    /**
   * @param bytes.
   */
    public final void setBytes (double bytes) {
        this.bytes =  bytes;
    }


    /**
   * @return String representation.
   */
    public final String toString () {
        return  super.toString() + "(bytes = " + bytes + ")";
    }

}

