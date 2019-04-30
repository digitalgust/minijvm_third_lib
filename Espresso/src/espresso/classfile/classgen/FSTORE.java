package  espresso.classfile.classgen;




/** 
 * FSTORE - Store float into local variable
 * Stack ..., value -> ... 
 *
 * @version 971114
 * @author  <A HREF="http://www.inf.fu-berlin.de/~dahm">M. Dahm</A>
 */
public class FSTORE
    extends LocalVariableInstruction
{

    /**
   * Empty constructor needed for the Class.newInstance() statement in
   * Instruction.readInstruction(). Not to be used otherwise.
   */
    FSTORE () {

    }


    public FSTORE (int n) {
        super(FSTORE, FSTORE_0, n);
    }

}

