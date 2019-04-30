package  espresso.classfile.classgen;




/** 
 * F2L - Convert float to long
 * Stack: ..., value -> ..., result.word1, result.word2
 *
 * @version 971115
 * @author  <A HREF="http://www.inf.fu-berlin.de/~dahm">M. Dahm</A>
 */
public class F2L
    extends Instruction
{

    public F2L () {
        super(F2L, (short)1);
    }

}

