package  espresso.classfile.classgen;




/** 
 * ICONST - Push value between -1, ..., 5, other values cause an exception
 *
 * Stack: ... -> ..., <i>
 *
 * @version 971120
 * @author  <A HREF="http://www.inf.fu-berlin.de/~dahm">M. Dahm</A>
 */
public class ICONST
    extends Instruction
{

    /**
   * Empty constructor needed for the Class.newInstance() statement in
   * Instruction.readInstruction(). Not to be used otherwise.
   */
    ICONST () {

    }


    public ICONST (int i) {
        super(ICONST_0, (short)1);
        if ((i >= -1) && (i <= 5)) tag =  (short)(ICONST_0 + i); // Even works for i == -1
        else throw  new ClassGenException("ICONST can be used only for value between -1 and 5: " + i);
    }

}

