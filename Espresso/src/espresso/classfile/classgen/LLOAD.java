package  espresso.classfile.classgen;




/** 
 * LLOAD - Load long from local variable
 * Stack ... -> ..., result.word1, result.word2
 *
 * @version 971114
 * @author  <A HREF="http://www.inf.fu-berlin.de/~dahm">M. Dahm</A>
 */
public class LLOAD
    extends LocalVariableInstruction
{

    /**
   * Empty constructor needed for the Class.newInstance() statement in
   * Instruction.readInstruction(). Not to be used otherwise.
   */
    LLOAD () {

    }


    public LLOAD (int n) {
        super(LLOAD, LLOAD_0, n);
    }

}

