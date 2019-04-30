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

public abstract class ReferenceType extends Type {

	public abstract boolean subTypeOf(Type other);
	public abstract boolean superTypeOf(Type other);
	public abstract boolean identicalTo(Type other);
	public abstract int distanceTo(Type other);
	
	public boolean primitiveType() {
		return false;
	}

	public boolean referenceType() {
		return true;
	}

	public boolean numericType() {
		return false;
	}

	public boolean integralType() {
		return false;
	}

	public boolean floatingPointType() {
		return false;
	}

	/*
	 * JVM instruction.
	 */
	public Instruction ALOAD() {
		return new AALOAD();
	}

	public Instruction ASTORE() {
		return new AASTORE();
	}

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

	public Instruction LOAD(int slot) {
		return new ALOAD(slot);
	}
	
	public Instruction STORE(int slot) {
		return new ASTORE(slot);
	}
	
	public Instruction RETURN() {
		return new ARETURN();
	}

	public Instruction NEWARRAY(int index) {
		return new ANEWARRAY(index);
	}
		
}
