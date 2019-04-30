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

public class NullType extends ReferenceType {

	public boolean identicalTo(Type other) {
		return (other instanceof NullType);
	}

	public boolean subTypeOf(Type other) {
		return (identicalTo(other) || other instanceof ClassType
			|| other instanceof ArrayType);
	}
	
	public boolean superTypeOf(Type other) {
		return identicalTo(other);
	}

	public int distanceTo(Type other) {
		return (other instanceof NullType || other instanceof ClassType
			|| other instanceof ArrayType) ? 0 : Integer.MIN_VALUE;
	}
	
	public String toString() {
		return Type.NullRep;
	}

	/**
	  * Map an Espresso type to a ClassGen one.
	  */
	public ClassGenType toClassFileType() {
		return null;		// should never be called !
	}

	public Instruction IFEQ(boolean tozero) {
		return tozero ? (Instruction) new IFNULL(null) : 
						(Instruction) new IF_ACMPEQ(null);	
	}

	public Instruction IFNE(boolean tozero) {
		return tozero ? (Instruction) new IFNONNULL(null) : 
						(Instruction) new IF_ACMPNE(null);	
	}

	public InstructionList VALUEOF(ConstantPoolGen cpg) {
		Symbol mstring = new Symbol("java.lang.String");
		Symbol mbuffer = new Symbol("java.lang.StringBuffer");

		InstructionList il = new InstructionList();
		MethodType mtype = new MethodType(Type.Void,new ClassType("java.lang.String"));
		int index = cpg.addMethodref(mbuffer.toInternalString(), "<init>", 
		mtype.toInternalString());
		il.append(new INVOKESPECIAL(index));
		return il;			
	}
	
	public Instruction APPENDSTB(ConstantPoolGen cpg) {
		Symbol mstring = new Symbol("java.lang.String");
		Symbol mbuffer = new Symbol("java.lang.StringBuffer");

		MethodType mtype = new MethodType(new ClassType(mbuffer), new ClassType(mstring));
		int index = cpg.addMethodref(mbuffer.toInternalString(), "append", 
		mtype.toInternalString());
		return new INVOKEVIRTUAL(index);	
	}

}
