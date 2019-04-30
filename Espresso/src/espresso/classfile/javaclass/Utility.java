package  espresso.classfile.javaclass;




import  espresso.classfile.Constants;
import  espresso.classfile.util.ByteSequence;
import  java.io.IOException;




/**
 * Utility functions that do not really belong to any class in particular.
 *
 * @version 971017
 * @author  <A HREF="http://www.inf.fu-berlin.de/~dahm">M. Dahm</A>
 */
public abstract class Utility
    implements Constants
{

    private static int consumed_chars;
    /* How many chars have been consumed
				      * during parsing in signatureToString().
				      * Read by methodSignatureToString().
				      * Set by side effect,but only internally.
				      */
    private static boolean wide =  false;


    /* The `WIDE' instruction is used in the
				      * byte code to allow 16-bit wide indices
				      * for local variables. This opcode
				      * precedes an `ILOAD', e.g.. The opcode
				      * immediately following takes an extra
				      * byte which is combined with the
				      * following byte to form a
				      * 16-bit value.
				      */
    /**
   * Convert bit field of flags into string such as `static final'.
   *
   * @param  access_flags Access flags
   * @return String representation of flags
   */
    public static final String accessToString (int access_flags) {
        return  accessToString(access_flags, false);
    }


    /**
   * Convert bit field of flags into string such as `static final'.
   *
   * Special case: Classes compiled with new compilers and with the
   * `ACC_SUPER' flag would be said to be "synchronized". This is
   * because SUN used the same value for the flags `ACC_SUPER' and
   * `ACC_SYNCHRONIZED'. 
   *
   * @param  access_flags Access flags
   * @param  for_class access flags are for class qualifiers ?
   * @return String representation of flags
   */
    public static final String accessToString (int access_flags, boolean for_class) {
        StringBuffer buf =  new StringBuffer();
        int p =  0;

        for (int i =  0; p < MAX_ACC_FLAG; i++) { // Loop through known flags
            p =  pow2(i);

            if ((access_flags & p) != 0) {
                /* Special case: Classes compiled with new compilers and with the
	 * `ACC_SUPER' flag would be said to be "synchronized". This is
	 * because SUN used the same value for the flags `ACC_SUPER' and
	 * `ACC_SYNCHRONIZED'.
	 */
                if ((p == ACC_SUPER) && for_class) buf.append("special "); else buf.append(ACCESS_NAMES[i] + " ");
            }
        }

        return  buf.toString().trim();
    }


    /**
   * Obtains the signed 32-bit value from 4 bytes, 
   * bytesToInt((byte) 0, (byte) 0, b1, b1) obtains the unsigned 16-bit value
   * from 2 bytes.
   */
    public static final int bytesToInt (byte b0, byte b1, byte b2, byte b3) {
        int result, tmp;
        result =  ((int)b0) << 24;
        tmp =  (b1 < 0? (int)(256 + b1) : (int)b1);
        tmp <<=  16;
        result =  result | tmp;
        tmp =  (b2 < 0? (int)(256 + b2) : (int)b2);
        tmp <<=  8;
        result =  result | tmp;
        tmp =  (b3 < 0? (int)(256 + b3) : (int)b3);
        result =  result | tmp;
        return  result;
    }


    /**
   * bytesToShort(b0, b1) obtains the signed 16-bit value from 2 bytes.
   */
    public static final short bytesToShort (byte b0, byte b1) {
        int result, tmp;
        result =  ((int)b0) << 8;
        tmp =  (b1 < 0)? (int)(256 + b1) : (int)b1;
        result =  result | tmp;
        return  (short)result;
    }


    /**
   * bytesToUnsignedShort(b0, b1) obtains the unsigned 16-bit value from 2 bytes.
   */
    public static final int bytesToUnsignedShort (byte b0, byte b1) {
        return  (((int)b0) << 8) | (int)b1;
    }


    /**
   * Convert (signed) byte to (unsigned) short value, i.e. all negative
   * values become positive.
   */
    public static final short byteToShort (byte b) {
        return  (b < 0)? (short)(256 + b) : (short)b;
    }


    /**
   * Disassemble a byte array of JVM byte codes starting from code line 
   * `index' and return the dissambled string representation. Decode only
   * `num' opcodes (including their operands), use -1 if you want to
   * decompile everything.
   *
   * @param  code byte code array
   * @param  constant_pool Array of constants
   * @param  index offset in `code' array
   * <EM>(number of opcodes, not bytes!)</EM>
   * @param  length number of opcodes to decompile, -1 for all
   * @return String representation of byte codes
   */
    public static final String codeToString (byte[] code, ConstantPool constant_pool, int index, int length) {
        StringBuffer buf =  new StringBuffer();
        ByteSequence stream =  new ByteSequence(code);

        try {
            for (int i =  0; i < index; i++) // Skip `index' lines of code
            codeToString(stream, constant_pool);

            for (int i =  0; stream.available() > 0; i++) {
                if ((length < 0) || (i < length)) buf.append(i + "(" + stream.getIndex() + "):\t\t" + codeToString(stream, constant_pool) + '\n');
            }
        } catch (IOException e) {
            System.out.println(buf.toString());
            e.printStackTrace();
            throw  new ClassFormatError("Byte code error: " + e);
        }

        return  buf.toString();
    }


    /**
   * Disassemble a stream of byte codes and return the
   * string representation.
   *
   * @param  stream data input stream
   * @param  constant_pool Array of constants
   * @return String representation of byte code
   */
    public static final String codeToString (ByteSequence bytes, ConstantPool constant_pool)
        throws IOException
    {
        short opcode =  (short)bytes.readUnsignedByte();
        StringBuffer buf;
        int default_offset =  0, low, high, npairs;
        int index, vindex, constant;
        int[] match, jump_table;
        int no_pad_bytes =  0, offset;
        buf =  new StringBuffer(OPCODE_NAMES[opcode]);

        /* Special case: Skip (0-3) padding bytes, i.e. the
     * following bytes are 4-byte-aligned
     */
        if ((opcode == TABLESWITCH) || (opcode == LOOKUPSWITCH)) {
            int remainder =  bytes.getIndex()%4;
            no_pad_bytes =  (remainder == 0)? 0 : 4 - remainder;

            for (int i =  0; i < no_pad_bytes; i++) {
                byte b;
                if ((b =  bytes.readByte()) != 0) System.err.println("Ooops. Padding byte != 0 " + b);
            }

            // Both cases have a field default_offset in common
            default_offset =  bytes.readInt();
        }

        switch (opcode) {
            /* Table switch has variable length arguments.
       */
            case TABLESWITCH:
                low =  bytes.readInt();
                high =  bytes.readInt();
                offset =  bytes.getIndex() - 12 - no_pad_bytes - 1;
                default_offset +=  offset;
                buf.append("\tdefault = " + default_offset + ", low = " + low + ", high = " + high + "(");
                jump_table =  new int[high - low + 1];

                for (int i =  0; i < jump_table.length; i++) {
                    jump_table[i] =  offset + bytes.readInt();
                    buf.append(jump_table[i]);
                    if (i < jump_table.length - 1) buf.append(", ");
                }

                buf.append(")");
                break;
                /* Lookup switch has variable length arguments.
       */

            case LOOKUPSWITCH:
                {
                    npairs =  bytes.readInt();
                    offset =  bytes.getIndex() - 8 - no_pad_bytes - 1;
                    match =  new int[npairs];
                    jump_table =  new int[npairs];
                    default_offset +=  offset;
                    buf.append("\tdefault = " + default_offset + ", npairs = " + npairs + " (");

                    for (int i =  0; i < npairs; i++) {
                        match[i] =  bytes.readInt();
                        jump_table[i] =  offset + bytes.readInt();
                        buf.append("(" + match[i] + ", " + jump_table[i] + ")");
                        if (i < npairs - 1) buf.append(", ");
                    }

                    buf.append(")");
                }

                break;
                /* Two address bytes + offset from start of byte stream form the
     * jump target
     */

            case GOTO:
            case IFEQ:
            case IFGE:
            case IFGT:
            case IFLE:
            case IFLT:
            case IFNE:
            case IFNONNULL:
            case IFNULL:
            case IF_ACMPEQ:
            case IF_ACMPNE:
            case IF_ICMPEQ:
            case IF_ICMPGE:
            case IF_ICMPGT:
            case IF_ICMPLE:
            case IF_ICMPLT:
            case IF_ICMPNE:
            case JSR:
                buf.append("\t#" + ((bytes.getIndex() - 1) + bytes.readShort()));
                break;
                /* 32-bit wide jumps
       */

            case GOTO_W:
            case JSR_W:
                buf.append("\t#" + ((bytes.getIndex() - 1) + bytes.readInt()));
                break;
                /* Index byte references local variable (register)
       */

            case ALOAD:
            case ASTORE:
            case DLOAD:
            case DSTORE:
            case FLOAD:
            case FSTORE:
            case ILOAD:
            case ISTORE:
            case LLOAD:
            case LSTORE:
            case RET:
                if (wide) {
                    vindex =  bytes.readUnsignedShort();
                    wide =  false; // Clear flag
                } else vindex =  bytes.readUnsignedByte();

                buf.append("\t%" + vindex);
                break;

            case RET_W:
                vindex =  bytes.readShort();
                buf.append("\t%" + vindex);
                break;
                /*
       * Remember wide byte which is used to form a 16-bit address in the
       * following instruction. Relies on that the method is called again with
       * the following opcode.
       */

            case WIDE:
                wide =  true;
                buf.append("\t(wide)");
                break;
                /* Array of basic type.
       */

            case NEWARRAY:
                buf.append("\t<" + TYPE_NAMES[bytes.readByte()] + ">");
                break;
                /* Access object/class fields.
       */

            case GETFIELD:
            case GETSTATIC:
            case PUTFIELD:
            case PUTSTATIC:
                index =  bytes.readUnsignedShort();
                buf.append("\t" + constant_pool.constantToString(index, CONSTANT_Fieldref) + " (" + index + ")");
                break;
                /* Operands are references to classes in constant pool
       */

            case CHECKCAST:
            case INSTANCEOF:
            case NEW:
                index =  bytes.readUnsignedShort();
                buf.append("\t<" + constant_pool.constantToString(index, CONSTANT_Class) + "> (" + index + ")");
                break;
                /* Operands are references to methods in constant pool
       */

            case INVOKESPECIAL:
            case INVOKESTATIC:
            case INVOKEVIRTUAL:
                index =  bytes.readUnsignedShort();
                buf.append("\t" + constant_pool.constantToString(index, CONSTANT_Methodref) + " (" + index + ")");
                break;

            case INVOKEINTERFACE:
                index =  bytes.readUnsignedShort();
                int nargs =  bytes.readUnsignedByte(); // historical, redundant
                buf.append("\t" + constant_pool.constantToString(index, CONSTANT_InterfaceMethodref) + "() (" + index + ")" + nargs + "\t" + bytes.readUnsignedByte()); // Last byte is a reserved space
                break;
                /* Operands are references to items in constant pool
       */

            case LDC_W:
            case LDC2_W:
                index =  bytes.readUnsignedShort();
                buf.append("\t<" + constant_pool.constantToString(index, constant_pool.getConstant(index).getTag()) + "> (" + index + ")");
                break;

            case LDC:
                index =  bytes.readUnsignedByte();
                buf.append("\t" + constant_pool.constantToString(index, constant_pool.getConstant(index).getTag()) + " (" + index + ")");
                break;
                /* Array of references.
       */

            case ANEWARRAY:
                index =  bytes.readUnsignedShort();
                buf.append("\t<" + compactClassName(constant_pool.getConstantString(index, CONSTANT_Class)) + "> (" + index + ")");
                break;
                /* Multidimensional array of references.
       */

            case MULTIANEWARRAY:
                {
                    index =  bytes.readUnsignedShort();
                    int dimensions =  bytes.readUnsignedByte();
                    buf.append("\t<" + compactClassName(constant_pool.getConstantString(index, CONSTANT_Class)) + ">\t" + dimensions + " (" + index + ")");
                }

                break;
                /* Increment local variable.
     */

            case IINC:
                if (wide) {
                    vindex =  bytes.readUnsignedShort();
                    constant =  bytes.readShort();
                    wide =  false;
                } else {
                    vindex =  bytes.readUnsignedByte();
                    constant =  bytes.readByte();
                }

                buf.append("\t%" + vindex + "\t" + constant);
                break;

            default:
                if (NO_OF_OPERANDS[opcode] > 0) {
                    for (int i =  0; i < TYPE_OF_OPERANDS[opcode].length; i++) {
                        buf.append("\t\t");

                        switch (TYPE_OF_OPERANDS[opcode][i]) {
                            case T_BYTE:
                                buf.append(bytes.readUnsignedByte());
                                break;

                            case T_SHORT:
                                buf.append(bytes.readShort());
                                break;

                            case T_INT:
                                buf.append(bytes.readInt());
                                break;

                            default: // Never reached
                                System.err.println("Unreachable default case reached!");
                                System.exit(-1);
                        }
                    }
                }
        }

        //System.out.println("ret:" + buf.toString());
        return  buf.toString();
    }


    /**
   * Shorten long class names, <em>java/lang/String</em> becomes 
   * <em>String</em>.
   *
   * @param str The long class name
   * @return Compacted class name
   */
    public static final String compactClassName (String str) {
        return  compactClassName(str, true);
    }


    /**
   * Shorten long class name <em>str</em>, i.e. chop off the <em>prefix</em>,
   * if the
   * class name starts with this string and the flag <em>chopit</em> is true.
   * Slashes <em>/</em> are converted to dots <em>.</em>.
   *
   * @param str The long class name
   * @param prefix The prefix the get rid off
   * @param chopit Flag that determines whether chopping is executed or not
   * @return Compacted class name
   */
    public static final String compactClassName (String str, String prefix, boolean chopit) {
        int len =  prefix.length();
        str =  str.replace('/', '.'); // Is `/' on all systems, even DOS

        if (chopit) {
            // If string starts with `prefix' and contains no further dots
            if (str.startsWith(prefix) && (str.substring(len).indexOf('.') == -1)) str =  str.substring(len);
        }

        return  str;
    }


    /**
   * Shorten long class names, <em>java/lang/String</em> becomes 
   * <em>java.lang.String</em>,
   * e.g.. If <em>chopit</em> is <em>true</em> the prefix <em>java.lang</em>
   * is also removed.
   *
   * @param str The long class name
   * @param chopit Flag that determines whether chopping is executed or not
   * @return Compacted class name
   */
    public static final String compactClassName (String str, boolean chopit) {
        return  compactClassName(str, "java.lang.", chopit);
    }


    private static final boolean is_digit (char ch) {
        return  (ch >= '0') && (ch <= '9');
    }


    private static final boolean is_space (char ch) {
        return  (ch == ' ') || (ch == '\t') || (ch == '\r') || (ch == '\n');
    }


    /**
   * Converts string containing the method return and argument types 
   * to a byte code method signature.
   *
   * @param  ret Return type of method
   * @param  argv Types of method arguements
   * @return Byte code representation of method type
   */
    public final static String methodTypeToSignature (String ret, String[] argv)
        throws ClassFormatError
    {
        StringBuffer buf =  new StringBuffer("(");
        String str;

        if (argv != null) for (int i =  0; i < argv.length; i++) {
            str =  typeToSignature(argv[i]);
            if (str.endsWith("V")) // void can't be a method argument
            throw  new ClassFormatError("Invalid type: " + argv[i]);
            buf.append(str);
        }

        str =  typeToSignature(ret);
        buf.append(")" + str);
        return  buf.toString();
    }


    /**
   * Gets Java conformant type like `String[]' and returns a string containing
   * the type in byte code format, i.e. String[] becomes [Ljava/lang/String;
   *
   * @param  str Type string like int[][]
   * @return Byte code representation of type like [[I
   */
    public final static String typeToSignature (String str)
        throws ClassFormatError
    {
        int index =  str.indexOf('[');
        String type, array =  null, code =  null;

        try {
            if (index > -1) { // Is an array?
                type =  str.substring(0, index);
                array =  str.substring(index);
            } else type =  str;

            if (array == null) // Not an array
            array =  ""; else {
                StringBuffer buf =  new StringBuffer();
                char ch, lastchar =  'X';

                for (int i =  0; i < array.length(); i++) {
                    ch =  array.charAt(i);

                    if (ch == '[') buf.append('['); else if ((ch == ']') || is_space(ch)) // Ignore
                    ; else if (is_digit(ch)) {
                        if ((lastchar == '[') || is_digit(lastchar)) // Then it's OK
                        buf.append(ch); else throw  new ClassFormatError("Invalid type: " + str);
                    } else throw  new ClassFormatError("Invalid type: " + str);

                    lastchar =  ch;
                }

                array =  buf.toString();
            }
        } catch (StringIndexOutOfBoundsException e) {
            throw  new ClassFormatError("Invalid type: " + str);
        }

        int i;

        for (i =  T_BOOLEAN; i <= T_VOID; i++) {
            if (type.equals(TYPE_NAMES[i])) {
                code =  SHORT_TYPE_NAMES[i];
                break;
            }
        }

        if (i == T_VOID) {
            if (array.startsWith("[")) // Array of void !?
            throw  new ClassFormatError("Invalid type: " + str);
        } else if (i > T_VOID) // Interpret as class name
        code =  "L" + type.replace('.', '/') + ";";

        return  array + code;
    }


    /**
   * @param  signature    Method signature
   * @return Array of argument types
   * @throw  ClassFormatError  
   */
    public static final String[] methodSignatureArgumentTypes (String signature)
        throws ClassFormatError
    {
        return  methodSignatureArgumentTypes(signature, true);
    }


    /**
   * @param  signature    Method signature
   * @param chopit Shorten class names ?
   * @return Array of argument types
   * @throw  ClassFormatError  
   */
    public static final String[] methodSignatureArgumentTypes (String signature, boolean chopit)
        throws ClassFormatError
    {
        java.util.Vector vec =  new java.util.Vector();
        int index;
        String[] types;

        try { // Read all declarations between for `(' and `)'
            if (signature.charAt(0) != '(') throw  new ClassFormatError("Invalid method signature: " + signature);
            index =  1; // current string position

            while (signature.charAt(index) != ')') {
                vec.addElement(signatureToString(signature.substring(index), chopit));
                index +=  consumed_chars; // update position
            }
        } catch (StringIndexOutOfBoundsException e) { // Should never occur
            throw  new ClassFormatError("Invalid method signature: " + signature);
        }

        types =  new String[vec.size()];
        vec.copyInto(types);
        return  types;
    }


    /**
   * @param  signature    Method signature
   * @return return type of method
   * @throw  ClassFormatError  
   */
    public static final String methodSignatureReturnType (String signature)
        throws ClassFormatError
    {
        return  methodSignatureReturnType(signature, true);
    }


    /**
   * @param  signature    Method signature
   * @param chopit Shorten class names ?
   * @return return type of method
   * @throw  ClassFormatError  
   */
    public static final String methodSignatureReturnType (String signature, boolean chopit)
        throws ClassFormatError
    {
        int index;
        String type;

        try {
            // Read return type after `)'
            index =  signature.lastIndexOf(')') + 1;
            type =  signatureToString(signature.substring(index), chopit);
        } catch (StringIndexOutOfBoundsException e) { // Should never occur
            throw  new ClassFormatError("Invalid method signature: " + signature);
        }

        return  type;
    }


    /**
   * Converts method signature to string with all class names compacted.
   *
   * @param signature to convert
   * @param name of method
   * @param access flags of method
   * @return Human readable signature
   */
    public static final String methodSignatureToString (String signature, String name, String access) {
        return  methodSignatureToString(signature, name, access, true);
    }


    /**
   * A return\u00adtype signature represents the return value from a method.
   * It is a series of bytes in the following grammar:
   *
   * <return_signature> ::= <field_type> | V
   *
   * The character V indicates that the method returns no value. Otherwise, the
   * signature indicates the type of the return value.
   * An argument signature represents an argument passed to a method:
   *
   * <argument_signature> ::= <field_type>
   *
   * A method signature represents the arguments that the method expects, and
   * the value that it returns.
   * <method_signature> ::= (<arguments_signature>) <return_signature>
   * <arguments_signature>::= <argument_signature>*
   *
   * This method converts such a string into a Java type declaration like
   * `void main(String[])' and throws a `ClassFormatError' when the parsed 
   * type is invalid.
   *
   * @param  signature    Method signature
   * @param  name         Method name
   * @param  access       Method access rights
   * @return Java type declaration
   * @throw  ClassFormatError  
   */
    public static final String methodSignatureToString (String signature, String name, String access, boolean chopit)
        throws ClassFormatError
    {
        StringBuffer buf =  new StringBuffer("(");
        String type;
        int index;

        try { // Read all declarations between for `(' and `)'
            if (signature.charAt(0) != '(') throw  new ClassFormatError("Invalid method signature: " + signature);
            index =  1; // current string position

            while (signature.charAt(index) != ')') {
                buf.append(signatureToString(signature.substring(index), chopit) + ", ");
                index +=  consumed_chars; // update position
            }

            index++; // update position
            // Read return type after `)'
            type =  signatureToString(signature.substring(index), chopit);
        } catch (StringIndexOutOfBoundsException e) { // Should never occur
            throw  new ClassFormatError("Invalid method signature: " + signature);
        }

        if (buf.length() > 1) // Tack off the extra ", "
        buf.setLength(buf.length() - 2);
        buf.append(")");
        return  access + ((access.length() > 0)? " " : "") + // May be an empty string
        type + " " + name + buf.toString();
    }


    // Guess what this does
    private static final int pow2 (int n) {
        return  1 << n;
    }


    /**
   * Replace all occurences of <em>old</em> in <em>str</em> with <em>new</em>.
   *
   * @param str String to permute
   * @param old String to be replaced
   * @param new Replacement string
   * @return new String object
   */
    public static final String replace (String str, String old, String new_) {
        int index, old_index;
        StringBuffer buf =  new StringBuffer();
        ;

        try {
            if ((index =  str.indexOf(old)) != -1) { // `old' found in str
                old_index =  0; // String start offset

                // While we have something to replace
                while ((index =  str.indexOf(old, old_index)) != -1) {
                    buf.append(str.substring(old_index, index)); // append prefix
                    buf.append(new_); // append replacement
                    old_index =  index + old.length(); // Skip `old'.length chars
                }

                buf.append(str.substring(old_index)); // append rest of string
                str =  buf.toString();
            } else return  str; // Nothing changed
        } catch (StringIndexOutOfBoundsException e) { // Should not occur
            System.err.println(e);
        }

        return  buf.toString();
    }


    /**
   * Converts signature to string with all class names compacted.
   *
   * @param signature to convert
   * @return Human readable signature
   */
    public static final String signatureToString (String signature) {
        return  signatureToString(signature, true);
    }


    /**
   * The field signature represents the value of an argument to a function or 
   * the value of a variable. It is a series of bytes generated by the 
   * following grammar:
   *
   * <field_signature> ::= <field_type>
   * <field_type>      ::= <base_type>|<object_type>|<array_type>
   * <base_type>       ::= B|C|D|F|I|J|S|Z
   * <object_type>     ::= L<fullclassname>;
   * <array_type>      ::= [<optional_size><field_type>
   * <optional_size>   ::= [0\u00ad9]*
   *
   * The meaning of the base types is as follows:
   * B byte signed byte
   * C char character
   * D double double precision IEEE float
   * F float single precision IEEE float
   * I int integer
   * J long long integer
   * L<fullclassname>; ... an object of the given class
   * S short signed short
   * Z boolean true or false
   * [<field sig> ... array
   *
   * This method converts this string into a Java type declaration such as
   * `String[]' and throws a `ClassFormatError' when the parsed type is 
   * invalid.
   *
   * @param  signature    Class signature
   * @return Java type declaration
   * @throw  ClassFormatError
   */
    public static final String signatureToString (String signature, boolean chopit)
        throws ClassFormatError
    {
        consumed_chars =  1; // This is the default, read just one char like `B'

        try {
            switch (signature.charAt(0)) {
                case 'B':
                    return  "byte";

                case 'C':
                    return  "char";

                case 'D':
                    return  "double";

                case 'F':
                    return  "float";

                case 'I':
                    return  "int";

                case 'J':
                    return  "long";

                case 'L':
                    { // Full class name
                        int index =  signature.indexOf(';'); // Look for closing `;'
                        if (index < 0) throw  new ClassFormatError("Invalid signature: " + signature);
                        consumed_chars =  index + 1; // "Lblabla;" `L' and `;' are removed
                        return  compactClassName(signature.substring(1, index), chopit);
                    }

                case 'S':
                    return  "short";

                case 'Z':
                    return  "boolean";

                case '[':
                    { // Array declaration
                        int n;
                        StringBuffer buf, brackets;
                        String type;
                        char ch;
                        int consumed_chars; // Shadows global var
                        brackets =  new StringBuffer(); // Accumulate []'s

                        // Count opening brackets and look for optional size argument
                        for (n =  0; n < signature.length(); n++) {
                            ch =  signature.charAt(n);
                            if (ch != '[') break;
                            brackets.append('[');
                            /* If this raises an exception the signature is invalid anyway, 
	   * because `[' must be followed by non-null `<field_type>'
	   */
                            ch =  signature.charAt(++n);

                            if (is_digit(ch)) { // Check for optional size argument
                                buf =  new StringBuffer();
                                // Append all digits
                                for (; is_digit(ch); ch =  signature.charAt(++n)) buf.append(ch);
                                brackets.append(buf.toString()); // Add size
                            }

                            n--; // Reset value since it is incremented by the for loop
                            brackets.append(']'); // Close nesting level
                        }

                        consumed_chars =  n; // Remember value
                        // The rest of the string denotes a `<field_type>'
                        type =  signatureToString(signature.substring(n), chopit);
                        Utility.consumed_chars +=  consumed_chars;
                        return  type + brackets.toString();
                    }

                case 'V':
                    return  "void";

                default:
                    throw  new ClassFormatError("Invalid signature: `" + signature + "'");
            }
        } catch (StringIndexOutOfBoundsException e) { // Should never occur
            throw  new ClassFormatError("Invalid signature: " + e + ":" + signature);
        }
    }


    /**
   * @param  signature in format described above
   * @return type of method signature
   * @see    Constants
   * @throw  ClassFormatError
   */
    public static final byte typeOfMethodSignature (String signature)
        throws ClassFormatError
    {
        int index;

        try {
            if (signature.charAt(0) != '(') throw  new ClassFormatError("Invalid method signature: " + signature);
            index =  signature.lastIndexOf(')') + 1;
            return  typeOfSignature(signature.substring(index));
        } catch (StringIndexOutOfBoundsException e) {
            throw  new ClassFormatError("Invalid method signature: " + signature);
        }
    }


    /**
   * Return type of signature as a byte value as defined in <em>Constants</em>
   * @param  signature in format described above
   * @return type of signature
   * @see    Constants
   * @throw  ClassFormatError
   */
    public static final byte typeOfSignature (String signature)
        throws ClassFormatError
    {
        try {
            switch (signature.charAt(0)) {
                case 'B':
                    return  T_BYTE;

                case 'C':
                    return  T_CHAR;

                case 'D':
                    return  T_DOUBLE;

                case 'F':
                    return  T_FLOAT;

                case 'I':
                    return  T_INT;

                case 'J':
                    return  T_LONG;

                case 'L':
                    return  T_REFERENCE;

                case '[':
                    return  T_ARRAY;

                case 'V':
                    return  T_VOID;

                case 'Z':
                    return  T_BOOLEAN;

                default:
                    throw  new ClassFormatError("Invalid method signature: " + signature);
            }
        } catch (StringIndexOutOfBoundsException e) {
            throw  new ClassFormatError("Invalid method signature: " + signature);
        }
    }


    /*
   * @return bytes as hexidecimal string, e.g. 00 FA 12 ...
   */
    public static final String toHexString (byte[] bytes) {
        StringBuffer buf =  new StringBuffer();

        for (int i =  0; i < bytes.length; i++) {
            short b =  byteToShort(bytes[i]);
            String hex =  Integer.toString(b, 0x10);
            if (b < 0x10) // just one digit, prepend '0'
            buf.append('0');
            buf.append(hex);
            if (i < bytes.length - 1) buf.append(' ');
        }

        return  buf.toString();
    }

}

