package  espresso.classfile.javaclass;




/**
 * Interface to make the use of a visitor pattern programming style possible.
 * I.e. a class that implements this interface can traverse the contents of
 * a Java class just by calling the `accept' method which all classes have.
 *
 * Implemented by wish of 
 * <A HREF="http://www.inf.fu-berlin.de/~bokowski">Boris Bokowski</A>.
 *
 * If don't like it, blame him. If you do like it thank me 8-)
 *
 * @version 970819
 * @author  <A HREF="http://www.inf.fu-berlin.de/~dahm">M. Dahm</A>
 */
public interface Visitor {

    ////////////////// In short form //////////////////////
    //   // General classes
    //   public void visit(JavaClass obj);
    //   public void visit(ConstantPool obj);
    //   public void visit(Field obj);
    //   public void visit(Method obj);
    //   // Constants
    //   public void visit(ConstantClass obj);
    //   public void visit(ConstantDouble obj);
    //   public void visit(ConstantFieldref obj);
    //   public void visit(ConstantFloat obj);
    //   public void visit(ConstantInteger obj);
    //   public void visit(ConstantInterfaceMethodref obj);
    //   public void visit(ConstantLong obj);
    //   public void visit(ConstantMethodref obj);
    //   public void visit(ConstantNameAndType obj);
    //   public void visit(ConstantString obj);
    //   public void visit(ConstantUnicode obj);
    //   public void visit(ConstantUtf8 obj);
    //   // Attributes
    //   public void visit(Code obj);
    //   public void visit(ConstantValue obj);
    //   public void visit(ExceptionTable obj);
    //   public void visit(InnerClasses obj);
    //   public void visit(LineNumberTable obj);
    //   public void visit(LocalVariableTable obj);
    //   public void visit(SourceFile obj);
    //   public void visit(Synthetic obj);
    //   public void visit(Deprecated obj);
    //   public void visit(Unknown obj);
    //   // Extra classes (i.e. leaves in this context)
    //   public void visit(InnerClass obj);
    //   public void visit(LocalVariable obj);
    //   public void visit(LineNumber obj);
    //   public void visit(CodeException obj);
    // Attributes
    public void visitCode (Code obj);


    public void visitCodeException (CodeException obj);


    // Constants
    public void visitConstantClass (ConstantClass obj);


    public void visitConstantDouble (ConstantDouble obj);


    public void visitConstantFieldref (ConstantFieldref obj);


    public void visitConstantFloat (ConstantFloat obj);


    public void visitConstantInteger (ConstantInteger obj);


    public void visitConstantInterfaceMethodref (ConstantInterfaceMethodref obj);


    public void visitConstantLong (ConstantLong obj);


    public void visitConstantMethodref (ConstantMethodref obj);


    public void visitConstantNameAndType (ConstantNameAndType obj);


    public void visitConstantPool (ConstantPool obj);


    public void visitConstantString (ConstantString obj);


    public void visitConstantUnicode (ConstantUnicode obj);


    public void visitConstantUtf8 (ConstantUtf8 obj);


    public void visitConstantValue (ConstantValue obj);


    public void visitDeprecated (Deprecated obj);


    public void visitExceptionTable (ExceptionTable obj);


    public void visitField (Field obj);


    // Extra classes (i.e. leaves in this context)
    public void visitInnerClass (InnerClass obj);


    public void visitInnerClasses (InnerClasses obj);


    // General classes
    public void visitJavaClass (JavaClass obj);


    public void visitLineNumber (LineNumber obj);


    public void visitLineNumberTable (LineNumberTable obj);


    public void visitLocalVariable (LocalVariable obj);


    public void visitLocalVariableTable (LocalVariableTable obj);


    public void visitMethod (Method obj);


    public void visitSourceFile (SourceFile obj);


    public void visitSynthetic (Synthetic obj);


    public void visitUnknown (Unknown obj);

}

