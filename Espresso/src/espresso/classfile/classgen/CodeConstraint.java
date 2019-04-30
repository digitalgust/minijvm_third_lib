package  espresso.classfile.classgen;




/**
 * Code patterns found with the FindPattern class may receive an additional
 * CodeConstraint argument that checks the found piece of code for user-defined
 * constraints. I.e. FindPatterrn.search() returns the matching code if and
 * only if CodeConstraint.checkCode() returns true.
 *
 * @see FindPattern
 */
public interface CodeConstraint {

    /**
   * @param match array of instructions matching the requested pattern
   */
    public boolean checkCode (InstructionHandle[] match);

}

