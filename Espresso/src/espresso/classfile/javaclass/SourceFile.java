package  espresso.classfile.javaclass;




import  espresso.classfile.Constants;
import  java.io.*;




/**
 * This class is derived from <em>Attribute</em> and represents a reference
 * to the source file of this class.
 * It is instantiated from the <em>Attribute.readAttribute()</em> method.
 *
 * @version 970922
 * @author  <A HREF="http://www.inf.fu-berlin.de/~dahm">M. Dahm</A>
 * @see     Attribute
 */
public final class SourceFile
    extends Attribute
{

    private int sourcefile_index;
    private ConstantPool constant_pool;


    /**
   * Empty constructor, all attributes have to be defined via `setXXX'
   * methods. Use at your own risk.
   */
    public SourceFile () {
        tag =  ATTR_SOURCE_FILE;
    }


    /**
   * Initialize from another object. Note that both objects use the same
   * references (shallow copy). Use clone() for a physical copy.
   */
    public SourceFile (SourceFile c) {
        this(c.getNameIndex(), c.getLength(), c.getSourceFileIndex(), c.getConstantPool());
    }


    /**
   * Construct object from file stream.
   * @param name_index Index in constant pool to CONSTANT_Utf8
   * @param length Content length in bytes
   * @param file Input stream
   * @param constant_pool Array of constants
   * @throw IOException
   */
    SourceFile (int name_index, int length, DataInputStream file, ConstantPool constant_pool)
        throws IOException
    {
        this(name_index, length, file.readUnsignedShort(), constant_pool);
    }


    /**
   * @param name_index Index in constant pool to CONSTANT_Utf8
   * @param length Content length in bytes
   * @param constant_pool Array of constants
   * @param sourcefile_index Index in constant pool to CONSTANT_Utf8
   */
    public SourceFile (int name_index, int length, int sourcefile_index, ConstantPool constant_pool) {
        this();
        this.name_index =  name_index;
        this.length =  length;
        this.constant_pool =  constant_pool;
        this.sourcefile_index =  sourcefile_index;
    }


    /**
   * Called by objects that are traversing the nodes of the tree implicitely
   * defined by the contents of a Java class. I.e., the hierarchy of methods,
   * fields, attributes, etc. spawns a tree of objects.
   *
   * @param v Visitor object
   */
    public void accept (Visitor v) {
        v.visitSourceFile(this);
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
        file.writeShort(sourcefile_index);
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
    public final int getSourceFileIndex () {
        return  sourcefile_index;
    }


    /**
   * @param constant_pool Constant pool to be used for this object.
   * @see ConstantPool
   */
    public final void setConstantPool (ConstantPool constant_pool) {
        this.constant_pool =  constant_pool;
    }


    /**
   * @param sourcefile_index.
   */
    public final void setSourceFileIndex (int sourcefile_index) {
        this.sourcefile_index =  sourcefile_index;
    }


    /**
   * @return Source file name.
   */
    public final String getSourceFileName () {
        ConstantUtf8 c =  (ConstantUtf8)constant_pool.getConstant(sourcefile_index, CONSTANT_Utf8);
        return  c.getBytes();
    }


    /**
   * @return String representation
   */
    public final String toString () {
        return  "SourceFile(" + getSourceFileName() + ")";
    }

}

