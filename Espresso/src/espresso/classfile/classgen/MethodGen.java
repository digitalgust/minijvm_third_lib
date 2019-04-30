package  espresso.classfile.classgen;




import  espresso.classfile.Constants;
import  espresso.classfile.javaclass.*;
import  java.util.Vector;
import  java.util.Enumeration;




/** 
 * Template class for building up a method. This is done by defining exception
 * handlers, adding thrown exceptions, local variables and attributes, whereas
 * the `LocalVariableTable' attribute will be set automatically for the code.
 *
 * While generating code it may be necessary to insert NOP operations. You can
 * use the `removeNOPs' method to get rid off them.
 * The resulting method object can be obtained via the `getMethod()' method.
 *
 * @version 980203
 * @author  <A HREF="http://www.inf.fu-berlin.de/~dahm">M. Dahm</A>
 * @see     Instruction
 * @see     Method
 */
public final class MethodGen
    implements Constants
{

    private String method_name;
    private String class_name;
    private ClassGenType return_type;
    private ClassGenType[] arg_types;
    private String[] arg_names;
    private int access_flags;
    private int slot;
    private InstructionList il;
    private ConstantPoolGen cp;
    private Vector variable_vec =  new Vector();
    private Vector attribute_vec =  new Vector();
    private Vector exception_vec =  new Vector();
    private Vector throws_vec =  new Vector();


    /**
   * Immutable object, i.e. rather a record.
   */
    private final class CException {

        InstructionHandle start_pc, end_pc, handler_pc;
        int catch_type;


        private CException (InstructionHandle start_pc, InstructionHandle end_pc, InstructionHandle handler_pc, int catch_type) {
            this.start_pc =  start_pc;
            this.end_pc =  end_pc;
            this.handler_pc =  handler_pc;
            this.catch_type =  catch_type;
        }

    }


    /**
   * Declare method. If the method is non-static the constructor automatically
   * declares a local variable `$this' in slot 0.
   *
   * @param access_flags access qualifiers
   * @param return_type  method type
   * @param arg_types argument types
   * @param arg_names argument names (may be null)
   * @param method_name name of method
   * @param class_name class name containing this method
   * @param il instruction list associated with this method, may be empty initially
   * @param cp constant pool
   */
    public MethodGen (int access_flags, ClassGenType return_type, ClassGenType[] arg_types, String[] arg_names, String method_name, String class_name, InstructionList il, ConstantPoolGen cp) {
        this.access_flags =  access_flags;
        this.return_type =  return_type;
        this.arg_types =  arg_types;
        this.arg_names =  arg_names;
        this.method_name =  method_name;
        this.class_name =  class_name;
        this.il =  il;
        this.cp =  cp;
        /* Add local variables, namely the implicit `this' and the arguments
     */
        if ((access_flags & ACC_STATIC) == 0) // Instance method -> `this' is local var 0
        addLocalVariable("$this", new ClassGenType(class_name), null, null); // Valid from start to the end

        if (arg_types != null) {
            int size =  arg_types.length;

            if (arg_names != null) { // Names for variables provided?
                if (size != arg_names.length) throw  new ClassGenException("Mismatch in argument array lengths: " + size + " vs. " + arg_names.length);
            } else { // Give them dummy names
                arg_names =  new String[size];
                for (int i =  0; i < size; i++) arg_names[i] =  "$arg" + i;
            }

            for (int i =  0; i < size; i++) addLocalVariable(arg_names[i], arg_types[i], null, null); // Valid from start to the end
        }
    }


    public LocalVariableGen lookupLocal (String name) {
        int n =  variable_vec.size();

        for (int i =  0; i < n; i++) {
            LocalVariableGen lv =  (LocalVariableGen)variable_vec.elementAt(i);

            if (name.equals(lv.getName())) {
                return  lv;
            }
        }

        return  null;
    }


    /**
   * Add a local variable to this method.
   *
   * @param name variable name
   * @param type variable type
   * @param start from where the variable is valid, if this is null,
   * it is valid from the start
   * @param end until where the variable is valid, if this is null,
   * it is valid to the end
   * @return new local variable
   * @see LocalVariable
   */
    public LocalVariableGen addLocalVariable (String name, ClassGenType type, InstructionHandle start, InstructionHandle end) {
        LocalVariableGen l =  null;

        if (slot < MAX_SHORT) { // Ensures capacity for double and long
            l =  new LocalVariableGen(slot++, name, type, start, end);
            String s =  type.getSignature();
            if (s.equals("J") || s.equals("D")) // double and long take two entries
            slot++;
            variable_vec.addElement(l);
        } else throw  new ClassGenException("Out of local variables: " + slot);

        return  l;
    }


    /**
   * Add an exception handler, i.e. specify region where a handler is active and an
   * instruction where the actual handling is done.
   *
   * @param start_pc Start of region
   * @param end_pc End of region
   * @param handler_pc Where handling is done
   * @param catch_type which exception is handled (reference to class in constant pool)
   */
    public void addExceptionHandler (InstructionHandle start_pc, InstructionHandle end_pc, InstructionHandle handler_pc, int catch_type) {
        if ((start_pc == null) || (end_pc == null) || (handler_pc == null)) throw  new ClassGenException("Exception handler target is a null instruction");
        exception_vec.addElement(new CException(start_pc, end_pc, handler_pc, catch_type));
    }


    /**
   * Add an exception handler, i.e. specify region where a handler is active and an
   * instruction where the actual handling is done.
   *
   * @param start_pc Start of region
   * @param end_pc End of region
   * @param handler_pc Where handling is done
   * @param catch_type which exception is handled (fully qualified class name)
   */
    public void addExceptionHandler (InstructionHandle start_pc, InstructionHandle end_pc, InstructionHandle handler_pc, String catch_type) {
        addExceptionHandler(start_pc, end_pc, handler_pc, cp.addClass(catch_type));
    }


    /**
   * Add an exception thrown by this method. This is actually an attribute of the
   * method and thus is being added to the attribute list.
   *
   * @param class_name name of exception
   */
    public void addException (String class_name) {
        throws_vec.addElement(class_name);
    }


    /**
   * Add an attribute to this method. Currently, the JVM knows about the `Code' and
   * `Exceptions' attribute, others will be ignored.
   *
   * @param attr Attribute to be added
   */
    public void addAttribute (Attribute attr) {
        attribute_vec.addElement(attr);
    }


    /**
   * Add `Exceptions' attribute if there are any exceptions thrown by this method.
   *
   * @see ExceptionTable
   */
    private final void addExceptionAttribute () {
        int size =  throws_vec.size();

        if (size > 0) {
            int[] ex =  new int[size];

            try {
                for (int i =  0; i < size; i++) ex[i] =  cp.addClass((String)throws_vec.elementAt(i));
            } catch (ArrayIndexOutOfBoundsException e) {

            }

            ExceptionTable ex_t =  new ExceptionTable(cp.addUtf8("Exceptions"), 2 + 2*size, ex, cp.getConstantPool());
            addAttribute(ex_t);
        }
    }


    /**
   * @return InstructionList (geter for il )
   */
    public final InstructionList getInstructionList () {
        return  il;
    }


    /**
   * @return all attributes of this method.
   */
    public final Attribute[] getAttributes () {
        addExceptionAttribute(); // Check for thrown exceptions
        int attr_len =  attribute_vec.size();
        Attribute[] attributes =  new Attribute[attr_len];
        attribute_vec.copyInto(attributes);
        return  attributes;
    }


    /**
   * Convert `LocalVariableGen's to `LocalVariable's
   */
    private final LocalVariable[] getLocalVariableTable () {
        int size =  variable_vec.size();
        LocalVariable[] lv =  new LocalVariable[size];

        try {
            for (int i =  0; i < size; i++) lv[i] =  ((LocalVariableGen)variable_vec.elementAt(i)).getLocalVariable(il, cp);
        } catch (ArrayIndexOutOfBoundsException e) {

        } // Never occurs

        return  lv;
    }


    /*
   * @return array of declared local variables
   */
    public final LocalVariableGen[] getLocalVariables () {
        LocalVariableGen[] lg;
        int size =  variable_vec.size(), start =  0;
        if ((access_flags & ACC_STATIC) == 0) // return everything except `this' in slot 0
        start =  1;
        lg =  new LocalVariableGen[size - start];

        try {
            for (int i =  start; i < size; i++) lg[i - start] =  (LocalVariableGen)variable_vec.elementAt(i);
        } catch (ArrayIndexOutOfBoundsException e) {

        } // Never occurs

        return  lg;
    }


    /**
   * Get method object.
   *
   * The maximum stack size has to be supplied by the user, since it is hard
   * to determine statically with just the byte code at hands. I.e. you'd have to
   * identify loops and such.
   * The stack size can be computed much easier on the fly when creating the code.
   *
   * @param max_stack maximum stack size of this method
   * @param il instructions (byte code)
   * @param cp constant pool
   * @return method object
   */
    public Method getMethod (int max_stack) {
        String signature =  ClassGenType.getMethodSignature(return_type, arg_types);
        int name_index =  cp.addUtf8(method_name);
        int signature_index =  cp.addUtf8(signature);
        /* Also updates positions of instructions, i.e. their indices
     */
        byte[] byte_code =  il.getByteCode();
        /* Create LocalVariableTable attribute (for debuggers)
     */
        LocalVariable[] lv =  getLocalVariableTable();
        LocalVariableTable table =  new LocalVariableTable(cp.addUtf8("LocalVariableTable"), 2 + lv.length*10, lv, null);
        /* Make array of code attributes. Other attributes than LocalVariableTable:
     * TODO
     */
        Attribute[] code_attrs =  {table};
        int attrs_len =  0;

        if (slot == 0) { // No local variables
            code_attrs =  null;
            attrs_len =  0;
        } else for (int i =  0; i < code_attrs.length; i++) attrs_len +=  (6 + code_attrs[i].getLength());

        /* Each attribute causes 6
						       * additional header bytes */
        // Create Exception table of handled exceptions
        int exc_len =  exception_vec.size();
        CodeException[] c_exc;

        if (exc_len > 0) {
            c_exc =  new CodeException[exc_len];

            for (int i =  0; i < exc_len; i++) {
                int start_pc, end_pc, handler_pc;

                try {
                    CException c =  (CException)exception_vec.elementAt(i);
                    start_pc =  c.start_pc.getInstruction().getPosition();
                    end_pc =  c.end_pc.getInstruction().getPosition();
                    handler_pc =  c.handler_pc.getInstruction().getPosition();
                    c_exc[i] =  new CodeException(start_pc, end_pc, handler_pc, c.catch_type);
                } catch (ArrayIndexOutOfBoundsException e) {

                }
            }
        } else c_exc =  null;

        exc_len =  exc_len*8; // Every entry takes 8 bytes
        // Create `Code' attribute
        Code code =  new Code(cp.addUtf8("Code"), 8 + byte_code.length + 
        /* prologue +
								       * byte code */
        2 + exc_len + // exceptions
        2 + attrs_len, // attributes
        max_stack, slot, byte_code, c_exc, code_attrs, cp.getConstantPool());
        attribute_vec.addElement(code);
        return  new Method(access_flags, name_index, signature_index, getAttributes(), cp.getConstantPool());
    }


    /**
   * Checks an instruction handle whether it contains a NOP instruction. If so,
   * update the instruction and add the NOP instruction to a given vector, if
   * it is not already in there. If we reach the end of the list without finding
   * a non-NOP instruction a pointer to the last NOP is returned. (Santiago)
   *
   * @param nop_vec Vector containing NOP instruction handles
   * @param target Target instruction handle being referenced
   */
    private static final InstructionHandle checkTarget (Vector nop_vec, 
		InstructionHandle target) {
        InstructionHandle last, new_target;

		last = new_target = target;

        if (target.instruction instanceof NOP) {
            for (new_target =  target.next;
            	 new_target != null && new_target.instruction instanceof NOP;
				 new_target =  new_target.next) {
				last = new_target;
			}

			if (new_target != null && !nop_vec.contains(target)) {
				nop_vec.addElement(target); 	// target of multiple instructions
			}
        }

        return (new_target == null) ? last : new_target;
    }


    /**
   * Remove all NOPs from the instruction list (if possible) and update every
   * object refering to them, i.e. branch instructions, local variables and
   * exception handlers.
   */
    public final void removeNOPs () {
        Vector nop_vec =  new Vector();

        /* Check branch instructions. */

        for (InstructionHandle ih =  il.getStart(); ih != null; ih =  ih.next) {
            Instruction i = ih.getInstruction();

            if (i instanceof BranchInstruction) {
                BranchInstruction b = (BranchInstruction) i;
				InstructionHandle target = b.getTarget();
                b.setTarget(checkTarget(nop_vec, target));

                if (b instanceof Select) { // Either LOOKUPSWITCH or TABLESWITCH
                    InstructionHandle[] targets =  ((Select)b).getTargets();
                    for (int j =  0; j < targets.length; j++) // Update targets
                    targets[j] =  checkTarget(nop_vec, targets[j]);
                }
            }
        }

        /* Check the scope of all local variables. */

        LocalVariableGen[] lg =  getLocalVariables();

        for (int i =  0; i < lg.length; i++) {
            InstructionHandle start =  lg[i].getStart();
            InstructionHandle end =  lg[i].getEnd();
            if (start != null) lg[i].setStart(checkTarget(nop_vec, start));
            if (end != null) lg[i].setEnd(checkTarget(nop_vec, end));
        }

        /* Check all declared exception handlers.  */ 

        int size =  exception_vec.size();
        CException[] exceptions =  new CException[size];
        exception_vec.copyInto(exceptions);

        for (int i =  0; i < size; i++) {
            exceptions[i].start_pc =  checkTarget(nop_vec, exceptions[i].start_pc);
            exceptions[i].end_pc =  checkTarget(nop_vec, exceptions[i].end_pc);
            exceptions[i].handler_pc =  checkTarget(nop_vec, exceptions[i].handler_pc);
        }


        /* 
         * Check for NOPs not targetted anywhere in the code. Make sure not
         * to remove NOPs at the end of the list. (Santiago)   
         */

        for (InstructionHandle ih = il.getStart(); ih.next != null; ih = ih.next) {
            if ((ih.getInstruction() instanceof NOP) && !nop_vec.contains(ih)) {
                nop_vec.addElement(ih);
            }
        }

        /* 
		 * Finally remove all redudant NOPs from the list, all instructions, etc.
		 * targetting them, have been updated.
		 */
        size =  nop_vec.size();

        try {
            for (int i =  0; i < size; i++) {
                InstructionHandle ih = (InstructionHandle) nop_vec.elementAt(i);
                il.delete(ih);
            }
        } catch (ArrayIndexOutOfBoundsException e) {
        }
    }


    public void setMethodName (String method_name) {
        this.method_name =  method_name;
    }


    public String getMethodName () {
        return  method_name;
    }


    public String getClassName () {
        return  class_name;
    }


    public void setReturnType (ClassGenType return_type) {
        this.return_type =  return_type;
    }


    public ClassGenType getReturnType () {
        return  return_type;
    }


    public void setArgTypes (ClassGenType[] arg_types) {
        this.arg_types =  arg_types;
    }


    public ClassGenType[] getArgTypes () {
        return  arg_types;
    }


    public void setAccessFlags (short access_flags) {
        this.access_flags =  access_flags;
    }


    public int getAccessFlags () {
        return  access_flags;
    }

}

