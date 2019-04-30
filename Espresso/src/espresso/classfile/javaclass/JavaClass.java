package  espresso.classfile.javaclass;




import  espresso.classfile.Constants;
import  java.io.*;




/**
 * Represents a Java class, i.e. the data structures, constant pool,
 * fields, methods and commands contained in a Java .class file.
 * See <a href="ftp://java.sun.com/docs/specs/">JVM 
 * specification</a> for details.
 *
 * @version 970922
 * @author  <A HREF="http://www.inf.fu-berlin.de/~dahm">M. Dahm</A>
 */
public class JavaClass
    implements Constants
{

    private String file_name; // Self explanatory!
    private String source_file_name =  "<Unknown>";
    private int class_name_index;
    private int superclass_name_index;
    private String class_name;
    private String superclass_name;
    private int major, minor; // Compiler version
    private int access_flags; // Access rights
    private ConstantPool constant_pool; // Constant pool
    private int[] interfaces; // implemented interfaces
    private String[] interface_names;
    private Field[] fields; // Fields, i.e. variables of class
    private Method[] methods; // methods defined in the class
    private Attribute[] attributes; // attributes defined in the class
    static boolean debug =  false; // Debugging on/off
    static char sep =  '/'; // directory separator


    /**
   * Constructor gets all contents as arguments.
   *
   * @param class_name Class name
   * @param superclass_name Superclass name
   * @param file_name File name
   * @param major Major compiler version
   * @param minor Minor compiler version
   * @param access_flags Access rights defined by bit flags
   * @param constant_pool Array of constants
   * @param interfaces Implemented interfaces
   * @param fields Class fields
   * @param methods Class methods
   * @param attributes Class attributes
   */
    public JavaClass (int class_name_index, int superclass_name_index, String file_name, int major, int minor, int access_flags, ConstantPool constant_pool, int[] interfaces, Field[] fields, Method[] methods, Attribute[] attributes) {
        this.class_name_index =  class_name_index;
        this.superclass_name_index =  superclass_name_index;
        this.file_name =  file_name;
        this.major =  major;
        this.minor =  minor;
        this.access_flags =  access_flags;
        this.constant_pool =  constant_pool;
        this.interfaces =  interfaces;
        this.fields =  fields;
        this.methods =  methods;
        this.attributes =  attributes;

        // Get source file name if available
        if (attributes != null) {
            for (int i =  0; i < attributes.length; i++) {
                if (attributes[i] instanceof SourceFile) {
                    source_file_name =  ((SourceFile)attributes[i]).getSourceFileName();
                    break;
                }
            }
        }

        // Get class name and superclass name
        ConstantUtf8 name;
        /* According to the specification the following entries must be of type
     * `ConstantClass' but we check that anyway via the 
     * `ConstPool.getConstant' method.
     */
        class_name =  constant_pool.getConstantString(class_name_index, CONSTANT_Class);
        class_name =  Utility.compactClassName(class_name, false);

        if (superclass_name_index > 0) { // May be zero -> class is java.lang.Object
            superclass_name =  constant_pool.getConstantString(superclass_name_index, CONSTANT_Class);
            superclass_name =  Utility.compactClassName(superclass_name, false);
        } else superclass_name =  "java.lang.Object";

        // Get interface names
        String str;

        if (interfaces != null) {
            interface_names =  new String[interfaces.length];

            for (int i =  0; i < interfaces.length; i++) {
                str =  constant_pool.getConstantString(interfaces[i], CONSTANT_Class);
                interface_names[i] =  Utility.compactClassName(str);
            }
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
        v.visitJavaClass(this);
        //    v.visit(this);
    }


    /* Print debug information depending on `JavaClass.debug'
   */
    static final void Debug (String str) {
        if (debug) System.out.println(str);
    }


    /** 
   * Dump class to a file, this should create a valid .class file.
   *
   * @param file Output file
   * @throw IOException
   */
    public void dump (File file)
        throws IOException
    {
        dump(file.getAbsolutePath());
    }


    /** 
   * Dump class to a file, this should create a valid .class file.
   *
   * @param file_name Output file name
   * @throw IOException
   */
    public void dump (String file_name)
        throws IOException
    {
        DataOutputStream file;
        file =  new DataOutputStream(new BufferedOutputStream(new FileOutputStream(file_name)));
        dump(file);
    }


    /** 
   * Dump Java class to output stream in binary format.
   *
   * @param file Output stream
   * @throw IOException
   */
    public void dump (DataOutputStream file)
        throws IOException
    {
        file.writeInt(0xcafebabe);
        file.writeShort(minor);
        file.writeShort(major);
        constant_pool.dump(file);
        file.writeShort(access_flags);
        file.writeShort(class_name_index);
        file.writeShort(superclass_name_index);
        file.writeShort(interfaces.length);
        for (int i =  0; i < interfaces.length; i++) file.writeShort(interfaces[i]);
        file.writeShort(fields.length);
        for (int i =  0; i < fields.length; i++) fields[i].dump(file);
        file.writeShort(methods.length);
        for (int i =  0; i < methods.length; i++) methods[i].dump(file);

        if (attributes != null) {
            file.writeShort(attributes.length);
            for (int i =  0; i < attributes.length; i++) attributes[i].dump(file);
        } else file.writeShort(0);

        file.close();
    }


    /**
   * @return Access rights of class.
   */
    public final int getAccessFlags () {
        return  access_flags;
    }


    /**
   * @return Attributes of the class.
   */
    public final Attribute[] getAttributes () {
        return  attributes;
    }


    /**
   * @return Class name.
   */
    public final String getClassName () {
        return  class_name;
    }


    /**
   * @return Class name index.
   */
    public final int getClassNameIndex () {
        return  class_name_index;
    }


    /**
   * @return Constant pool.
   */
    public final ConstantPool getConstantPool () {
        return  constant_pool;
    }


    /**
   * @return Fields, i.e. variables of the class.
   */
    public final Field[] getFields () {
        return  fields;
    }


    /**
   * @return File name.
   */
    public final String getFileName () {
        return  file_name;
    }


    /**
   * @return Names of implemented interfaces.
   */
    public final String[] getInterfaceNames () {
        return  interface_names;
    }


    /**
   * @return Implemented interfaces.
   */
    public final int[] getInterfaces () {
        return  interfaces;
    }


    /**
   * @return Major number of compiler version.
   */
    public final int getMajor () {
        return  major;
    }


    /**
   * @return Methods of the class.
   */
    public final Method[] getMethods () {
        return  methods;
    }


    /**
   * @return Minor number of compiler version.
   */
    public final int getMinor () {
        return  minor;
    }


    /**
   * @return File name of source.
   */
    public final String getSourceFileName () {
        return  source_file_name;
    }


    /**
   * @return Superclass name.
   */
    public final String getSuperclassName () {
        return  superclass_name;
    }


    /**
   * @return Class name index.
   */
    public final int getSuperclassNameIndex () {
        return  superclass_name_index;
    }


    /** Initialize the package.
   */
    public static void initJavaClass () {
        // Debugging ... on/off
        String debug =  System.getProperty("JavaClass.debug");
        if (debug != null) JavaClass.debug =  new Boolean(debug).booleanValue();
        // Get path separator either / or \ usually
        String sep =  System.getProperty("file.separator");

        if (sep != null) try {
            JavaClass.sep =  sep.charAt(0);
        } catch (StringIndexOutOfBoundsException e) {

        } // Never reached
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
    }


    /**
   * @param class_name.
   */
    public final void setClassName (String class_name) {
        this.class_name =  class_name;
    }


    /**
   * @param class_name_index.
   */
    public final void setClassNameIndex (int class_name_index) {
        this.class_name_index =  class_name_index;
    }


    /**
   * @param constant_pool.
   */
    public final void setConstantPool (ConstantPool constant_pool) {
        this.constant_pool =  constant_pool;
    }


    /**
   * @param fields.
   */
    public final void setFields (Field[] fields) {
        this.fields =  fields;
    }


    /**
   * @param file_name.
   */
    public final void setFileName (String file_name) {
        this.file_name =  file_name;
    }


    /**
   * @param interface_names.
   */
    public final void setInterfaceNames (String[] interface_names) {
        this.interface_names =  interface_names;
    }


    /**
   * @param interfaces.
   */
    public final void setInterfaces (int[] interfaces) {
        this.interfaces =  interfaces;
    }


    /**
   * @param major.
   */
    public final void setMajor (int major) {
        this.major =  major;
    }


    /**
   * @param methods.
   */
    public final void setMethods (Method[] methods) {
        this.methods =  methods;
    }


    /**
   * @param minor.
   */
    public final void setMinor (int minor) {
        this.minor =  minor;
    }


    /**
   * @param source_file_name.
   */
    public final void setSourceFileName (String source_file_name) {
        this.source_file_name =  source_file_name;
    }


    /**
   * @param superclass_name.
   */
    public final void setSuperclassName (String superclass_name) {
        this.superclass_name =  superclass_name;
    }


    /**
   * @param superclass_name_index.
   */
    public final void setSuperclassNameIndex (int superclass_name_index) {
        this.superclass_name_index =  superclass_name_index;
    }


    /**
   * @return String representing class contents.
   */
    public String toString () {
        StringBuffer buf =  new StringBuffer();
        String access =  Utility.accessToString(access_flags, true);
        buf.append(access + " class " + class_name + " extends " + Utility.compactClassName(superclass_name) + '\n');
        int size =  interfaces.length;

        if (size > 0) {
            buf.append("implements\t\t");

            for (int i =  0; i < size; i++) {
                buf.append(interface_names[i]);
                if (i < size - 1) buf.append(", ");
            }

            buf.append('\n');
        }

        buf.append("filename\t\t" + file_name + '\n');
        buf.append("compiled from\t\t" + source_file_name + '\n');
        buf.append("compiler version\t" + major + "." + minor + '\n');
        buf.append("access flags\t\t" + access_flags + '\n');
        buf.append("constant pool\t\t" + constant_pool.getLength() + " entries\n");
        buf.append(attributes.length + " attributes:\n");
        for (int i =  0; i < attributes.length; i++) buf.append("\t" + attributes[i] + '\n');
        buf.append(fields.length + " fields:\n");
        for (int i =  0; i < fields.length; i++) buf.append("\t" + fields[i] + '\n');
        buf.append(methods.length + " methods:\n");
        for (int i =  0; i < methods.length; i++) buf.append("\t" + methods[i] + '\n');
        buf.append('\n');
        return  buf.toString();
    }

}

