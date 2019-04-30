package  espresso.classfile.classgen;




/** 
 * GETSTATIC - Fetch static field from class
 * Stack: ..., -> ..., value
 * OR
 * Stack: ..., -> ..., value.word1, value.word2
 *
 * @version 971115
 * @author  <A HREF="http://www.inf.fu-berlin.de/~dahm">M. Dahm</A>
 */
public class GETSTATIC
    extends CPInstruction
{

    /**
   * Empty constructor needed for the Class.newInstance() statement in
   * Instruction.readInstruction(). Not to be used otherwise.
   */
    GETSTATIC () {

    }


    public GETSTATIC (int index) {
        super(GETSTATIC, index);
    }

}

