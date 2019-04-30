package  espresso.classfile.classgen;




/** 
 * DCMPL - Compare doubles: value1 < value2
 * Stack: ..., value1.word1, value1.word2, value2.word1, value2.word2 ->
 *        ..., result
 *
 * @version 971114
 * @author  <A HREF="http://www.inf.fu-berlin.de/~dahm">M. Dahm</A>
 */
public class DCMPL
    extends Instruction
{

    public DCMPL () {
        super(DCMPL, (short)1);
    }

}

