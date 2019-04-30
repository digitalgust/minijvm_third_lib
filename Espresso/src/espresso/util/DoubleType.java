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

import java.math.BigDecimal;

public class DoubleType extends FloatingPointType {

	public boolean identicalTo(Type other) {
		return (other instanceof DoubleType);
	}

	public int distanceTo(Type other) {
		return (other instanceof DoubleType) ? 0 : Integer.MIN_VALUE;
	}

	public String toString() {
		return Type.DoubleRep;
	}

	/**
	  * Map an Espresso type to a ClassGen one.
	  */
	public ClassGenType toClassFileType() {
		return new ClassGenType(Constants.T_DOUBLE);
	}

	public boolean oneWord() {
		return false;
	}

	public boolean twoWords() {
		return true;
	}

	/*
	 * JVM instructions.
	 */

	public Instruction ALOAD() {
		return new DALOAD();
	}

	public Instruction ASTORE() {
		return new DASTORE();
	}

	public Instruction DUP() {
		return new DUP2();
	}

	public Instruction DUPX1() {
		return new DUP2_X1();
	}	 

	public Instruction DUPX2() {
		return new DUP2_X2();
	}

	public Instruction POP() {
		return new POP2();
	}

	public Instruction CMP(boolean less) {
		return less ? (Instruction) new DCMPG() : (Instruction) new DCMPL();
	}
	
	public Instruction LOAD(int slot) {
		return new DLOAD(slot);
	}

	public Instruction STORE(int slot) {
		return new DSTORE(slot);
	}

	public Instruction RETURN() {
		return new DRETURN();
	}

	public Instruction PUSH(ConstantPoolGen cpg, BigDecimal value) {
		PUSH push = new PUSH(cpg, value.doubleValue());	
		return push.getInstruction();
	}

	public Instruction ADD() {
		return new DADD();
	}

	public Instruction SUB() {
		return new DSUB();
	}

	public Instruction MUL() {
		return new DMUL();
	}

	public Instruction DIV() {
		return new DDIV();
	}

	public Instruction REM() {
		return new DREM();
	}

	public Instruction NEG() {
		return new DNEG();
	}

	public Instruction NEWARRAY(int index) {
		return new NEWARRAY(Constants.T_DOUBLE);
	}
	
	public InstructionList CAST(Type other) {
		InstructionList il = new InstructionList();
		
		if (other instanceof FloatType) {
			il.append(new D2F());
		}
		else if (other instanceof LongType) {
			il.append(new D2L());
		}
		else if (other instanceof IntegralType) {		// long tested first !
			il.append(new D2I());
			il.append(Type.Int.CAST(other));
		}
		else {
			il.append(new NOP());
		}
		
		return il;
	}

}
