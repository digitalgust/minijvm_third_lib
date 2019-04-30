package  espresso.classfile.classgen;




/** 
 * FCONST - Push 0.0, 1.0 or 2.0, other values cause an exception
 *
 * Stack: ... -> ..., <i>
 *
 * @version 971120
 * @author  <A HREF="http://www.inf.fu-berlin.de/~dahm">M. Dahm</A>
 */
public class FCONST
    extends Instruction
{

    /**
   * Empty constructor needed for the Class.newInstance() statement in
   * Instruction.readInstruction(). Not to be used otherwise.
   */
    FCONST () {

    }


    public FCONST (float f) {
        super(FCONST_0, (short)1);
        if (f == 0.0) tag =  FCONST_0; else if (f == 1.0) tag =  FCONST_1; else if (f == 2.0) tag =  FCONST_2; else throw  new ClassGenException("FCONST can be used only for 0.0, 1.0 and 2.0: " + f);
    }

}

