package  espresso.classfile.classgen;




/** 
 * DALOAD - Load double from array
 * Stack: ..., arrayref, index -> ..., result.word1, result.word2
 *
 * @version 971114
 * @author  <A HREF="http://www.inf.fu-berlin.de/~dahm">M. Dahm</A>
 */
public class DALOAD
    extends Instruction
{

    public DALOAD () {
        super(DALOAD, (short)1);
    }

}

