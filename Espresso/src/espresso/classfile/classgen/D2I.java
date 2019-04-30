package  espresso.classfile.classgen;




/** 
 * D2I - Convert double to int
 * Stack: ..., value.word1, value.word2 -> ..., result
 *
 * @version 971114
 * @author  <A HREF="http://www.inf.fu-berlin.de/~dahm">M. Dahm</A>
 */
public class D2I
    extends Instruction
{

    public D2I () {
        super(D2I, (short)1);
    }

}

