package  espresso.classfile.classgen;




import  espresso.classfile.Constants;
import  espresso.classfile.javaclass.*;
// Import regex classes either
 import com.stevesoft.pat.*; // newer version
// or
// import pat.*; // old version
import  java.util.Enumeration;




/** 
 * This class is an utility to search for given patterns, i.e. regular expressions
 * in an instruction list. This can be used in order to implement a
 * peep hole optimizer that looks for code patterns and replaces them with
 * faster equivalents.
 *
 * This class internally uses the package <a href="http://www.win.net/~stevesoft/pat/">
 * COM.stevesoft.pat</a> to search for regular expressions. Pat comes as a
 * distribution of its own rights.
 *
 * @version 980116
 * @author  <A HREF="http://www.inf.fu-berlin.de/~dahm">M. Dahm</A>
 * @see     Instruction
 * @see     InstructionList
 * @see     Regex
 * @see     CodeConstraint
 */
public final class FindPattern
    implements Constants
{

    private static final int OFFSET =  32767; // char + OFFSET is outside of LATIN-1
    private static final int NO_OPCODES =  256; // Potential number, some are not used
    private static String instruction_pattern; // matches any Instruction
    private static String binstruction_pattern; // matches any BranchInstruction
    private static String if_icmp_pattern; // matches any IF_ICMP__ instruction
    private static String if_pattern; // matches any IF__ instruction
    private static String push_pattern; // matches any ICONST, LDC, ...
    private static String iload_pattern, aload_pattern, fload_pattern, dload_pattern, lload_pattern, istore_pattern, astore_pattern, fstore_pattern, dstore_pattern, lstore_pattern; // As the name suggests ...
    private static final String[] patterns =  {"instruction", "branchinstruction", "if_icmp__", "if__", "push", "iload__", "aload__", "fload__", "dload__", "lload__", "istore__", "astore__", "fstore__", "dstore__", "lstore__"};
    private static String[] pattern_map; // filled in by static initializer, see below
    private InstructionList il;
    private String il_string; // instruction list as string
    private Regex regex; // Last regular expression used
    private InstructionHandle[] handles; // map instruction list to array
    private int match_length; // Number of matched instructions
    private int matched_from; // Index of match in instruction list


    /**
   * @param il instruction list to search for given patterns
   */
    public FindPattern (InstructionList il) {
        this.il =  il;
        reread();
    }


    /**
   * Rereads the instruction list, e.g., if you've altered the list upon a match.
   */
    public final void reread () {
        int size =  il.getLength();
        char[] buf =  new char[size]; // Create a string with length equal to il length
        handles =  new InstructionHandle[size];
        match_length =  -1; // reset match_length
        int j =  0;

        for (Enumeration e =  il.elements(); e.hasMoreElements();) {
            InstructionHandle ih =  (InstructionHandle)e.nextElement();
            Instruction i =  ih.getInstruction();
            buf[j] =  (char)(i.getTag() + OFFSET); // This ensures that meta characters
            handles[j] =  ih; // such as |,[,], etc. can't be used
            j++;
        }

        il_string =  new String(buf);
    }


    /**
   * Make a pattern such as `branchinstruction' a regular expression string
   * such as (a|b|z) (where a,b,c whil be non-printable characters in LATIN-1)
   *
   * @param pattern instruction pattern in lower case
   * @return encoded string for a pattern such as "BranchInstruction".
   */
    private static final String getPattern (String pattern) {
        // Check for abbreviations
        for (int i =  0; i < patterns.length; i++) {
            if (pattern.equals(patterns[i])) return  pattern_map[i]; // return the string mapped to that name
        }

        // Check for opcode names
        for (int i =  0; i < NO_OPCODES; i++) {
            if (pattern.equals(OPCODE_NAMES[i])) {
                char[] ch =  {(char)(i + OFFSET)};
                return  new String(ch);
            }
        }

        return  null; // Failed to match
    }


    /**
   * Replace all occurences of `something' with the appropiate pattern, the `' chars
   * are used as an escape sequence.
   * Other characters than the escaped one will be ignored, in particular the meta
   * characters used for regular expression such as *, +, [, etc.
   *
   * @param pattern The pattern to compile
   * @return complete regular expression string
   */
    private static final String makePattern (String pattern) {
        String lower =  pattern.toLowerCase();
        StringBuffer buf =  new StringBuffer();
        int size =  pattern.length();
        boolean in_pattern =  false; // remember current state
        StringBuffer collect =  null;

        try {
            for (int i =  0; i < size; i++) {
                char ch =  lower.charAt(i);

                switch (ch) {
                    case '`': // Start of instruction pattern
                        if (in_pattern) throw  new ClassGenException("` within `' block.");
                        collect =  new StringBuffer();
                        in_pattern =  true; // remember current state
                        break;

                    case '\'': // end of instruction pattern
                        if (!in_pattern) throw  new ClassGenException("' without starting `.");
                        in_pattern =  false;
                        String str =  collect.toString(); // String within the `'
                        String pat =  getPattern(str);
                        if (pat == null) throw  new ClassGenException("Unknown instruction pattern: \"" + str + "\"" + " at index " + i);
                        buf.append(pat);
                        break;

                    default:
                        if (in_pattern) collect.append(ch); else buf.append(ch); // Just append it (meta character)
                }
            }
        } catch (StringIndexOutOfBoundsException e) {
            e.printStackTrace();
        }

        return  buf.toString();
    }


    /**
   * Search for the given pattern in the InstructionList. You may use the following
   * special expressions in your pattern string which match instructions that belong
   * to the denoted class. The `' are an escape and <b>must not</b> be omitted.
   *
   * You can use the Instruction names directly:
   *
   * `ILOAD_1', `GOTO', 'NOP', etc..
   *
   * For convenience there exist some abbreviations for instructions that belong
   * to the same group (underscores _ are used as some kind of wildcards):
   *
   * `Instruction', `BranchInstruction'
   * `IF_ICMP__', `IF__', where __ stands for EQ, LE, etc.
   * `xLOAD__', `xSTORE__', where x stands for I, D, F, L or A. __ is 0..3 or empty
   * `PUSH' stands for any LDC, xCONST__, SIPUSH or BIPUSH instruction
   *
   * You <B>must</B> put the `' around these words or they can't be matched correctly.
   *
   * For the rest the usual (PERL) pattern matching rules apply.<P>
   * Example pattern:
   * <pre>
     search("(`BranchInstruction')`NOP'((`IF_ICMP__'|`GOTO')+`ISTORE__'`Instruction')*");
   * </pre>
   *
   *
   * @param pattern the instruction pattern to search for, case is ignored
   * @param from where to start the search in the instruction list
   * @param constraint optional CodeConstraint to check the found code pattern for
   * given constraints
   * @return instruction handle or `null' if the matching fails
   */
    public final InstructionHandle search (String pattern, InstructionHandle from, CodeConstraint constraint) {
        String search =  makePattern(pattern);
        int start =  -1;
        match_length =  matched_from =  -1; // reset

        for (int i =  0; i < handles.length; i++) if (handles[i] == from) {
            start =  i; // Where to start search from (index)
            break;
        }

        if (start == -1) throw  new ClassGenException("Instruction handle " + from + " not found in instruction list.");
        // Search using regex package
        regex =  new Regex(search);
        regex.optimize();

        if (regex.searchFrom(il_string, start)) { // Match succeeded
            RegRes r =  regex.result();
            matched_from =  r.matchFrom(); // Index in instruction list
            match_length =  r.charsMatched(); // Set number of matched instructions
            if ((constraint != null) && constraint.checkCode(getMatch())) return  handles[matched_from];
        }

        return  null;
    }


    /**
   * Start search beginning from the start of the given instruction list.
   *
   * @param pattern the instruction pattern to search for, case is ignored
   * @return instruction handle or `null' if the matching fails
   */
    public final InstructionHandle search (String pattern) {
        return  search(pattern, il.getStart(), null);
    }


    /**
   * Start search beginning from `from'.
   *
   * @param pattern the instruction pattern to search for, case is ignored
   * @param from where to start the search in the instruction list
   * @return instruction handle or `null' if the matching fails
   */
    public final InstructionHandle search (String pattern, InstructionHandle from) {
        return  search(pattern, from, null);
    }


    /**
   * Start search beginning from the start of the given instruction list.
   * Check found matches with the constraint object.
   *
   * @param pattern the instruction pattern to search for, case is ignored
   * @param constraint constraints to be checked on matching code
   * @return instruction handle or `null' if the match failed
   */
    public final InstructionHandle search (String pattern, CodeConstraint constraint) {
        return  search(pattern, il.getStart(), constraint);
    }


    /**
   * @return number of matched instructions, or -1 if the match did not succeed
   */
    public final int getMatchLength () {
        return  match_length;
    }


    /**
   * @return the matched piece of code as an array of instruction (handles)
   */
    public final InstructionHandle[] getMatch () {
        if (match_length == -1) throw  new ClassGenException("Nothing matched.");
        InstructionHandle[] match =  new InstructionHandle[match_length];
        System.arraycopy(handles, matched_from, match, 0, match_length);
        return  match;
    }


    /**
   * Defines a new instruction list. Automatically calls
   * <a href="#reread()">reread()</a> to update the object.
   *
   * @param il the new instuction list
   */
    public final void setInstructionList (InstructionList il) {
        this.il =  il;
        reread();
    }


    /**
   * Internal debugging routines.
   */
    private static final String pattern2string (String pattern) {
        return  pattern2string(pattern, true);
    }


    private static final String pattern2string (String pattern, boolean make_string) {
        StringBuffer buf =  new StringBuffer();

        for (int i =  0; i < pattern.length(); i++) {
            char ch =  pattern.charAt(i);

            if (ch >= OFFSET) {
                if (make_string) buf.append(OPCODE_NAMES[ch - OFFSET]); else buf.append((int)(ch - OFFSET));
            } else buf.append(ch);
        }

        return  buf.toString();
    }


    /* Static initializer.
   */
    static {
        StringBuffer buf;
        /* Make instruction string
     */
        buf =  new StringBuffer("(");

        for (int i =  0; i < NO_OPCODES; i++) {
            if (NO_OF_OPERANDS[i] != UNDEFINED) { // Not an invalid opcode
                buf.append((char)(i + OFFSET));
                if (i < NO_OPCODES - 1) buf.append('|');
            }
        }

        buf.append(')');
        instruction_pattern =  buf.toString();
        /* Make BranchInstruction string
     */
        appendPatterns(buf =  new StringBuffer("("), LCMP, LOOKUPSWITCH);
        buf.append('|');
        appendPatterns(buf, IFNULL, JSR_W);
        buf.append(')');
        binstruction_pattern =  buf.toString();
        /* Make IF_ICMP__ pattern string
     */
        appendPatterns(buf =  new StringBuffer("("), IF_ICMPEQ, IF_ICMPLE);
        buf.append(')');
        if_icmp_pattern =  buf.toString();
        /* Make IF__ pattern string
     */
        appendPatterns(buf =  new StringBuffer("("), IFEQ, IFLE);
        buf.append(')');
        if_pattern =  buf.toString();
        /* Make PUSH pattern string
     */
        appendPatterns(buf =  new StringBuffer("("), ACONST_NULL, LDC2_W);
        buf.append(')');
        push_pattern =  buf.toString();
        /* Make ILOAD__ pattern string
     */
        appendPatterns(buf =  new StringBuffer("("), ILOAD_0, ILOAD_3);
        buf.append('|');
        buf.append((char)(ILOAD + OFFSET));
        buf.append(')');
        iload_pattern =  buf.toString();
        /* Make ALOAD__ pattern string
     */
        appendPatterns(buf =  new StringBuffer("("), ALOAD_0, ALOAD_3);
        buf.append('|');
        buf.append((char)(ALOAD + OFFSET));
        buf.append(')');
        aload_pattern =  buf.toString();
        /* Make FLOAD__ pattern string
     */
        appendPatterns(buf =  new StringBuffer("("), FLOAD_0, FLOAD_3);
        buf.append('|');
        buf.append((char)(FLOAD + OFFSET));
        buf.append(')');
        fload_pattern =  buf.toString();
        /* Make DLOAD__ pattern string
     */
        appendPatterns(buf =  new StringBuffer("("), DLOAD_0, DLOAD_3);
        buf.append('|');
        buf.append((char)(DLOAD + OFFSET));
        buf.append(')');
        dload_pattern =  buf.toString();
        /* Make LLOAD__ pattern string
     */
        appendPatterns(buf =  new StringBuffer("("), LLOAD_0, LLOAD_3);
        buf.append('|');
        buf.append((char)(LLOAD + OFFSET));
        buf.append(')');
        lload_pattern =  buf.toString();
        /* Make ISTORE__ pattern string
     */
        appendPatterns(buf =  new StringBuffer("("), ISTORE_0, ISTORE_3);
        buf.append('|');
        buf.append((char)(ISTORE + OFFSET));
        buf.append(')');
        istore_pattern =  buf.toString();
        /* Make ASTORE__ pattern string
     */
        appendPatterns(buf =  new StringBuffer("("), ASTORE_0, ASTORE_3);
        buf.append('|');
        buf.append((char)(ASTORE + OFFSET));
        buf.append(')');
        astore_pattern =  buf.toString();
        /* Make FSTORE__ pattern string
     */
        appendPatterns(buf =  new StringBuffer("("), FSTORE_0, FSTORE_3);
        buf.append('|');
        buf.append((char)(FSTORE + OFFSET));
        buf.append(')');
        fstore_pattern =  buf.toString();
        /* Make DSTORE__ pattern string
     */
        appendPatterns(buf =  new StringBuffer("("), DSTORE_0, DSTORE_3);
        buf.append('|');
        buf.append((char)(DSTORE + OFFSET));
        buf.append(')');
        dstore_pattern =  buf.toString();
        /* Make LSTORE__ pattern string
     */
        appendPatterns(buf =  new StringBuffer("("), LSTORE_0, LSTORE_3);
        buf.append('|');
        buf.append((char)(LSTORE + OFFSET));
        buf.append(')');
        lstore_pattern =  buf.toString();
        String[] pat_map =  {instruction_pattern, binstruction_pattern, if_icmp_pattern, if_pattern, push_pattern, iload_pattern, aload_pattern, fload_pattern, dload_pattern, lload_pattern, istore_pattern, astore_pattern, fstore_pattern, dstore_pattern, lstore_pattern};
        pattern_map =  pat_map;
    }


    /**
   * Append instructions characters starting from `start' to `to'.
   */
    private final static void appendPatterns (StringBuffer buf, short from, short to) {
        for (int i =  from; i <= to; i++) {
            buf.append((char)(i + OFFSET));
            if (i < to) buf.append('|');
        }
    }


    /**
   * @return the inquired instruction list
   */
    public final InstructionList getInstructionList () {
        return  il;
    }

}

