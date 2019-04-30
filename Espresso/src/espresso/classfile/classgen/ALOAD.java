package  espresso.classfile.classgen;




/** 
 * ALOAD - Load reference from local variable
 * Stack ... -> ..., objectref
 *
 * @version 971114
 * @author  <A HREF="http://www.inf.fu-berlin.de/~dahm">M. Dahm</A>
 */
public class ALOAD
    extends LocalVariableInstruction
{

    /**
   * Empty constructor needed for the Class.newInstance() statement in
   * Instruction.readInstruction(). Not to be used otherwise.
   */
    ALOAD () {

    }


    public ALOAD (int n) {
        super(ALOAD, ALOAD_0, n);
    }

}

