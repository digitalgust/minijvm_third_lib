package  espresso.classfile.javaclass;




import  espresso.classfile.Constants;
import  java.io.*;




/**
 * Abstract super class for <em>Attribute</em> objects. Currently the
 * <em>ConstantValue</em>, <em>SourceFile</em>, <em>Code</em>,
 * <em>Exceptiontable</em>, <em>LineNumberTable</em>, 
 * <em>LocalVariableTable</em>, <em>InnerClasses</em> and
 * <em>Synthetic</em> attributes are supported. The
 * <em>Unknown</em> attribute stands for non-standard-attributes.
 *
 * @version 970922
 * @author  <A HREF="http://www.inf.fu-berlin.de/~dahm">M. Dahm</A>
 * @see     ConstantValue
 * @see     SourceFile
 * @see     Code
 * @see     Unknown
 * @see     ExceptionTable
 * @see     LineNumberTable
 * @see     LocalVariableTable 
 * @see     InnerClasses
 * @see     Synthetic
 * @see     Deprecated
*/
public abstract class Attribute
    implements Constants
{

    protected int name_index; // Points to attribute name in constant pool
    protected int length; // Content length of attribute field
    protected byte tag; // Tag to distiguish subclasses, analogous to


    /**
   * Called by objects that are traversing the nodes of the tree implicitely
   * defined by the contents of a Java class. I.e., the hierarchy of methods,
   * fields, attributes, etc. spawns a tree of objects.
   *
   * @param v Visitor object
   */
    public abstract void accept (Visitor v);


    /**
   * Dump attribute to file stream in binary format.
   *
   * @param file Output file stream
   * @throw IOException
   */
    public void dump (DataOutputStream file)
        throws IOException
    {
        file.writeShort(name_index);
        file.writeInt(length);
    }


    /**
   * @return Length of attribute field in bytes.
   */
    public final int getLength () {
        return  length;
    }


    /**
   * @return Name index in constant pool of attribute name.
   */
    public final int getNameIndex () {
        return  name_index;
    }


    /**
   * @return Tag of attribute, i.e. its type. Value may not be altered, thus
   * there is no setTag() method.
   */
    public final byte getTag () {
        return  tag;
    }


    // Constant.java, for efficiency reasons
    /* Class method reads one attribute from the input data stream.
   * This method must not be accessible from the outside. This method
   * is static because this is an abstract class and thus has no constructors.
   * This method is called by the <A HREF="DE.fub.inf.JavaClass.Field.html">
   * Field</A> and <A HREF="DE.fub.inf.JavaClass.Method.html">
   * Method</A> constructor methods.
   *
   * @see    Field
   * @see    Method
   * @param  file Input stream
   * @param  constant_pool Array of constants
   * @return Attribute
   * @throw  IOException
   * @throw  ClassFormatError
   * @throw  InternalError
   */
    static final Attribute readAttribute (DataInputStream file, ConstantPool constant_pool)
        throws IOException, ClassFormatError, InternalError
    {
        ConstantUtf8 c;
        String name;
        int name_index;
        int length;
        byte tag =  ATTR_UNKNOWN; // Unknown attribute
        // Get class name from constant pool via `name_index' indirection
        name_index =  (int)(file.readUnsignedShort());
        c =  (ConstantUtf8)constant_pool.getConstant(name_index, CONSTANT_Utf8);
        name =  c.getBytes();
        // Length of data in bytes
        length =  file.readInt();

        // Compare strings to find known attribute
        for (byte i =  0; i < KNOWN_ATTRIBUTES; i++) {
            if (name.equals(ATTRIBUTE_NAMES[i])) {
                tag =  i; // found!
                break;
            }
        }

        // Call proper constructor, depending on `tag'
        switch (tag) {
            case ATTR_UNKNOWN:
                return  new Unknown(name_index, length, file, constant_pool);

            case ATTR_CONSTANT_VALUE:
                return  new ConstantValue(name_index, length, file, constant_pool);

            case ATTR_SOURCE_FILE:
                return  new SourceFile(name_index, length, file, constant_pool);

            case ATTR_CODE:
                return  new Code(name_index, length, file, constant_pool);

            case ATTR_EXCEPTIONS:
                return  new ExceptionTable(name_index, length, file, constant_pool);

            case ATTR_LINE_NUMBER_TABLE:
                return  new LineNumberTable(name_index, length, file, constant_pool);

            case ATTR_LOCAL_VARIABLE_TABLE:
                return  new LocalVariableTable(name_index, length, file, constant_pool);

            case ATTR_INNER_CLASSES:
                return  new InnerClasses(name_index, length, file, constant_pool);

            case ATTR_SYNTHETIC:
                return  new Synthetic(name_index, length, file, constant_pool);

            case ATTR_DEPRECATED:
                return  new Deprecated(name_index, length, file, constant_pool);

            default: // Never reached
                throw  new InternalError("Ooops! default case reached.");
        }
    }


    /**
   * @param Attribute length in bytes.
   */
    public final void setLength (int length) {
        this.length =  length;
    }


    /**
   * @param name_index of attribute.
   */
    public final void setNameIndex (int name_index) {
        this.name_index =  name_index;
    }

}

