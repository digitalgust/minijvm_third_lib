package  espresso.classfile.classgen;




/** 
 * ILOAD - Load int from local variable
 * Stack ... -> ..., result
 *
 * @version 971120
 * @author  <A HREF="http://www.inf.fu-berlin.de/~dahm">M. Dahm</A>
 */
public class ILOAD
    extends LocalVariableInstruction
{

    /**
   * Empty constructor needed for the Class.newInstance() statement in
   * Instruction.readInstruction(). Not to be used otherwise.
   */
    ILOAD () {

    }


    public ILOAD (int n) {
        super(ILOAD, ILOAD_0, n);
    }

}

