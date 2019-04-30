package  espresso.classfile.classgen;




import  espresso.classfile.Constants;
import  espresso.classfile.javaclass.*;
import  java.io.*;




/** 
 * Represents a basic or reference type (int, short, ..., objectref, arrayref)
 *
 * @version 971208
 * @author  <A HREF="http://www.inf.fu-berlin.de/~dahm">M. Dahm</A>
 * @see     Instruction
 */
public final class ClassGenType
    implements Constants
{

    private byte type;
    private String name; // signature for the type
    private int dimensions =  -1; // Applies for T_ARRAY only
    private byte basic_type =  -1;


    /**
   * Constructor for basic types such as int, long, `void'
   *
   * @param type one of T_INT, T_BOOLEAN, ..., T_VOID
   */
    public ClassGenType (byte type) {
        if ((type < T_BOOLEAN) || (type > T_VOID)) throw  new ClassGenException("Invalid type: " + type);
        this.type =  type;
        name =  SHORT_TYPE_NAMES[type];
    }


    /**
   * Constructor for reference type, e.g. java.lang.String
   *
   * @param class_name String for complete class name
   */
    public ClassGenType (String class_name) {
        type =  T_REFERENCE;
        name =  "L" + class_name.replace('.', '/') + ";";
    }


    /**
   * Constructor for array type, e.g. int[]
   *
   * @param type array type, e.g. T_INT
   */
    public ClassGenType (byte type, int dimensions) {
        this(type);
        if ((dimensions < 1) || (dimensions > MAX_BYTE)) throw  new ClassGenException("Invalid number of dimensions: " + dimensions);
        if (type == T_VOID) throw  new ClassGenException("Invalid void[]");
        this.dimensions =  dimensions;
        this.type =  T_ARRAY;
        basic_type =  type;
        StringBuffer buf =  new StringBuffer();
        for (int i =  0; i < dimensions; i++) buf.append('[');
        buf.append(name); // name is set in this(type)
        name =  buf.toString();
    }


    /**
   * Constructor for reference array type, e.g. Object[]
   *
   * @param class_name complete name of class (java.lang.String, e.g.)
   */
    public ClassGenType (String class_name, int dimensions) {
        this(class_name);
        if ((dimensions < 1) || (dimensions > MAX_BYTE)) throw  new ClassGenException("Invalid number of dimensions: " + dimensions);
        this.dimensions =  dimensions;
        this.type =  T_ARRAY;
        basic_type =  T_REFERENCE;
        StringBuffer buf =  new StringBuffer();
        for (int i =  0; i < dimensions; i++) buf.append('[');
        buf.append(name); // name is set in this(class_name)
        name =  buf.toString();
    }


    /**
   * Constructor for array of given type
   *
   * @param type type of array (may be an array itself)
   */
    public ClassGenType (ClassGenType type, int dimensions) {
        this.type =  T_ARRAY;

        if (type.type == T_ARRAY) {
            this.dimensions =  dimensions + type.dimensions;
            basic_type =  type.basic_type;
        } else {
            this.dimensions =  dimensions;
            basic_type =  type.type;
        }

        if ((dimensions < 1) || (this.dimensions > MAX_BYTE)) throw  new ClassGenException("Invalid number of dimensions: " + dimensions);
        StringBuffer buf =  new StringBuffer();
        for (int i =  0; i < dimensions; i++) buf.append('[');
        buf.append(type.name);
        name =  buf.toString();
    }


    /**
   * @return signature for given type.
   */
    public String getSignature () {
        return  name;
    }


    /**
   * @return ClassGenType string, e.g. `int[]'
   */
    public String toString () {
        return  Utility.signatureToString(name);
    }


    /**
   * Convert type to Java method signature, e.g. int[] f(java.lang.String x)
   * becomes (Ljava/lang/String;)[I
   *
   * @param return_type what the method returns
   * @param arg_types what are the argument types
   * @return method signature for given type(s).
   */
    public static String getMethodSignature (ClassGenType return_type, ClassGenType[] arg_types) {
        StringBuffer buf =  new StringBuffer("(");
        int length =  (arg_types == null)? 0 : arg_types.length;
        for (int i =  0; i < length; i++) buf.append(arg_types[i].getSignature());
        buf.append(')');
        buf.append(return_type.getSignature());
        return  buf.toString();
    }


    private static int consumed_chars =  0;


    private static final ClassGenType getType (String signature)
        throws StringIndexOutOfBoundsException
    {
        byte type =  Utility.typeOfSignature(signature);
        consumed_chars =  1;

        if (type <= T_VOID) return  new ClassGenType(type); else if (type == T_ARRAY) {
            int dim =  0;

            do { // Count dimensions
                consumed_chars++;
                dim++;
            } while (signature.charAt(consumed_chars) == '[');

            ClassGenType t =  getType(signature.substring(consumed_chars));
            return  new ClassGenType(t, dim);
        } else { // type == T_REFERENCE
            int index =  signature.indexOf(';'); // Look for closing `;'
            if (index < 0) throw  new ClassFormatError("Invalid signature: " + signature);
            consumed_chars =  index + 1; // "Lblabla;" `L' and `;' are removed
            return  new ClassGenType(Utility.compactClassName(signature.substring(1, index), false));
        }
    }


    /**
   * Convert arguments of a method (signature) to an array of ClassGenType objects.
   * @param signature signature string such as (Ljava/lang/String;)V
   * @return return type
   */
    public static ClassGenType getReturnType (String signature) {
        try {
            // Read return type after `)'
            int index =  signature.lastIndexOf(')') + 1;
            return  getType(signature.substring(index));
        } catch (StringIndexOutOfBoundsException e) { // Should never occur
            throw  new ClassFormatError("Invalid method signature: " + signature);
        }
    }


    /**
   * Convert arguments of a method (signature) to an array of ClassGenType objects.
   * @param signature signature string such as (Ljava/lang/String;)V
   * @return array of argument types
   */
    public static ClassGenType[] getArgumentTypes (String signature) {
        java.util.Vector vec =  new java.util.Vector();
        int index;
        ClassGenType[] types;

        try { // Read all declarations between for `(' and `)'
            if (signature.charAt(0) != '(') throw  new ClassFormatError("Invalid method signature: " + signature);
            index =  1; // current string position

            while (signature.charAt(index) != ')') {
                vec.addElement(getType(signature.substring(index)));
                index +=  consumed_chars; // update position
            }
        } catch (StringIndexOutOfBoundsException e) { // Should never occur
            throw  new ClassFormatError("Invalid method signature: " + signature);
        }

        types =  new ClassGenType[vec.size()];
        vec.copyInto(types);
        return  types;
    }

}

