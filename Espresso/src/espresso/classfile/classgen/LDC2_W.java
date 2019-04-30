package  espresso.classfile.classgen;




/** 
 * LDC2_W - Push long or double from constant pool
 *
 * Stack: ... -> ..., item.word1, item.word2
 *
 * @version 971120
 * @author  <A HREF="http://www.inf.fu-berlin.de/~dahm">M. Dahm</A>
 */
public class LDC2_W
    extends CPInstruction
{

    /**
   * Empty constructor needed for the Class.newInstance() statement in
   * Instruction.readInstruction(). Not to be used otherwise.
   */
    LDC2_W () {

    }


    public LDC2_W (int index) {
        super(LDC2_W, index);
    }

}

