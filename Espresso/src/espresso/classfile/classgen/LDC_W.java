package  espresso.classfile.classgen;




/** 
 * LDC_W - Push item from constant pool (wide index)
 *
 * Stack: ... -> ..., item.word1, item.word2
 *
 * @version 971120
 * @author  <A HREF="http://www.inf.fu-berlin.de/~dahm">M. Dahm</A>
 */
public class LDC_W
    extends CPInstruction
{

    /**
   * Empty constructor needed for the Class.newInstance() statement in
   * Instruction.readInstruction(). Not to be used otherwise.
   */
    LDC_W () {

    }


    public LDC_W (int index) {
        super(LDC_W, index);
    }

}

