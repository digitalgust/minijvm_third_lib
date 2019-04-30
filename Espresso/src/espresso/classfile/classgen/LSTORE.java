package  espresso.classfile.classgen;




/** 
 * LSTORE - Store long into local variable
 * Stack ..., value.word1, value.word2 -> ... 
 *
 * @version 971114
 * @author  <A HREF="http://www.inf.fu-berlin.de/~dahm">M. Dahm</A>
 */
public class LSTORE
    extends LocalVariableInstruction
{

    /**
   * Empty constructor needed for the Class.newInstance() statement in
   * Instruction.readInstruction(). Not to be used otherwise.
   */
    LSTORE () {

    }


    public LSTORE (int n) {
        super(LSTORE, LSTORE_0, n);
    }

}

