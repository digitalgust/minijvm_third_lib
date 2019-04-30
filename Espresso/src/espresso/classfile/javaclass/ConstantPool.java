package  espresso.classfile.javaclass;




import  espresso.classfile.Constants;
import  java.io.*;




/**
 * This class represents the constant pool, i.e. a table of constants.
 * It may contain null references, due to the JVM specification that skips
 * an entry after an 8-byte constant (double, long) entry.
 *
 * @version 970918
 * @see     Constant
 * @author <A HREF="http://www.inf.fu-berlin.de/~dahm">M. Dahm</A>
 */
public final class ConstantPool
    implements Constants
{

    private int constant_pool_count;
    private Constant[] constant_pool;


    /**
   * @param constant_pool Array of constants
   */
    public ConstantPool (Constant[] constant_pool) {
        setConstantPool(constant_pool);
    }


    /**
   * Read constants from given file stream.
   *
   * @param file Input stream
   * @throw IOException
   * @throw ClassFormatError
   */
    ConstantPool (DataInputStream file)
        throws IOException, ClassFormatError
    {
        byte tag;
        constant_pool_count =  file.readUnsignedShort();
        constant_pool =  new Constant[constant_pool_count];

        /* constant_pool[0] is unused by the compiler and may be used freely
     * by the implementation.
     */
        for (int i =  1; i < constant_pool_count; i++) {
            constant_pool[i] =  readConstant(file);
            /* Quote from the JVM specification:
       * "All eight byte constants take up two spots in the constant pool.
       * If this is the n'th byte in the constant pool, then the next item
       * will be numbered n+2"
       * 
       * Thus we have to increment the index counter.
       */
            tag =  constant_pool[i].getTag();
            if ((tag == CONSTANT_Double) || (tag == CONSTANT_Long)) i++;
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
        v.visitConstantPool(this);
    }


    /**
   * Resolve constant to a string representation.
   *
   * @param  constant Constant to be printed
   * @return String representation
   */
    public String constantToString (Constant c)
        throws ClassFormatError
    {
        String str;
        int i;
        byte tag =  c.getTag();

        switch (tag) {
            case CONSTANT_Class:
                i =  ((ConstantClass)c).getNameIndex();
                c =  getConstant(i, CONSTANT_Utf8);
                str =  Utility.compactClassName(((ConstantUtf8)c).getBytes(), false);
                break;

            case CONSTANT_String:
                i =  ((ConstantString)c).getStringIndex();
                c =  getConstant(i, CONSTANT_Utf8);
                str =  "\"" + ((ConstantUtf8)c).getBytes() + "\"";
                break;

            case CONSTANT_Utf8:
                str =  ((ConstantUtf8)c).getBytes();
                break;

            case CONSTANT_Unicode:
                str =  ((ConstantUnicode)c).getBytes();
                break;

            case CONSTANT_Double:
                str =  "" + ((ConstantDouble)c).getBytes();
                break;

            case CONSTANT_Float:
                str =  "" + ((ConstantFloat)c).getBytes();
                break;

            case CONSTANT_Long:
                str =  "" + ((ConstantLong)c).getBytes();
                break;

            case CONSTANT_Integer:
                str =  "" + ((ConstantInteger)c).getBytes();
                break;

            case CONSTANT_NameAndType:
                str =  (constantToString(((ConstantNameAndType)c).getNameIndex(), CONSTANT_Utf8) + ":" + constantToString(((ConstantNameAndType)c).getSignatureIndex(), CONSTANT_Utf8));
                break;

            case CONSTANT_InterfaceMethodref:
            case CONSTANT_Methodref:
            case CONSTANT_Fieldref:
                str =  (constantToString(((ConstantCP)c).getClassIndex(), CONSTANT_Class) + "." + constantToString(((ConstantCP)c).getNameAndTypeIndex(), CONSTANT_NameAndType));
                break;

            default: // Never reached
                str =  "<Error>";
        }

        return  str;
    }


    /**
   * Retrieve constant at `index' from constant pool and resolve it to
   * a string representation.
   *
   * @param  index of constant in constant pool
   * @param  tag expected type
   * @return String representation
   */
    public String constantToString (int index, byte tag)
        throws ClassFormatError
    {
        Constant c =  getConstant(index, tag);
        return  constantToString(c);
    }


    /** 
   * Dump constant pool to file stream in binary format.
   *
   * @param file Output file stream
   * @throw IOException
   */
    public void dump (DataOutputStream file)
        throws IOException
    {
        file.writeShort(constant_pool_count);
        for (int i =  1; i < constant_pool_count; i++) if (constant_pool[i] != null) constant_pool[i].dump(file);
    }


    /**
   * Get constant from constant pool.
   *
   * @param  index Index in constant pool
   * @return Constant value
   * @see    Constant
   */
    public final Constant getConstant (int index) {
        return  constant_pool[index];
    }


    /**
   * Get constant from constant pool and check whether it has the
   * expected type.
   *
   * @param  index Index in constant pool
   * @param  tag Tag of expected constant, i.e. its type
   * @return Constant value
   * @see    Constant
   * @throw  ClassFormatError
   */
    public final Constant getConstant (int index, byte tag)
        throws ClassFormatError
    {
        Constant c;
        c =  constant_pool[index];
        if (c == null) throw  new ClassFormatError("Constant_pool at index " + index + " is null.");
        if (c.getTag() == tag) return  c; else throw  new ClassFormatError("Expected class `" + CONSTANT_NAMES[tag] + "' at index " + index + " and got " + c);
    }


    /**
   * @return Array of constants.
   * @see    Constant
   */
    public final Constant[] getConstantPool () {
        return  constant_pool;
    }


    /**
   * Get string from constant pool and bypass the indirection of 
   * `ConstantClass' and `ConstantString' objects. I.e. these classes have
   * an index field that points to another entry of the constant pool of
   * type `ConstantUtf8' which contains the real data.
   *
   * @param  index Index in constant pool
   * @param  tag Tag of expected constant, either ConstantClass or ConstantString
   * @return Contents of string reference
   * @see    ConstantClass
   * @see    ConstantString
   * @throw  ClassFormatError
   */
    public final String getConstantString (int index, byte tag)
        throws ClassFormatError
    {
        Constant c;
        int i;
        String s;
        c =  getConstant(index, tag);

        /* This switch() is not that elegant, since the two classes have the
     * same contents, they just differ in the name of the index
     * field variable.
     * But we want to stick to the JVM naming conventions closely though
     * we could have solved these more elegantly by using the same
     * variable name or by subclassing.
     */
        switch (tag) {
            case CONSTANT_Class:
                i =  ((ConstantClass)c).getNameIndex();
                break;

            case CONSTANT_String:
                i =  ((ConstantString)c).getStringIndex();
                break;

            default:
                throw  new InternalError("getConstantString" + "called with illegal tag " + tag);
        }

        // Finally get the string from the constant pool
        c =  getConstant(i, CONSTANT_Utf8);
        return  ((ConstantUtf8)c).getBytes();
    }


    /**
   * @return Length of constant pool.
   */
    public final int getLength () {
        return  constant_pool_count;
    }


    /**
   * Read one constant from the given file, the type depends on a tag byte.
   *
   * @param file Input stream
   * @return Constant object
   */
    private final Constant readConstant (DataInputStream file)
        throws IOException, ClassFormatError
    {
        byte b =  file.readByte();

        switch (b) {
            case CONSTANT_Class:
                return  new ConstantClass(file);

            case CONSTANT_Fieldref:
                return  new ConstantFieldref(file);

            case CONSTANT_Methodref:
                return  new ConstantMethodref(file);

            case CONSTANT_InterfaceMethodref:
                return  new ConstantInterfaceMethodref(file);

            case CONSTANT_String:
                return  new ConstantString(file);

            case CONSTANT_Integer:
                return  new ConstantInteger(file);

            case CONSTANT_Float:
                return  new ConstantFloat(file);

            case CONSTANT_Long:
                return  new ConstantLong(file);

            case CONSTANT_Double:
                return  new ConstantDouble(file);

            case CONSTANT_NameAndType:
                return  new ConstantNameAndType(file);

            case CONSTANT_Utf8:
                return  new ConstantUtf8(file);

            case CONSTANT_Unicode:
                return  new ConstantUnicode(file);

            default:
                throw  new ClassFormatError("Invalid byte tag in constant pool: " + b);
        }
    }


    /**
   * @param constant Constant to set
   */
    public final void setConstant (int index, Constant constant) {
        constant_pool[index] =  constant;
    }


    /**
   * @param constant_pool
   */
    public final void setConstantPool (Constant[] constant_pool) {
        this.constant_pool =  constant_pool;
        constant_pool_count =  (constant_pool == null)? 0 : constant_pool.length;
    }


    /**
   * @return String representation.
   */
    public final String toString () {
        StringBuffer buf =  new StringBuffer();
        for (int i =  1; i < constant_pool_count; i++) buf.append(i + ")" + constant_pool[i] + "\n");
        return  buf.toString();
    }

}

