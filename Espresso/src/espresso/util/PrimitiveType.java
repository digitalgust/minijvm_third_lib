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

public abstract class PrimitiveType extends Type {

	public abstract boolean integralType();
	public abstract boolean floatingPointType();	
	public abstract int distanceTo(Type other);

	public abstract ClassGenType toClassFileType();

	public boolean subTypeOf(Type other) {
		return identicalTo(other);
	}
	
	public boolean superTypeOf(Type other) {
		return identicalTo(other);
	}

	public boolean referenceType() {
		return false;
	}

	public boolean numericType() {
		return !(this instanceof BooleanType || this instanceof VoidType );
	}
	
	public boolean primitiveType() {
		return true;
	}

	/*
	 * JVM instructions.
	 */

	public Instruction DUP() {
		return new DUP();
	}

	public Instruction DUPX1() {
		return new DUP_X1();
	}	 

	public Instruction DUPX2() {
		return new DUP_X2();
	}

	public Instruction POP() {
		return new POP();
	}

	public InstructionList VALUEOF(ConstantPoolGen cpg) {
		Symbol mstring = new Symbol("java.lang.String");
		Symbol mbuffer = new Symbol("java.lang.StringBuffer");

		InstructionList il = new InstructionList();
		Symbol mname = new Symbol("valueOf");
		MethodType mtype = new MethodType(new ClassType(mstring),
												  this);
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
		MethodType mtype = new MethodType(new ClassType(mbuffer), this);
		int index = cpg.addMethodref(mbuffer.toInternalString(), "append", 
		mtype.toInternalString());
		return new INVOKEVIRTUAL(index);
	
	}
		
}
