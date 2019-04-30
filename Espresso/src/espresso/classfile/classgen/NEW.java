package  espresso.classfile.classgen;




import  java.io.*;




/** 
 * NEW - Create new object
 * Stack: ... -> ..., objectref
 *
 * @version 971204
 * @author  <A HREF="http://www.inf.fu-berlin.de/~dahm">M. Dahm</A>
 */
public class NEW
    extends CPInstruction
{

    /**
   * Empty constructor needed for the Class.newInstance() statement in
   * Instruction.readInstruction(). Not to be used otherwise.
   */
    NEW () {

    }


    public NEW (int index) {
        super(NEW, index);
    }

}

