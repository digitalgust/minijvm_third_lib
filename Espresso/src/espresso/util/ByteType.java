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

import espresso.classfile.Constants;
import espresso.classfile.classgen.*;

public class ByteType extends IntegralType {

	public boolean identicalTo(Type other) {
		return (other instanceof ByteType);
	}

	public int distanceTo(Type other) {
		int result = Integer.MIN_VALUE;
		
		if (other instanceof ByteType) {
			result = 0;
		}
		else if (other instanceof ShortType) {
			result = 1;
		}
		else if (other instanceof IntType) {
			result = 2;
		}
		else if (other instanceof LongType) {
			result = 3;
		}
		else if (other instanceof FloatType) {
			result = 4;
		}
		else if (other instanceof DoubleType) {
			result = 5;
		}
		
		return result;
	}

	public boolean inRange(long value) {
		return value >= java.lang.Byte.MIN_VALUE && 
		       value <= java.lang.Byte.MAX_VALUE;
	}
		
	public String toString() {
		return Type.ByteRep;
	}

	/**
	  * Map an Espresso type to a ClassGen one.
	  */
	public ClassGenType toClassFileType() {
		return new ClassGenType(Constants.T_BYTE);
	}

	/*
	 * JVM instructions.
	 */

	public Instruction ALOAD() {
		return new BALOAD();
	}

	public Instruction ASTORE() {
		return new BASTORE();
	}
	 
	public Instruction NEWARRAY(int index) {
		return new NEWARRAY(Constants.T_BYTE);
	}

	public InstructionList CAST(Type other) {
		InstructionList il = new InstructionList();

		if (other instanceof ByteType) {
			il.append(new NOP());
		} else { 
			il.append(Type.Int.CAST(other));
		}

		return il;
	}

	public InstructionList VALUEOF(ConstantPoolGen cpg) {
		Symbol mstring = new Symbol("java.lang.String");
		Symbol mbuffer = new Symbol("java.lang.StringBuffer");

		InstructionList il = new InstructionList();
		Symbol mname = new Symbol("valueOf");
		MethodType mtype = new MethodType(new ClassType(mstring),
												  Type.Int);
		int index = cpg.addMethodref(mstring.toInternalString(), 
			   		mname.toString(), mtype.toInternalString());
		il.append(new INVOKESTATIC(index));				
			
		mtype = new MethodType(Type.Void,new ClassType(mstring));
		index = cpg.addMethodref(mbuffer.toInternalString(), "<init>", 
		mtype.toInternalString());
		il.append(new INVOKESPECIAL(index));
		return il;
	
	}

	public Instruction APPENDSTB(ConstantPoolGen cpg) {
		Symbol mbuffer = new Symbol("java.lang.StringBuffer");
		MethodType mtype = new MethodType(new ClassType(mbuffer), Type.Int);
		int index = cpg.addMethodref(mbuffer.toInternalString(), "append", 
		mtype.toInternalString());
		return new INVOKEVIRTUAL(index);
	
	}
	
}
