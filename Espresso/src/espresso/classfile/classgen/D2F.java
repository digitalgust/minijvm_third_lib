package  espresso.classfile.classgen;




/** 
 * D2F - Convert double to float
 * Stack: ..., value.word1, value.word2 -> ..., result
 *
 * @version 971114
 * @author  <A HREF="http://www.inf.fu-berlin.de/~dahm">M. Dahm</A>
 */
public class D2F
    extends Instruction
{

    public D2F () {
        super(D2F, (short)1);
    }

}

