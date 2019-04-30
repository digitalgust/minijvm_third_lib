package  espresso.classfile.javaclass;




import  espresso.classfile.Constants;
import  java.io.*;




/**
 * This class represents the method info structure, i.e. the representation 
 * for a method in the class. See JVM specification for details.
 * A method has access flags, a name, a signature and a number of attributes.
 *
 * @version 970922
 * @author  <A HREF="http://www.inf.fu-berlin.de/~dahm">M. Dahm</A>
 */
public final class Method
    implements Constants
{

    private int access_flags; // Access rights of field
    private int name_index; // Points to field name in constant pool 
    private int signature_index; // Points to encoded signature
    private int attributes_count; // No. of attributes
    private Attribute[] attributes; // Collection of attributes
    private ConstantPool constant_pool;


    /**
   * Empty constructor, all attributes have to be defined via `setXXX'
   * methods. Use at your own risk.
   */
    public Method () {

    }


    /**
   * Initialize from another object. Note that both objects use the same
   * references (shallow copy). Use clone() for a physical copy.
   */
    public Method (Method c) {
        this(c.getAccessFlags(), c.getNameIndex(), c.getSignatureIndex(), c.getAttributes(), c.getConstantPool());
    }


    /**
   * Construct object from file stream.
   * @param file Input stream
   * @throw IOException
   * @throw ClassFormatError
   */
    Method (DataInputStream file, ConstantPool constant_pool)
        throws IOException, ClassFormatError
    {
        this(file.readUnsignedShort(), file.readUnsignedShort(), file.readUnsignedShort(), null, constant_pool);
        attributes_count =  (file.readUnsignedShort());
        attributes =  new Attribute[attributes_count];
        for (int i =  0; i < attributes_count; i++) // Implicit recursion
        attributes[i] =  Attribute.readAttribute(file, constant_pool);
    }


    /**
   * @param access_flags Access rights of method
   * @param name_index Points to field name in constant pool
   * @param signature_index Points to encoded signature
   * @param attributes Collection of attributes
   * @param constant_pool Array of constants
   */
    public Method (int access_flags, int name_index, int signature_index, Attribute[] attributes, ConstantPool constant_pool) {
        this.access_flags =  access_flags;
        this.name_index =  name_index;
        this.signature_index =  signature_index;
        this.constant_pool =  constant_pool;
        setAttributes(attributes);
    }


    /**
   * Called by objects that are traversing the nodes of the tree implicitely
   * defined by the contents of a Java class. I.e., the hierarchy of methods,
   * fields, attributes, etc. spawns a tree of objects.
   *
   * @param v Visitor object
   */
    public void accept (Visitor v) {
        v.visitMethod(this);
    }


    /**
   * Dump method to file stream on binary format.
   *
   * @param file Output file stream
   * @throw IOException
   */
    public final void dump (DataOutputStream file)
        throws IOException
    {
        file.writeShort(access_flags);
        file.writeShort(name_index);
        file.writeShort(signature_index);
        file.writeShort(attributes_count);
        for (int i =  0; i < attributes_count; i++) attributes[i].dump(file);
    }


    /** 
   * @return Access flags of the class field.
   */
    public final int getAccessFlags () {
        return  access_flags;
    }


    /**
   * @return Collection of method attributes.
   */
    public final Attribute[] getAttributes () {
        return  attributes;
    }


    /**
   * @return Code attribute of method
   */
    public final Code getCode () {
        for (int i =  0; i < attributes.length; i++) if (attributes[i] instanceof Code) return  (Code)attributes[i];
        return  null;
    }


    /**
   * @return Constant pool used by this object.
   * @see ConstantPool
   */
    public final ConstantPool getConstantPool () {
        return  constant_pool;
    }


    /**
   * @return Name of method
   */
    public final String getName () {
        ConstantUtf8 c;
        c =  (ConstantUtf8)constant_pool.getConstant(name_index, CONSTANT_Utf8);
        return  c.getBytes();
    }


    /**
   * @return Index in constant pool of field name.
   */
    public final int getNameIndex () {
        return  name_index;
    }


    /**
   * @return String representation of method type signature.
   */
    public final String getSignature () {
        ConstantUtf8 c;
        c =  (ConstantUtf8)constant_pool.getConstant(signature_index, CONSTANT_Utf8);
        return  c.getBytes();
    }


    /**
   * @return Index in constant pool of field signature.
   */
    public final int getSignatureIndex () {
        return  signature_index;
    }


    /**
   * @param access_flags.
   */
    public final void setAccessFlags (int access_flags) {
        this.access_flags =  access_flags;
    }


    /**
   * @param attributes.
   */
    public final void setAttributes (Attribute[] attributes) {
        this.attributes =  attributes;
        attributes_count =  (attributes == null)? 0 : attributes.length;
    }


    /**
   * @param constant_pool Constant pool to be used for this object.
   * @see ConstantPool
   */
    public final void setConstantPool (ConstantPool constant_pool) {
        this.constant_pool =  constant_pool;
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
   * Return string representation close to declaration format,
   * `public static int main(String[]) throws IOException', e.g.
   *
   * @return String representation of the method.
   */
    public final String toString () {
        ConstantUtf8 c;
        ConstantValue cv;
        String name, signature, access; // Short cuts to constant pool
        String exceptions;
        StringBuffer buf;
        Attribute[] attr;
        access =  Utility.accessToString(access_flags);
        // Get name and signature from constant pool
        c =  (ConstantUtf8)constant_pool.getConstant(signature_index, CONSTANT_Utf8);
        signature =  c.getBytes();
        c =  (ConstantUtf8)constant_pool.getConstant(name_index, CONSTANT_Utf8);
        name =  c.getBytes();
        signature =  Utility.methodSignatureToString(signature, name, access);
        buf =  new StringBuffer(signature);

        for (int i =  0; i < attributes_count; i++) {
            /* Get only the thrown exceptions (not the line numbers attribute from
       * the `Code' attribute, e.g.)
       */
            if (attributes[i] instanceof ExceptionTable) {
                buf.append("\n\t\tthrows " + attributes[i].toString());
                break;
            }
        }

        return  buf.toString();
    }

}

