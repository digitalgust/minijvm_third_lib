package  espresso.classfile.classgen;




/** 
 * D2L - Convert double to long
 * Stack: ..., value.word1, value.word2 -> ..., result.word1, result.word2
 *
 * @version 971114
 * @author  <A HREF="http://www.inf.fu-berlin.de/~dahm">M. Dahm</A>
 */
public class D2L
    extends Instruction
{

    public D2L () {
        super(D2L, (short)1);
    }

}

