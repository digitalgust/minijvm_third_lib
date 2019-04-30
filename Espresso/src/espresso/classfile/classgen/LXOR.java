package  espresso.classfile.classgen;




/** 
 * LXOR - Bitwise XOR long
 * Stack: ..., value1.word1, value1.word2, value2.word1, value2.word2 -> ..., result
 *
 * @version 971119
 * @authXOR  <A HREF="http://www.inf.fu-berlin.de/~dahm">M. Dahm</A>
 */
public class LXOR
    extends Instruction
{

    public LXOR () {
        super(LXOR, (short)1);
    }

}

