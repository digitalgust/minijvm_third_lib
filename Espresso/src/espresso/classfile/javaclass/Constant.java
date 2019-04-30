package  espresso.classfile.javaclass;




import  espresso.classfile.Constants;
import  java.io.*;




/**
 * Abstract superclass for classes to represent the different constant types
 * in the constant pool of a class file. The classes keep closely to
 * the JVM specification.
 *
 * @version 970922
 * @author  <A HREF="http://www.inf.fu-berlin.de/~dahm">M. Dahm</A>
 * @see     ConstantClass
 * @see     ConstantFieldref
 * @see     ConstantMethodref
 * @see     ConstantInterfaceMethodref
 * @see     ConstantString
 * @see     ConstantInteger
 * @see     ConstantFloat
 * @see     ConstantLong
 * @see     ConstantDouble
 * @see     ConstantNameAndType
 * @see     ConstantUtf8
 * @see     ConstantUnicode
 */
public abstract class Constant
    implements Constants
{

    /* In fact this tag is redundant since we can distinguish different
   * `Constant' objects by their type, i.e. via `instanceof'. In some
   * places we will use the tag for switch()es anyway.
   *
   * First, we want match the specification as closely as possible. Second we
   * need the tag as an index to select the corresponding class name from the 
   * `CONSTANT_NAMES' array.
   */
    protected byte tag;


    /**
   * Called by objects that are traversing the nodes of the tree implicitely
   * defined by the contents of a Java class. I.e., the hierarchy of methods,
   * fields, attributes, etc. spawns a tree of objects.
   *
   * @param v Visitor object
   */
    public abstract void accept (Visitor v);


    public abstract void dump (DataOutputStream file)
        throws IOException;


    /**
   * @return Tag of constant, i.e. its type. No setTag() method to avoid
   * confusion.
   */
    public final byte getTag () {
        return  tag;
    }


    /**
   * @return String representation.
   */
    public String toString () {
        return  CONSTANT_NAMES[tag] + "[" + tag + "]";
    }

}

