package  espresso.classfile.classgen;




/** 
 * CHECKCAST - Check whether object is of given type
 * Stack: ..., objectref -> ..., objectref
 *
 * @version 971114
 * @author  <A HREF="http://www.inf.fu-berlin.de/~dahm">M. Dahm</A>
 */
public class CHECKCAST
    extends CPInstruction
{

    /**
   * Empty constructor needed for the Class.newInstance() statement in
   * Instruction.readInstruction(). Not to be used otherwise.
   */
    CHECKCAST () {

    }


    public CHECKCAST (int index) {
        super(CHECKCAST, index);
    }

}

