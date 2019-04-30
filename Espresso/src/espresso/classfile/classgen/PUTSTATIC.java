package  espresso.classfile.classgen;




/** 
 * PUTSTATIC - Put static field in class
 * Stack: ..., objectref, value -> ...
 * OR
 * Stack: ..., objectref, value.word1, value.word2 -> ...
 *
 * @version 971115
 * @author  <A HREF="http://www.inf.fu-berlin.de/~dahm">M. Dahm</A>
 */
public class PUTSTATIC
    extends CPInstruction
{

    /**
   * Empty constructor needed for the Class.newInstance() statement in
   * Instruction.readInstruction(). Not to be used otherwise.
   */
    PUTSTATIC () {

    }


    public PUTSTATIC (int index) {
        super(PUTSTATIC, index);
    }

}

