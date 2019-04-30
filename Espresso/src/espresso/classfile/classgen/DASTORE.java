package  espresso.classfile.classgen;




/** 
 * DASTORE -  Store into double array
 * Stack: ..., arrayref, index, value.word1, value.word2 -> ...
 *
 * @version 971114
 * @author  <A HREF="http://www.inf.fu-berlin.de/~dahm">M. Dahm</A>
 */
public class DASTORE
    extends Instruction
{

    public DASTORE () {
        super(DASTORE, (short)1);
    }

}

