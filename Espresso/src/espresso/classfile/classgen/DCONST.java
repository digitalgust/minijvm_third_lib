package  espresso.classfile.classgen;




/** 
 * DCONST - Push 0.0 or 1.0, other values cause an exception
 *
 * Stack: ... -> ..., <i>
 *
 * @version 971120
 * @author  <A HREF="http://www.inf.fu-berlin.de/~dahm">M. Dahm</A>
 */
public class DCONST
    extends Instruction
{

    /**
   * Empty constructor needed for the Class.newInstance() statement in
   * Instruction.readInstruction(). Not to be used otherwise.
   */
    DCONST () {

    }


    public DCONST (double f) {
        super(DCONST_0, (short)1);
        if (f == 0.0) tag =  DCONST_0; else if (f == 1.0) tag =  DCONST_1; else throw  new ClassGenException("DCONST can be used only for 0.0 and 1.0: " + f);
    }

}

