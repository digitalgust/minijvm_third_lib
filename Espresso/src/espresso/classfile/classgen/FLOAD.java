package  espresso.classfile.classgen;




/** 
 * FLOAD - Load float from local variable
 * Stack ... -> ..., result
 *
 * @version 971114
 * @author  <A HREF="http://www.inf.fu-berlin.de/~dahm">M. Dahm</A>
 */
public class FLOAD
    extends LocalVariableInstruction
{

    /**
   * Empty constructor needed for the Class.newInstance() statement in
   * Instruction.readInstruction(). Not to be used otherwise.
   */
    FLOAD () {

    }


    public FLOAD (int n) {
        super(FLOAD, FLOAD_0, n);
    }

}

