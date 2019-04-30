/**
 *
 * Copyright (C) 1998 Boston University
 *
 * Use of this file and the system it is part of is constrained by the
 * file COPYRIGHT in the root directory of this system.  You may, however,
 * make any modifications you wish to this file.
 *
 * Author: Santiago M. Pericas
 * Date: 3/7/98
 *
 */

package espresso.util;

import espresso.classfile.classgen.*;

import java.util.Vector;
import java.util.Hashtable;
import java.io.IOException;
import java.io.StringReader;
import java.math.BigDecimal;

public abstract class Type {

 	final static int CACHE_SIZE = 31;
	static Hashtable cache_d = new Hashtable(CACHE_SIZE);
 
    final static protected String VoidRep    = "V";
    final static protected String ByteRep    = "B";
    final static protected String CharRep    = "C";
    final static protected String DoubleRep  = "D";
    final static protected String FloatRep   = "F";
    final static protected String IntRep     = "I";
    final static protected String LongRep    = "J";
    final static protected String ShortRep   = "S";
    final static protected String BooleanRep = "Z";
	final static protected String NullRep	 = "<null>";

    final static public Type Throwable = new ClassType("java.lang.Throwable");
    final static public Type Exception = new ClassType("java.lang.Exception");
    final static public Type Error = new ClassType("java.lang.Error");
    final static public Type RuntimeException = new ClassType("java.lang.RuntimeException");

    final static public Type Null   = new NullType();
    final static public Type Void 	= new VoidType();
    final static public Type Byte 	= new ByteType();
    final static public Type Char 	= new CharType();
    final static public Type Double = new DoubleType();
    final static public Type Float	= new FloatType();
    final static public Type Int	= new IntType();
    final static public Type Long	= new LongType();
    final static public Type Short	= new ShortType();
    final static public Type Boolean = new BooleanType();

 	Type() {
	}
	
	public abstract boolean subTypeOf(Type other);
	public abstract boolean superTypeOf(Type other);
	public abstract boolean identicalTo(Type other);
	public abstract int distanceTo(Type other);

	public abstract boolean referenceType();
	public abstract boolean primitiveType();
	public abstract boolean numericType();
	public abstract boolean integralType();
	public abstract boolean floatingPointType();

	public abstract String toString();
	
	public String toInternalString() {
		return toString().replace('.','/');
	}
	
	public abstract ClassGenType toClassFileType();

	/**
	  *	Determines if two ref types belong to the same branch of the
	  * subtype relation hierarchy.
	  */
	public boolean relatedTo(Type other) {
		return (subTypeOf(other) || other.subTypeOf(this));
	}

	public static Type createType(Symbol rep) {
		return createType(rep.toString());
	}
	
	public static Type createType(String rep) {

 		Type result = (Type) cache_d.get(rep);

 		if (result == null) {
			try { 		
				result = parseType(new StringReader(rep));
				cache_d.put(rep, result);
			} 
			catch (IOException e) {
			}
		}	
			
		return result;
	}
	 	
	/**
	  * Search for class types without 'L' and ';'. This is useful
	  * to use the cache after types have been parsed.
	  */
	public static ClassType createClassType(String rep) {
		rep = 'L' + rep + ';';
		return (ClassType) createType(rep);
	}

	public static ClassType createClassType(Symbol rep) {
		return createClassType(rep.toString());
	}
				 	
 	static {
 		// Add primitive types to the cache
 		cache_d.put(VoidRep, new VoidType());
 		cache_d.put(ByteRep, new ByteType()) ;
 		cache_d.put(CharRep, new CharType());
 		cache_d.put(DoubleRep, new DoubleType());
 		cache_d.put(FloatRep, new FloatType());
 		cache_d.put(IntRep, new IntType());
 		cache_d.put(LongRep, new LongType());
 		cache_d.put(ShortRep, new ShortType());
 		cache_d.put(BooleanRep, new BooleanType());	 		 		
 	}
 	
