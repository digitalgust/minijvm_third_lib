package  espresso.classfile.classgen;




/** 
 * I2C - Convert int to char
 * Stack: ..., value -> ..., result
 *
 * @version 971116
 * @author  <A HREF="http://www.inf.fu-berlin.de/~dahm">M. Dahm</A>
 */
public class I2C
    extends Instruction
{

    public I2C () {
        super(I2C, (short)1);
    }

}

