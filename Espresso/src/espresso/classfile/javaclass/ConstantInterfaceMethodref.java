package  espresso.classfile.javaclass;




import  espresso.classfile.Constants;
import  java.io.*;




/** 
 * This class represents a constant pool reference to an interface method.
 *
 * @version 980205
 * @author  <A HREF="http://www.inf.fu-berlin.de/~dahm">M. Dahm</A>
 */
public final class ConstantInterfaceMethodref
    extends ConstantCP
{

    /**
   * Empty constructor, all attributes have to be defined via `setXXX'
   * methods. Use at your own risk.
   */
    public ConstantInterfaceMethodref () {
        super(CONSTANT_InterfaceMethodref, 0, 0);
    }


    /**
   * Initialize from another object.
   */
    public ConstantInterfaceMethodref (ConstantInterfaceMethodref c) {
        super(CONSTANT_InterfaceMethodref, c.getClassIndex(), c.getNameAndTypeIndex());
    }


    /**
   * Initialize instance from file data.
   *
   * @param file input stream
   * @throw IOException
   */
    ConstantInterfaceMethodref (DataInputStream file)
        throws IOException
    {
        super(CONSTANT_InterfaceMethodref, file);
    }


    /**
   * @param class_index Reference to the class containing the method
   * @param name_and_type_index and the method signature
   */
    public ConstantInterfaceMethodref (int class_index, int name_and_type_index) {
        super(CONSTANT_InterfaceMethodref, class_index, name_and_type_index);
    }


    /**
   * Called by objects that are traversing the nodes of the tree implicitely
   * defined by the contents of a Java class. I.e., the hierarchy of methods,
   * fields, attributes, etc. spawns a tree of objects.
   *
   * @param v Visitor object
   */
    public void accept (Visitor v) {
        v.visitConstantInterfaceMethodref(this);
    }

}

