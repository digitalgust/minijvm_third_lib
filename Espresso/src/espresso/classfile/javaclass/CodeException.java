package  espresso.classfile.javaclass;




import  espresso.classfile.Constants;
import  java.io.*;




/**
 * This class represents an entry in the exception table of the <em>Code</em>
 * attribute and is used only there. It contains a range in which a
 * particular exception handler is active.
 *
 * @version 970922
 * @author  <A HREF="http://www.inf.fu-berlin.de/~dahm">M. Dahm</A>
 * @see     Code
 */
public final class CodeException {

    private int start_pc; // Range in the code the exception handler is
    private int end_pc; // active. start_pc is inclusive, end_pc exclusive
    private int handler_pc;
    /* Starting address of exception handler, i.e.
			   * an offset from start of code.
			   */
    private int catch_type;


    /* If this is zero the handler catches any
			   * exception, otherwise it points to the
			   * exception class which is to be caught.
			   */
    /**
   * Empty constructor, all attributes have to be defined via `setXXX'
   * methods. Use at your own risk.
   */
    public CodeException () {

    }


    /**
   * Initialize from another object.
   */
    public CodeException (CodeException c) {
        this(c.getStartPC(), c.getEndPC(), c.getHandlerPC(), c.getCatchType());
    }


    /**
   * Construct object from file stream.
   * @param file Input stream
   * @throw IOException
   */
    CodeException (DataInputStream file)
        throws IOException
    {
        this(file.readUnsignedShort(), file.readUnsignedShort(), file.readUnsignedShort(), file.readUnsignedShort());
    }


    /**
   * @param start_pc Range in the code the exception handler is active,
   * start_pc is inclusive while
   * @param end_pc is exclusive
   * @param handler_pc Starting address of exception handler, i.e.
   * an offset from start of code.
   * @param catch_type If zero the handler catches any 
   * exception, otherwise it points to the exception class which is 
   * to be caught.
   */
    public CodeException (int start_pc, int end_pc, int handler_pc, int catch_type) {
        this.start_pc =  start_pc;
        this.end_pc =  end_pc;
        this.handler_pc =  handler_pc;
        this.catch_type =  catch_type;
    }


    /**
   * Called by objects that are traversing the nodes of the tree implicitely
   * defined by the contents of a Java class. I.e., the hierarchy of methods,
   * fields, attributes, etc. spawns a tree of objects.
   *
   * @param v Visitor object
   */
    public void accept (Visitor v) {
        v.visitCodeException(this);
    }


    /**
   * Dump code exception to file stream in binary format.
   *
   * @param file Output file stream
   * @throw IOException
   */
    public final void dump (DataOutputStream file)
        throws IOException
    {
        file.writeShort(start_pc);
        file.writeShort(end_pc);
        file.writeShort(handler_pc);
        file.writeShort(catch_type);
    }


    /**
   * @return Zero, if the handler catches any exception, otherwise it points to
   * the exception class which is to be caught.
   */
    public final int getCatchType () {
        return  catch_type;
    }


    /**
   * @return Exclusive end index of the region where the handler is active.
   */
    public final int getEndPC () {
        return  end_pc;
    }


    /**
   * @return Starting address of exception handler, relative to the code.
   */
    public final int getHandlerPC () {
        return  handler_pc;
    }


    /**
   * @return Inclusive start index of the region where the handler is active.
   */
    public final int getStartPC () {
        return  start_pc;
    }


    /**
   * @param catch_type.
   */
    public final void setCatchType (int catch_type) {
        this.catch_type =  catch_type;
    }


    /**
   * @param end_pc.
   */
    public final void setEndPC (int end_pc) {
        this.end_pc =  end_pc;
    }


    /**
   * @param handler_pc.
   */
    public final void setHandlerPC (int handler_pc) {
        this.handler_pc =  handler_pc;
    }


    /**
   * @param start_pc.
   */
    public final void setStartPC (int start_pc) {
        this.start_pc =  start_pc;
    }


    /**
   * @return String representation.
   */
    public final String toString () {
        return  "CodeException(start_pc = " + start_pc + ", end_pc = " + end_pc + ", handler_pc = " + handler_pc + ", catch_type = " + catch_type + ")";
    }

}

