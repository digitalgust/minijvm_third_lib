package  espresso.classfile.classgen;




/** 
 * LCONST - Push 0 or 1, other values cause an exception
 *
 * Stack: ... -> ..., <i>
 *
 * @version 971120
 * @author  <A HREF="http://www.inf.fu-berlin.de/~dahm">M. Dahm</A>
 */
public class LCONST
    extends Instruction
{

    /**
   * Empty constructor needed for the Class.newInstance() statement in
   * Instruction.readInstruction(). Not to be used otherwise.
   */
    LCONST () {

    }


    public LCONST (long f) {
        super(LCONST_0, (short)1);
        if (f == 0) tag =  LCONST_0; else if (f == 1) tag =  LCONST_1; else throw  new ClassGenException("LCONST can be used only for 0 and 1: " + f);
    }

}

