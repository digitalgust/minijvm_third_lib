package  espresso.classfile.classgen;




/**
 * Instances of this class give users a handle to the instructions contained in
 * an InstructionList. Instruction objects may be uesd more than once within a
 * list, this is useful because it saves memory and may be much faster.
 *
 * Within an InstructionList an InstructionHandle object is wrapped
 * around all instructions, i.e. it implements a cell in a
 * doubly-linked list. From the outside only the next and the
 * previous instruction (handle) are accessible. One
 * can traverse the list via an Enumeration returned by
 * InstructionList.elements().
 *
 * @see java.util.Enumeration
 * @see Instruction
 * @see BranchHandle
 * @see InstructionList 
 */
public class InstructionHandle {

    InstructionHandle next, prev; // Will be set from the outside
    Instruction instruction;


    public final InstructionHandle getNext () {
        return  next;
    }


    public final InstructionHandle getPrev () {
        return  prev;
    }


    public final Instruction getInstruction () {
        return  instruction;
    }


    public InstructionHandle (Instruction i) {
        instruction =  i;
    }


    /**
   * Delete all contents, i.e. remove user access.
   */
    void dispose () {
        next =  prev =  null;
        instruction =  null;
    }

}

