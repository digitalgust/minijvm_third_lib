package  espresso.classfile.classgen;




import  espresso.classfile.Constants;
import  espresso.classfile.javaclass.*;
import  espresso.classfile.util.ByteSequence;
import  java.io.*;
import  java.util.Enumeration;
import  java.util.Hashtable;
import  java.util.Vector;




/** 
 * This class is a container for a list of `Instruction's, instructions can
 * be appended, inserted, deleted, etc.. Instructions are being wrapped into
 * InstructionHandle objects that are returned upon append/insert operations.
 * They give the user (read only) access to the list structure, it can be
 * traversed and manipulated in a controlled way.
 *
 * A list is finally dumped to a byte code array with `getByteCode'.
 *
 * @version 980120
 * @author  <A HREF="http://www.inf.fu-berlin.de/~dahm">M. Dahm</A>
 * @see     Instruction
 * @see     InstructionHandle
 */
public final class InstructionList
    implements Constants
{

    private InstructionHandle start =  null, end =  null;
    private int length =  0; // number of elements in list


    /**
   * Test for empty list.
   */
    public final boolean isEmpty () {
        return  start == null;
    } // && end == null


    /**
   * Create (empty) instruction list.
   */
    public InstructionList () {

    }


    /**
   * Find the target instruction (handle) that corresponds to the given target
   * position.
   *
   * @param ihs array of instruction handles
   * @param pos array of positions corresponding to ihs
   * @param count length of arrays
   * @param target target position to search for
   * @return target position's instruction handle if available
   */
    private static final InstructionHandle findTarget (InstructionHandle[] ihs, int[] pos, int count, int target) {
        int l =  0, r =  count - 1;

        /* Do a binary search since the pos array is orderd.
     */
        do {
            int i =  (l + r)/2;
            int j =  pos[i];
            if (j == target) // target found
            return  ihs[i]; else if (target < j) // else constrain search area
            r =  i - 1; else // target > j
             l =  i + 1;
        } while (l <= r);

        return  null;
    }


    /**
   * Initialize instruction list from byte array.
   *
   * @param code byte array containing the instructions
   */
    public InstructionList (byte[] code) {
        ByteSequence bytes =  new ByteSequence(code);
        InstructionHandle[] ihs =  new InstructionHandle[code.length];
        int[] pos =  new int[code.length]; // Can't be more than that
        int count =  0; // Contains actual length

        /* Pass 1: Create an object for each byte code and append them
     * to the list.
     */
        try {
            while (bytes.available() > 0) {
                // Remember byte offset and associate it with the instruction
                pos[count] =  bytes.getIndex();
                // read one instruction from the byte stream
                Instruction i =  Instruction.readInstruction(bytes);
                if (i instanceof BranchInstruction) // Use proper append() method
                ihs[count] =  append((BranchInstruction)i); else ihs[count] =  append(i);
                count++;
            }
        } catch (IOException e) {
            throw  new ClassGenException(e.toString());
        }

        /* Pass 2: Look for BranchInstruction and update their targets, i.e.
     * convert offsets to instruction handles.
     */
        for (int i =  0; i < count; i++) {
            if (ihs[i] instanceof BranchHandle) {
                BranchInstruction bi =  (BranchInstruction)ihs[i].instruction;
                int target =  bi.position + bi.getIndex();
                /* Byte code position:
						   * relative -> absolute. */
                // Search for target position
                InstructionHandle ih =  findTarget(ihs, pos, count, target);
                if (ih == null) // Search failed
                throw  new ClassGenException("Couldn't find target instruction: " + bi);
                bi.setTarget(ih); // Update target

                // If it is a Select instruction, update all branch targets
                if (bi instanceof Select) { // Either LOOKUPSWITCH or TABLESWITCH
                    Select s =  (Select)bi;
                    InstructionHandle[] targets =  s.getTargets();
                    int[] indices =  s.getIndices();

                    for (int j =  0; j < targets.length; j++) {
                        target =  bi.position + indices[j];
                        ih =  findTarget(ihs, pos, count, target);
                        if (ih == null) // Search failed
                        throw  new ClassGenException("Couldn't find instruction target: " + bi);
                        targets[j] =  ih; // Update target      
                    }
                }
            }
        }
    }


    /**
   * Initialize list with (nonnull) instruction.
   *
   * @param i first instruction
   */
    public InstructionList (Instruction i) {
        if (i == null) throw  new ClassGenException("Invalid null instruction");
        append(i);
    }


    /**
   * Initialize list with (nonnull) compound instruction. Consumes argument
   * list, i.e. it becomes empty.
   *
   * @param c compound instruction (list)
   */
    public InstructionList (CompoundInstruction c) {
        if (c == null) throw  new ClassGenException("Invalid null compound instruction");
        append(c.getInstructionList());
    }


    /**
   * Append another list after instruction i contained in this list.
   * Consumes argument list, i.e. it becomes empty.
   *
   * @param i  where to append the instruction list 
   * @param il Instruction list to append to this one
   * @return instruction handle pointing to the last appended instruction, i.e.
   * il.getEnd()
   */
    public final InstructionHandle append (Instruction i, InstructionList il) {
        InstructionHandle ih;
        if (il == null) throw  new ClassGenException("Appending null InstructionList");
        if ((ih =  findInstruction2(i)) == null) // Also applies for empty list
        throw  new ClassGenException("Instruction " + i + " is not contained in this list.");
        return  append(ih, il);
    }


    /**
   * Append another list after instruction (handle) ih contained in this list.
   * Consumes argument list, i.e. it becomes empty.
   *
   * @param ih where to append the instruction list 
   * @param il Instruction list to append to this one
   * @return instruction handle pointing to the last appended instruction, i.e.
   * il.getEnd()
   */
    public final InstructionHandle append (InstructionHandle ih, InstructionList il) {
        InstructionHandle next =  ih.next, ret =  il.start;
        ih.next =  il.start;
        il.start.prev =  ih;
        il.end.next =  next;
        if (next != null) // i == end ?
        next.prev =  il.end; else end =  il.end; // Update end ...
        length +=  il.length; // Update length
        il.dispose();
        return  ret;
    }


    /**
   * Insert another list before Instruction i contained in this list.
   * Consumes argument list, i.e. it becomes empty.
   *
   * @param i  where to append the instruction list 
   * @param il Instruction list to insert
   * @return instruction handle pointing to the first inserted instruction,
   * i.e. il.getStart()
   */
    public final InstructionHandle insert (Instruction i, InstructionList il) {
        InstructionHandle ih;
        if (il == null) throw  new ClassGenException("Inserting null InstructionList");
        if ((ih =  findInstruction1(i)) == null) throw  new ClassGenException("Instruction " + i + " is not contained in this list.");
        return  insert(ih, il);
    }


    /**
   * Insert another list before Instruction handle ih contained in this list.
   * Consumes argument list, i.e. it becomes empty.
   *
   * @param i  where to append the instruction list 
   * @param il Instruction list to insert
   * @return instruction handle pointing to the first inserted instruction,
   * i.e. il.getStart()
   */
    public final InstructionHandle insert (InstructionHandle ih, InstructionList il) {
        InstructionHandle prev =  ih.prev, ret =  il.start;
        ih.prev =  il.end;
        il.end.next =  ih;
        il.start.prev =  prev;
        if (prev != null) // i == start ?
        prev.next =  il.start; else start =  il.start; // Update start ...
        length +=  il.length; // Update length
        il.dispose();
        return  ret;
    }


    /**
   * Append an instruction to the end of this list.
   *
   * @param ih instruction to append
   */
    private final void append (InstructionHandle ih) {
        if (isEmpty()) {
            start =  end =  ih;
            ih.next =  ih.prev =  null;
        } else {
            end.next =  ih;
            ih.prev =  end;
            ih.next =  null;
            end =  ih;
        }

        length++; // Update length
    }


    /**
   * Append an instruction to the end of this list.
   *
   * @param i instruction to append
   * @return instruction handle of the appended instruction
   */
    public final InstructionHandle append (Instruction i) {
        InstructionHandle ih =  new InstructionHandle(i);
        append(ih);
        return  ih;
    }


    /**
   * Append a branch instruction to the end of this list.
   *
   * @param i branch instruction to append
   * @return branch instruction handle of the appended instruction
   */
    public final BranchHandle append (BranchInstruction i) {
        BranchHandle ih =  new BranchHandle(i);
        append(ih);
        return  ih;
    }


    /**
   * Insert an instruction at start of this list.
   *
   * @param ih instruction to insert
   */
    private final void insert (InstructionHandle ih) {
        if (isEmpty()) {
            start =  end =  ih;
            ih.next =  ih.prev =  null;
        } else {
            start.prev =  ih;
            ih.next =  start;
            ih.prev =  null;
            start =  ih;
        }

        length++;
    }


    /**
   * Insert an instruction at start of this list.
   *
   * @param i instruction to insert
   * @return instruction handle of the inserted instruction
   */
    public final InstructionHandle insert (Instruction i) {
        InstructionHandle ih =  new InstructionHandle(i);
        insert(ih);
        return  ih;
    }


    /**
   * Insert a branch instruction at start of this list.
   *
   * @param i branch instruction to insert
   * @return branch instruction handle of the appended instruction
   */
    public final BranchHandle insert (BranchInstruction i) {
        BranchHandle ih =  new BranchHandle(i);
        insert(ih);
        return  ih;
    }


    /**
   * Append another list to this one.
   *
   * @param il list to append to end of this list
   * @return instruction handle of the last appended instruction
   */
    public final InstructionHandle append (InstructionList il) {
        if (isEmpty()) {
            start =  il.start;
            end =  il.end;
            length =  il.length;
            il.dispose();
            return  end;
        } else return  append(end.instruction, il);
    }


    /**
   * Insert another list.   
   *
   * @param il list to insert before start of this list
   * @return instruction handle of the first inserted instruction
   */
    public final InstructionHandle insert (InstructionList il) {
        if (isEmpty()) return  append(il); // Code is identical for this case
        else return  insert(start.instruction, il);
    }


    /**
   * Append a single instruction j after another instruction i, which
   * must be in this list of course!
   *
   * @param i Instruction in list
   * @param j Instruction to append after i in list
   * @return instruction handle of the last appended instruction
   */
    public final InstructionHandle append (Instruction i, Instruction j) {
        return  append(i, new InstructionList(j));
    }


    /**
   * Insert a single instruction j before another instruction i, which
   * must be in this list of course!
   *
   * @param i Instruction in list
   * @param j Instruction to insert before i in list
   * @return instruction handle of the first inserted instruction
   */
    public InstructionHandle insert (Instruction i, Instruction j) {
        return  insert(i, new InstructionList(j));
    }


    /**
   * Append a compound instruction, after instruction i.
   *
   * @param i Instruction in list
   * @param c The composite instruction (containing an InstructionList)
   * @return instruction handle of the last appended instruction
   */
    public final InstructionHandle append (Instruction i, CompoundInstruction c) {
        return  append(i, c.getInstructionList());
    }


    /**
   * Insert a compound instruction before instruction i.
   *
   * @param i Instruction in list
   * @param c The composite instruction (containing an InstructionList)
   * @return instruction handle of the first inserted instruction
   */
    public final InstructionHandle insert (Instruction i, CompoundInstruction c) {
        return  insert(i, c.getInstructionList());
    }


    /**
   * Append a compound instruction.
   *
   * @param c The composite instruction (containing an InstructionList)
   * @return instruction handle of the last appended instruction
   */
    public final InstructionHandle append (CompoundInstruction c) {
        return  append(c.getInstructionList());
    }


    /**
   * Insert a compound instruction.
   *
   * @param c The composite instruction (containing an InstructionList)
   * @return instruction handle of the first inserted instruction
   */
    public final InstructionHandle insert (CompoundInstruction c) {
        return  insert(c.getInstructionList());
    }


    /**
   * Remove from instruction `prev' to instruction `next' both contained
   * in this list.
   *
   * @param prev where to start deleting (predecessor, exclusive)
   * @param next where to end deleting (successor, exclusive)
   */
    private final void remove (InstructionHandle prev, InstructionHandle next) {
        InstructionHandle first = (prev == null) ? start : prev.next;
        InstructionHandle last = (next == null) ? end : next.prev;

		if (prev != null) {
			prev.next = next; 
		}
		else {
			start = next;
		}

		if (next != null) {
			next.prev = prev; 
		}
		else {
			end = prev;
		}

		if (first == last) {		// A single instruction
			length--;
			first.dispose();
		}
		else {
			InstructionHandle ih = first;

			do {
				length--;
				InstructionHandle old = ih;
				ih = ih.next;
				old.dispose();
			} while (ih != last);
		}
    }


    /**
   * Remove instruction from this list.
   *
   * @param ih instruction (handle) to remove
   */
    public final void delete (InstructionHandle ih) {
        remove(ih.prev, ih.next);
    }


    /**
   * Remove instruction from this list.
   *
   * @param i instruction to remove
   */
    public final void delete (Instruction i) {
        InstructionHandle ih;
        if ((ih =  findInstruction1(i)) == null) throw  new ClassGenException("Instruction " + i + " is not contained in this list.");
        delete(ih);
    }


    /**
   * Remove instructions from instruction `from' to instruction `to' contained
   * in this list. The user must ensure that `from' is an instruction before
   * `to', or risk havoc.
   *
   * @param from where to start deleting (inclusive)
   * @param to   where to end deleting (inclusive)
   */
    public final void delete (InstructionHandle from, InstructionHandle to) {
        remove(from.prev, to.next);
    }


    /**
   * Remove instructions from instruction `from' to instruction `to' contained
   * in this list. The user must ensure that `from' is an instruction before
   * `to', or risk havoc.
   *
   * @param from where to start deleting (inclusive)
   * @param to   where to end deleting (inclusive)
   */
    public final void delete (Instruction from, Instruction to) {
        InstructionHandle from_ih, to_ih;
        if ((from_ih =  findInstruction1(from)) == null) throw  new ClassGenException("Instruction " + from + " is not contained in this list.");
        if ((to_ih =  findInstruction2(to)) == null) throw  new ClassGenException("Instruction " + to + " is not contained in this list.");
        delete(from_ih, to_ih);
    }


    /**
   * Search for given Instruction reference, start at beginning of list.
   *
   * @param i instruction to search for
   * @return instruction found on success, null otherwise
   */
    private final InstructionHandle findInstruction1 (Instruction i) {
        for (InstructionHandle ih =  start; ih != null; ih =  ih.next) if (ih.instruction == i) return  ih;
        return  null;
    }


    /**
   * Search for given Instruction reference, start at end of list
   *
   * @param i instruction to search for
   * @return instruction found on success, null otherwise
   */
    private final InstructionHandle findInstruction2 (Instruction i) {
        for (InstructionHandle ih =  end; ih != null; ih =  ih.prev) if (ih.instruction == i) return  ih;
        return  null;
    }


    /**
   * Give all instructions their position number (offset in byte stream), i.e.
   * make the list ready to be dumped.
   */
    public final void setPositions () {
        int max_additional_bytes =  0, additional_bytes =  0;
        int index =  0;

        /* Pass 1: Set position numbers and sum up the maximum number of bytes an
     * instruction may be shifted.
     */
        for (InstructionHandle ih =  start; ih != null; ih =  ih.next) {
            Instruction i =  ih.instruction;
            i.setPosition(index);

            /* Get an estimate about how many additional bytes may be added, because
       * BranchInstructions may have variable length depending on the target
       * offset (short vs. int) or alignment issues (TABLESWITCH and
       * LOOKUPSWITCH).
       */
            switch (i.getTag()) {
                case JSR:
                case GOTO:
                    max_additional_bytes +=  2;
                    break;

                case TABLESWITCH:
                case LOOKUPSWITCH:
                    max_additional_bytes +=  3;
                    break;
            }

            index +=  i.getLength();
        }

        /* Pass 2: Expand the variable-length (Branch)Instructions depending on
     * the target offset (short or int) and ensure that branch targets are
     * within this list.
     */
        for (InstructionHandle ih =  start; ih != null; ih =  ih.next) {
            Instruction i =  ih.instruction;
            additional_bytes +=  i.updatePosition(additional_bytes, max_additional_bytes);

            if (i instanceof BranchInstruction) { // target instruction within list?
                if (findInstruction1(((BranchInstruction)i).getTarget().instruction) == null) throw  new ClassGenException("Branch target not in instruction list");
            }
        }

        /* Pass 3: Update position numbers (which may have changed due to the
     * preceding expansions), like pass 1.
     */
        index =  0;

        for (InstructionHandle ih =  start; ih != null; ih =  ih.next) {
            Instruction i =  ih.instruction;
            i.setPosition(index);
            index +=  i.getLength();
        }
    }


    /**
   * @return The byte code!
   */
    public byte[] getByteCode () {
        // Update position indices of instructions
        setPositions();
        ByteArrayOutputStream b =  new ByteArrayOutputStream();
        DataOutputStream out =  new DataOutputStream(b);

        try {
            for (InstructionHandle ih =  start; ih != null; ih =  ih.next) {
                Instruction i =  ih.instruction;
                i.dump(out); // Traverse list
            }
        } catch (IOException e) {
            System.err.println(e);
            return  null;
        }

        return  b.toByteArray();
    }


    /**
   * @return String containing all instructions in this list.
   */
    public String toString () {
        StringBuffer buf =  new StringBuffer();

        for (InstructionHandle ih =  start; ih != null; ih =  ih.next) {
            Instruction i =  ih.instruction;
            buf.append(i + "\n");
        }

        return  buf.toString();
    }


    /**
   * @return Enumeration that lists all instructions
   */
    public Enumeration elements () {
        return  new Enumeration() {

            private InstructionHandle ih =  start;


            public Object nextElement () {
                InstructionHandle i =  ih;
                ih =  ih.next;
                return  i;
            }


            public boolean hasMoreElements () {
                return  ih != null;
            }

        };
    }


    /**
   * @return complete, i.e. deep copy of this list
   */
    public InstructionList copy () {
        Hashtable map =  new Hashtable();
        InstructionList il =  new InstructionList();

        /* Pass 1: Make copies of all instructions, append them to the new list
     * and associate old instruction references with the new ones, i.e.
     * a 1:1 mapping.
     */
        for (InstructionHandle ih =  start; ih != null; ih =  ih.next) {
            Instruction i =  ih.instruction;
            Instruction c =  i.copy(); // Use clone for shallow copy
            map.put(ih, il.append(c));
        }

        /* Pass 2: Update branch targets.
     */
        InstructionHandle ih =  start;
        InstructionHandle ch =  il.start;

        while (ih != null) {
            Instruction i =  ih.instruction;
            Instruction c =  ch.instruction;

            if (i instanceof BranchInstruction) {
                BranchInstruction bi =  (BranchInstruction)i;
                BranchInstruction bc =  (BranchInstruction)c;
                InstructionHandle itarget =  bi.getTarget(); // old target
                // New target is in hash map
                bc.setTarget((InstructionHandle)map.get(itarget));

                if (bi instanceof Select) { // Either LOOKUPSWITCH or TABLESWITCH
                    InstructionHandle[] itargets =  ((Select)bi).getTargets();
                    InstructionHandle[] ctargets =  ((Select)bc).getTargets();

                    for (int j =  0; j < itargets.length; j++) { // Update all targets
                        ctargets[j] =  (InstructionHandle)map.get(itargets[j]);
                    }
                }
            }

            ih =  ih.next;
            ch =  ch.next;
        }

        return  il;
    }


    /**
   * Inaccurate, do not use!!
   * @return maximum stack size
   */
    private int getStackSize () {
        int size =  0;

        for (InstructionHandle ih =  start; ih != null; ih =  ih.next) {
            Instruction i =  ih.instruction;
            int stack =  i.produceStack();
            if (stack == UNDEFINED) throw  new ClassGenException("Invalid instruction"); else if (stack == UNPREDICTABLE) stack =  2; // max is pushing a long, i.e. using two stack slots
            size +=  stack;
        }

        return  size;
    }


    /**
   * Delete contents of list.
   */
    public final void dispose () {
        start =  end =  null;
        length =  0;
    }


    /**
   * @return start of list
   */
    public InstructionHandle getStart () {
        return  start;
    }


    /**
   * @return end of list
   */
    public InstructionHandle getEnd () {
        return  end;
    }


    /**
   * @return length of list (Number of instructions, not bytes)
   */
    public int getLength () {
        return  length;
    }

}

