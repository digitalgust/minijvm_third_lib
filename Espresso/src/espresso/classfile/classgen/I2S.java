package  espresso.classfile.classgen;




/**
 * I2S - Convert int to short
 * Stack: ..., value -> ..., result
 *
 * @version 971119
 * @author  <A HREF="http://www.inf.fu-berlin.de/~dahm">M. Dahm</A>
 */
public class I2S
    extends Instruction
{

    public I2S () {
        super(I2S, (short)1);
    }

}

