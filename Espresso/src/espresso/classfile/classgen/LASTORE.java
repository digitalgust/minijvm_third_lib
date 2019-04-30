package  espresso.classfile.classgen;




/** 
 * LASTORE -  Store into long array
 * Stack: ..., arrayref, index, value.word1, value.word2 -> ...
 *
 * @version 971124
 * @author  <A HREF="http://www.inf.fu-berlin.de/~dahm">M. Dahm</A>
 */
public class LASTORE
    extends Instruction
{

    public LASTORE () {
        super(LASTORE, (short)1);
    }

}

