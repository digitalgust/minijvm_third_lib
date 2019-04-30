package  espresso.classfile.javaclass;




import  espresso.classfile.Constants;
import  java.io.*;




/** 
 * This class is derived from the abstract 
 * <A HREF="DE.fub.inf.JVM.JavaClass.Constant.html">Constant</A> class 
 * and represents a reference to a Utf8 encoded string.
 *
 * @version 970922
 * @author  <A HREF="http://www.inf.fu-berlin.de/~dahm">M. Dahm</A>
 * @see     Constant
 */
public final class ConstantUtf8
    extends Constant
{

    private int length; // Length of string.
    /* This does not follow the specification closely, but is much more
   * convenient. Could rather be of type `byte[]'.
   */
    private String bytes;


    /**
   * Empty constructor, all attributes have to be defined via `setXXX'
   * methods. Use at your own risk.
   */
    public ConstantUtf8 () {
        tag =  CONSTANT_Utf8;
    }


    /**
   * Initialize from another object.
   */
    public ConstantUtf8 (ConstantUtf8 c) {
        this(c.getBytes());
    }


    /**
   * Initialize instance from file data.
   *
   * @param file Input stream
   * @throw IOException
   */
    ConstantUtf8 (DataInputStream file)
        throws IOException
    {
        this();
        length =  file.readUnsignedShort();
        byte[] bytes =  new byte[length];
        file.readFully(bytes); // Block until all is being read
        //    this.bytes = new String(bytes, 0); // deprecated
        this.bytes =  new String(bytes);
    }


    /**
   * @param bytes Data
   */
    public ConstantUtf8 (String bytes) {
        this();
        this.bytes =  bytes;
        this.length =  (bytes == null)? 0 : bytes.length();
    }


    /**
   * Called by objects that are traversing the nodes of the tree implicitely
   * defined by the contents of a Java class. I.e., the hierarchy of methods,
   * fields, attributes, etc. spawns a tree of objects.
   *
   * @param v Visitor object
   */
    public void accept (Visitor v) {
        v.visitConstantUtf8(this);
    }


    /**
   * Dump String in Utf8 format to file stream.
   *
   * @param file Output file stream
   * @throw IOException
   */
    public final void dump (DataOutputStream file)
        throws IOException
    {
        file.writeByte(tag);
        file.writeShort(length);
        file.writeBytes(bytes);
    }


    /**
   * @return Data converted to string.
   */
    public final String getBytes () {
        return  bytes;
    }


    /**
   * @return String length.
   */
    public final int getLength () {
        return  length;
    }


    /**
   * @param bytes.
   */
    public final void setBytes (String bytes) {
        this.bytes =  bytes;
    }


    /**
   * @param length.
   */
    public final void setLength (int length) {
        this.length =  length;
    }


    /**
   * @return String representation
   */
    public final String toString () {
        return  super.toString() + "(length = " + length + ", bytes = \"" + Utility.replace(bytes, "\n", "\\n") + "\")";
    }

}