 	static Type parseType(StringReader buffer) throws IOException {

		buffer.mark(0);
		char ch = (char) buffer.read();
		String rep = String.valueOf(ch);
		
		// Search for a primitive type using the cache
		Type result = (Type) cache_d.get(rep);

		if (result == null) {
			switch (ch) {
			case 'L':
				StringBuffer temp = new StringBuffer();
				while ((ch = (char) buffer.read()) != ';') {
					temp.append(ch);
				}
				result = new ClassType(temp.toString());
			break;
			
			case '[':
				int dims = 0;
				do {
					dims++;
					buffer.mark(0);
				} while ((ch = (char) buffer.read()) == '[');

				buffer.reset();			// unget()
				Type baseType = parseType(buffer);				
				result = new ArrayType(baseType, dims);			
			break;
			
			case '(':
				Vector argsType = new Vector();
				buffer.mark(0);
				do {
					buffer.reset();		// unget(')') after first iteration
					Type argt = parseType(buffer);
					if (argt != null)
						argsType.addElement(argt);
					buffer.mark(0);
				} while ((ch = (char) buffer.read()) != ')');

				Type resultType = parseType(buffer);
				result = new MethodType(resultType, argsType);
			break;
			
			case ')':
				buffer.reset();			// unget(')')
			break;
				
			default:	
				// throw ParseTypeError
			}
		}
 		
 		return result;
	}

        public Symbol name() {
	    return new Symbol(toString());
	}

        /**
	  * Determines if this type can be thrown as an exception. Defaults
	  * to false but overridden in ClassType
	  */
        public boolean throwable() {
	    return false;
	}

	/**
	  * Determines if vars of this type fit in one word. Defaults to true
	  * but overriden at DoubleType and LongType.
	  */
	public boolean oneWord() {
	    return true;
	}
	
	/**
	  * Determines if vars of this type need two words. Defaults to false
	  * but overriden at DoubleType and LongType.
	  */
	public boolean twoWords() {
		return false;
	}

	/*
	 * JVM instructions. The methods defined in this class should never
	 * be called.
	 */

	public Instruction DUP() {
		return null;
	}
	 
	public Instruction DUPX1() {
		return null;
	}
	 
	public Instruction DUPX2() {
		return null;
	}
	 
	public Instruction RETURN() {
		return null;
	}

	public Instruction LOAD(int slot) {
		return null;
	}

	public Instruction STORE(int slot) {
		return null;
	}

	public Instruction ALOAD() {
		return null;
	}

	public Instruction ASTORE() {
		return null;
	}

	public Instruction CMP(boolean less) {
		return null;
	}
	
	public Instruction IFGT(boolean tozero) {
		return null;
	}

	public Instruction IFLT(boolean tozero) {
		return null;
	}

	public Instruction IFGE(boolean tozero) {
		return null;
	}

	public Instruction IFLE(boolean tozero) {
		return null;
	}

	public Instruction IFEQ(boolean tozero) {
		return null;
	}

	public Instruction IFNE(boolean tozero) {
		return null;
	}

	public Instruction POP() {
		return null;
	}
	
	public Instruction PUSH(ConstantPoolGen cpg, BigDecimal value) {
		return null;
	}
	
	public Instruction ADD() {
		return null;
	}

	public Instruction SUB() {
		return null;
	}

	public Instruction MUL() {
		return null;
	}

	public Instruction DIV() {
		return null;
	}

	public Instruction REM() {
		return null;
	}

	public Instruction NEG() {
		return null;
	}

	public Instruction AND() {
		return null;
	}

	public Instruction OR() {
		return null;
	}

	public Instruction XOR() {
		return null;
	}

	public Instruction SHL() {
		return null;
	}

	public Instruction SHR() {
		return null;
	}

	public Instruction USHR() {
		return null;
	}

	public Instruction NEWARRAY(int index) {
		return null;
	}
	
	public InstructionList CAST(Type other) {
		return null;	
	}

	public InstructionList VALUEOF(ConstantPoolGen cpg) {
		return null;
	}

	public Instruction APPENDSTB(ConstantPoolGen cpg) {
		return null;
	}
		
	public void ADDSTRING(ClassGen classGen, MethodGen methodGen, boolean top ) {
	}
}
