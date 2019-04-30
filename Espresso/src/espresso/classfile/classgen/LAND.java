package  espresso.classfile.classgen;




/** 
 * LAND - Bitwise AND longs
 * Stack: ..., value1.word1, value1.word2, value2.word1, value2.word2 ->
 *        ..., result.word1, result.word2
 *
 * @version 971115
 * @author  <A HREF="http://www.inf.fu-berlin.de/~dahm">M. Dahm</A>
 */
public class LAND
    extends Instruction
{

    public LAND () {
        super(LAND, (short)1);
    }

}

