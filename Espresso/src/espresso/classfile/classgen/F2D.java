package  espresso.classfile.classgen;




/** 
 * F2D - Convert float to double
 * Stack: ..., value -> ..., result.word1, result.word2
 *
 * @version 971115
 * @author  <A HREF="http://www.inf.fu-berlin.de/~dahm">M. Dahm</A>
 */
public class F2D
    extends Instruction
{

    public F2D () {
        super(F2D, (short)1);
    }

}

