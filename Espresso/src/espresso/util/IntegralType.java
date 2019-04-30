/**
 *
 * Copyright (C) 1998 Boston University
 *
 * Use of this file and the system it is part of is constrained by the
 * file COPYRIGHT in the root directory of this system.  You may, however,
 * make any modifications you wish to this file.
 *
 * Author: K. Doerig
 * Date: 4/15/98
 *
 */

package espresso.util;

import espresso.classfile.classgen.*;

import java.math.BigDecimal;

public abstract class IntegralType extends PrimitiveType {

	public boolean integralType() {
		return true;
	}
	
	public boolean floatingPointType() {
		return false;
	}	
	
	public abstract boolean inRange(long value);

	/*
	 * JVM instructions.
	 */
	public Instruction LOAD(int slot) {
		return new ILOAD(slot);
	}

	public Instruction STORE(int slot) {
		return new ISTORE(slot);
	}

	public Instruction RETURN() {
		return new IRETURN();
	}

	public Instruction ADD() {
		return new IADD();
	}

	public Instruction SUB() {
		return new ISUB();
	}

	public Instruction MUL() {
		return new IMUL();
	}

	public Instruction DIV() {
		return new IDIV();
	}

	public Instruction REM() {
		return new IREM();
	}

	public Instruction NEG() {
		return new INEG();
	}

	public Instruction AND() {
		return new IAND();
	}

	public Instruction OR() {
		return new IOR();
	}

	public Instruction XOR() {
		return new IXOR();
	}

	public Instruction PUSH(ConstantPoolGen cpg, BigDecimal value) {
		PUSH push = new PUSH(cpg, value.intValue());	
		return push.getInstruction();
	}

}
