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

public class FloatType extends FloatingPointType {

	public boolean identicalTo(Type other) {
		return (other instanceof FloatType);
	}

	public int distanceTo(Type other) {
		int result = Integer.MIN_VALUE;
		
		if (other instanceof FloatType) {
			result = 0;
		}
		else if (other instanceof DoubleType) {
			result = 1;
		}
		
		return result;
	}

	public String toString() {
		return Type.FloatRep;
	}

	/**
	  * Map an Espresso type to a ClassGen one.
	  */
	public ClassGenType toClassFileType() {
		return new ClassGenType(Constants.T_FLOAT);
	}

	public Instruction LOAD(int slot) {
		return new FLOAD(slot);
	}

	public Instruction STORE(int slot) {
		return new FSTORE(slot);
	}

	public Instruction RETURN() {
		return new FRETURN();
	}

	public Instruction PUSH(ConstantPoolGen cpg, BigDecimal value) {
		PUSH push = new PUSH(cpg, value.floatValue());	
		return push.getInstruction();
	}

	/*
	 * JVM instructions.
	 */
	 
	public Instruction ALOAD() {
		return new FALOAD();
	}

	public Instruction ASTORE() {
		return new FASTORE();
	}

	public Instruction ADD() {
		return new FADD();
	}

	public Instruction SUB() {
		return new FSUB();
	}

	public Instruction MUL() {
		return new FMUL();
	}

	public Instruction DIV() {
		return new FDIV();
	}

	public Instruction REM() {
		return new FREM();
	}

	public Instruction NEG() {
		return new FNEG();
	}

	public Instruction NEWARRAY(int index) {
		return new NEWARRAY(Constants.T_FLOAT);
	}

	public InstructionList CAST(Type other) {
		InstructionList il = new InstructionList();
		
		if (other instanceof DoubleType) {
			il.append(new F2D());
		}
		else if (other instanceof LongType) {
			il.append(new F2L());
		}
		else if (other instanceof IntegralType) {		// long tested first !
			il.append(new F2I());
			il.append(Type.Int.CAST(other));
		}
		else {
			il.append(new NOP());
		}
		
		return il;
	}

}
