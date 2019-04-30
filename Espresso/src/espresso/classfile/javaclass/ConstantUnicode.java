package  espresso.classfile.javaclass;




import  espresso.classfile.Constants;
import  java.io.*;




/** 
 * This class is derived from the abstract 
 * <A HREF="DE.fub.inf.JVM.JavaClass.Constant.html">Constant</A> class 
 * and represents a reference to a Unicode encoded string.
 *
 * @version 970801
 * @author  <A HREF="http://www.inf.fu-berlin.de/~dahm">M. Dahm</A>
 * @see     Constant
 */
public final class ConstantUnicode
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
    public ConstantUnicode () {
        tag =  CONSTANT_Unicode;
    }


    /**
   * Initialize from another object.
   */
    public ConstantUnicode (ConstantUnicode c) {
        this(c.getBytes());
    }


    /**
   * Initialize instance from file data.
   *
   * @param file Input stream
   * @throw IOException
   */
    ConstantUnicode (DataInputStream file)
        throws IOException
    {
        this();
        length =  file.readUnsignedShort();
        char[] chbytes =  new char[length];
        for (int i =  0; i < length; i++) chbytes[i] =  file.readChar();
        bytes =  new String(chbytes);
    }


    /**
   * @param bytes Data
   */
    public ConstantUnicode (String bytes) {
        this();
        this.bytes =  bytes;
        this.length =  (bytes == null)? 0 : (bytes.length()*2); // sizeof(char) is 2
    }


    /**
   * Called by objects that are traversing the nodes of the tree implicitely
   * defined by the contents of a Java class. I.e., the hierarchy of methods,
   * fields, attributes, etc. spawns a tree of objects.
   *
   * @param v Visitor object
   */
    public void accept (Visitor v) {
        v.visitConstantUnicode(this);
    }


    /**
   * Dump String in Unicode format to file stream.
   *
   * @param file Output file stream
   * @throw IOException
   */
    public final void dump (DataOutputStream file)
        throws IOException
    {
        file.writeByte(tag);
        file.writeShort(length);
        file.writeChars(bytes);
    }


    /**
   * @return Data converted into normal String.
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
   * @return String representation.
   */
    public final String toString () {
        return  super.toString() + "(length = " + length + ", bytes = \"" + bytes + "\")";
    }

}

