package  espresso.classfile.classgen;




import  espresso.classfile.Constants;
import  espresso.classfile.javaclass.*;
import  java.util.Vector;




/** 
 * Template class for building up a java class. May be initialized by an
 * existing java class (file).
 *
 * @see JavaClass
 * @version 980114
 * @author  <A HREF="http://www.inf.fu-berlin.de/~dahm">M. Dahm</A>
 */
public final class ClassGen
    implements Constants
{

    /* Corresponds to the fields found in a JavaClass object.
   */
    private String class_name, super_class_name, file_name;
    private int class_name_index, superclass_name_index;
    private int access_flags;
    private ConstantPoolGen cp; // Template for building up constant pool
    // Vectors instead of arrays to gather fields, methods, etc.
    private Vector field_vec =  new Vector();
    private Vector method_vec =  new Vector();
    private Vector attribute_vec =  new Vector();
    private Vector interface_vec =  new Vector();


    /**
   * @param class_name fully qualified class name
   * @param super_class_name fully qualified superclass name
   * @param file_name source file name
   * @param access_flags access qualifiers
   * @param interfaces implemented interfaces
   */
    public ClassGen (String class_name, String super_class_name, String file_name, int access_flags, String[] interfaces) {
        this.class_name =  class_name;
        this.super_class_name =  super_class_name;
        this.file_name =  file_name;
        this.access_flags =  access_flags;
        cp =  new ConstantPoolGen(); // Create empty constant pool
        // Put everything needed by default into the constant pool and the vectors
        addAttribute(new SourceFile(cp.addUtf8("SourceFile"), 2, cp.addUtf8(file_name), cp.getConstantPool()));
        class_name_index =  cp.addClass(class_name);
        superclass_name_index =  cp.addClass(super_class_name);
        if (interfaces != null) for (int i =  0; i < interfaces.length; i++) addInterface(interfaces[i]);
    }


    /**
   * Initialize with existing class.
   * @param clazz JavaClass object (e.g. read from file)
   */
    public ClassGen (JavaClass clazz) {
        class_name_index =  clazz.getClassNameIndex();
        superclass_name_index =  clazz.getSuperclassNameIndex();
        class_name =  clazz.getClassName();
        super_class_name =  clazz.getSuperclassName();
        file_name =  clazz.getSourceFileName();
        access_flags =  clazz.getAccessFlags();
        cp =  new ConstantPoolGen(clazz.getConstantPool());
        Attribute[] attributes =  clazz.getAttributes();
        Method[] methods =  clazz.getMethods();
        Field[] fields =  clazz.getFields();
        int[] interfaces =  clazz.getInterfaces();
        if (interfaces != null) for (int i =  0; i < interfaces.length; i++) addInterface(interfaces[i]);
        if (attributes != null) for (int i =  0; i < attributes.length; i++) addAttribute(attributes[i]);
        if (methods != null) for (int i =  0; i < methods.length; i++) addMethod(methods[i]);
        if (fields != null) for (int i =  0; i < fields.length; i++) addField(fields[i]);
    }


    /**
   * @return the (finally) built up Java class object.
   */
    public JavaClass getJavaClass () {
        // Copy all vector data into arrays
        Attribute[] attributes =  new Attribute[attribute_vec.size()];
        attribute_vec.copyInto(attributes);
        Method[] methods =  new Method[method_vec.size()];
        method_vec.copyInto(methods);
        Field[] fields =  new Field[field_vec.size()];
        field_vec.copyInto(fields);
        int size =  interface_vec.size();
        int[] interfaces =  new int[size];
        for (int i =  0; i < size; i++) interfaces[i] =  ((Integer)interface_vec.elementAt(i)).intValue();
        return  new JavaClass(class_name_index, superclass_name_index, file_name, MAJOR, MINOR, access_flags, cp.getFinalConstantPool(), interfaces, fields, methods, attributes);
    }


    /**
   * Add an interface to this class, i.e. this class has to implement it.
   * @param i interface to implement (fully qualified class name)
   */
    public final void addInterface (String i) {
        addInterface(cp.addClass(i));
    }


    /**
   * Add an interface to this class, i.e. this class has to implement it.
   * @param i interface to implement (index in constant pool)
   */
    private final void addInterface (int i) {
        interface_vec.addElement(new Integer(i)); // Has to be wrapped in Integer object
    }


    /**
   * Add an attribute to this class.
   * @param a attribute to add
   */
    public final void addAttribute (Attribute a) {
        attribute_vec.addElement(a);
    }


    /**
   * Add a method to this class.
   * @param m method to add
   */
    public final void addMethod (Method m) {
        method_vec.addElement(m);
    }


    /**
   * Add a field to this class.
   * @param f field to add
   */
    public final void addField (Field f) {
        field_vec.addElement(f);
    }


    /**
   * Remove an attribute from this class.
   * @param a attribute to remove
   */
    public final void removeAttribute (Attribute a) {
        attribute_vec.removeElement(a);
    }


    /**
   * Remove a method from this class.
   * @param m method to remove
   */
    public final void removeMethod (Method m) {
        method_vec.removeElement(m);
    }


    /**
   * Remove a field to this class.
   * @param f field to remove
   */
    public final void removeField (Field f) {
        field_vec.removeElement(f);
    }


    public String getClassName () {
        return  class_name;
    }


    public String getSuperclassName () {
        return  super_class_name;
    }


    public String getFileName () {
        return  file_name;
    }


    public int getAccessFlags () {
        return  access_flags;
    }


    public ConstantPoolGen getConstantPool () {
        return  cp;
    }

}

