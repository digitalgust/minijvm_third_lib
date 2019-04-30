package  espresso.classfile.classgen;




/** 
 * DSTORE - Store double into local variable
 * Stack ..., value.word1, value.word2 -> ... 
 *
 * @version 971114
 * @author  <A HREF="http://www.inf.fu-berlin.de/~dahm">M. Dahm</A>
 */
public class DSTORE
    extends LocalVariableInstruction
{

    /**
   * Empty constructor needed for the Class.newInstance() statement in
   * Instruction.readInstruction(). Not to be used otherwise.
   */
    DSTORE () {

    }


    public DSTORE (int n) {
        super(DSTORE, DSTORE_0, n);
    }

}

