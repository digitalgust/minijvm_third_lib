package  espresso.classfile.classgen;




/** 
 * I2F - Convert int to float
 * Stack: ..., value -> ..., result
 *
 * @version 971116
 * @author  <A HREF="http://www.inf.fu-berlin.de/~dahm">M. Dahm</A>
 */
public class I2F
    extends Instruction
{

    public I2F () {
        super(I2F, (short)1);
    }

}

