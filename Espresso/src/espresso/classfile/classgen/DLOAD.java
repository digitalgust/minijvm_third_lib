package  espresso.classfile.classgen;




/** 
 * DLOAD - Load double from local variable
 * Stack ... -> ..., result.word1, result.word2
 *
 * @version 971114
 * @author  <A HREF="http://www.inf.fu-berlin.de/~dahm">M. Dahm</A>
 */
public class DLOAD
    extends LocalVariableInstruction
{

    /**
   * Empty constructor needed for the Class.newInstance() statement in
   * Instruction.readInstruction(). Not to be used otherwise.
   */
    DLOAD () {

    }


    public DLOAD (int n) {
        super(DLOAD, DLOAD_0, n);
    }

}

