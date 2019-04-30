package  espresso.classfile.classgen;




/** 
 * I2B - Convert int to byte
 * Stack: ..., value -> ..., result
 *
 * @version 971116
 * @author  <A HREF="http://www.inf.fu-berlin.de/~dahm">M. Dahm</A>
 */
public class I2B
    extends Instruction
{

    public I2B () {
        super(I2B, (short)1);
    }

}

