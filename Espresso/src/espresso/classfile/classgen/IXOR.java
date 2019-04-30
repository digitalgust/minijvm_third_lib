package  espresso.classfile.classgen;




/** 
 * IXOR - Bitwise XOR int
 * Stack: ..., value1, value2 -> ..., result
 *
 * @version 971119
 * @authXOR  <A HREF="http://www.inf.fu-berlin.de/~dahm">M. Dahm</A>
 */
public class IXOR
    extends Instruction
{

    public IXOR () {
        super(IXOR, (short)1);
    }

}

