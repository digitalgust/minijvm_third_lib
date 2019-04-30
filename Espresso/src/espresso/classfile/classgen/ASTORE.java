package  espresso.classfile.classgen;




/** 
 * ASTORE - Store reference into local variable
 * Stack ..., objectref -> ... 
 *
 * @version 971114
 * @author  <A HREF="http://www.inf.fu-berlin.de/~dahm">M. Dahm</A>
 */
public class ASTORE
    extends LocalVariableInstruction
{

    /**
   * Empty constructor needed for the Class.newInstance() statement in
   * Instruction.readInstruction(). Not to be used otherwise.
   */
    ASTORE () {

    }


    public ASTORE (int n) {
        super(ASTORE, ASTORE_0, n);
    }

}

