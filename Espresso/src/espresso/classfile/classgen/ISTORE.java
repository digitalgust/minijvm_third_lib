package  espresso.classfile.classgen;




/** 
 * ISTORE - Store int into local variable
 * Stack ..., value -> ... 
 *
 * @version 971114
 * @author  <A HREF="http://www.inf.fu-berlin.de/~dahm">M. Dahm</A>
 */
public class ISTORE
    extends LocalVariableInstruction
{

    /**
   * Empty constructor needed for the Class.newInstance() statement in
   * Instruction.readInstruction(). Not to be used otherwise.
   */
    ISTORE () {

    }


    public ISTORE (int n) {
        super(ISTORE, ISTORE_0, n);
    }

}

