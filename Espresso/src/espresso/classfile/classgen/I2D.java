package  espresso.classfile.classgen;




/**
 * I2D - Convert int to double
 * Stack: ..., value -> ..., result.word1, result.word2
 *
 * @version 971119
 * @author  <A HREF="http://www.inf.fu-berlin.de/~dahm">M. Dahm</A>
 */
public class I2D
    extends Instruction
{

    public I2D () {
        super(I2D, (short)1);
    }

}

