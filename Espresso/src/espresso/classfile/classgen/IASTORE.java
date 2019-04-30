package  espresso.classfile.classgen;




/** 
 * IASTORE -  Store into int array
 * Stack: ..., arrayref, index, value -> ...
 *
 * @version 971119
 * @author  <A HREF="http://www.inf.fu-berlin.de/~dahm">M. Dahm</A>
 */
public class IASTORE
    extends Instruction
{

    public IASTORE () {
        super(IASTORE, (short)1);
    }

}

