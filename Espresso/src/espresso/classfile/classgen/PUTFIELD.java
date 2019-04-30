package  espresso.classfile.classgen;




/** 
 * PUTFIELD - Put field in object
 * Stack: ..., objectref, value -> ...
 * OR
 * Stack: ..., objectref, value.word1, value.word2 -> ...
 *
 * @version 971115
 * @author  <A HREF="http://www.inf.fu-berlin.de/~dahm">M. Dahm</A>
 */
public class PUTFIELD
    extends CPInstruction
{

    /**
   * Empty constructor needed for the Class.newInstance() statement in
   * Instruction.readInstruction(). Not to be used otherwise.
   */
    PUTFIELD () {

    }


    public PUTFIELD (int index) {
        super(PUTFIELD, index);
    }

}

