package  espresso.classfile.classgen;




/**
 * Wrapper class for `compound' operations, virtual instructions that
 * don't exist as byte code, but give a useful meaning. For example,
 * the (virtual) PUSH instruction takes an arbitray argument and produces the
 * appropiate code at dump time (ICONST, LDC, BIPUSH, ...). Also you can use the
 * SWITCH instruction as a useful template for either LOOKUPSWITCH or
 * TABLESWITCH.
 *
 * The interface provides the possibilty for the user to write
 * `templates' or `macros' for such reuseable code patterns.
 *
 * @version 980112
 * @author  <A HREF="http://www.inf.fu-berlin.de/~dahm">M. Dahm</A>
 * @see PUSH
 * @see SWITCH
 */
public interface CompoundInstruction {

    public InstructionList getInstructionList ();

}

