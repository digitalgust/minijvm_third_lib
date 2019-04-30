package  espresso.classfile.classgen;




/** 
 * ANEWARRAY -  Create new array of references
 * Stack: ..., count -> ..., arrayref
 *
 * @version 971114
 * @author  <A HREF="http://www.inf.fu-berlin.de/~dahm">M. Dahm</A>
 */
public class ANEWARRAY
    extends CPInstruction
{

    /**
   * Empty constructor needed for the Class.newInstance() statement in
   * Instruction.readInstruction(). Not to be used otherwise.
   */
    ANEWARRAY () {

    }


    public ANEWARRAY (int index) {
        super(ANEWARRAY, index);
    }

}

