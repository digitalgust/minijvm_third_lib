package  espresso.classfile.classgen;




/**
 * I2L - Convert int to long
 * Stack: ..., value -> ..., result.word1, result.word2
 *
 * @version 971119
 * @author  <A HREF="http://www.inf.fu-berlin.de/~dahm">M. Dahm</A>
 */
public class I2L
    extends Instruction
{

    public I2L () {
        super(I2L, (short)1);
    }

}

