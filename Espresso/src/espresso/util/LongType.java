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

public class LongType extends IntegralType {

	public boolean identicalTo(Type other) {
		return (other instanceof LongType);
	}

	public int distanceTo(Type other) {
		int result = Integer.MIN_VALUE;
		
		if (other instanceof LongType) {
			result = 0;
		}
		else if (other instanceof FloatType) {
			result = 1;
		}
		else if (other instanceof DoubleType) {
			result = 2;
		}
		
		return result;
	}

	public boolean inRange(long value) {
		return value >= java.lang.Long.MIN_VALUE && 
			   value <= java.lang.Long.MAX_VALUE;
	}

	public String toString() {
		return Type.LongRep;
	}

	/**
	  * Map an Espresso type to a ClassGen one.
	  */
	public ClassGenType toClassFileType() {
		return new ClassGenType(Constants.T_LONG);
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
		return new LCMP();
	}

	public Instruction LOAD(int slot) {
		return new LLOAD(slot);
	}

	public Instruction STORE(int slot) {
		return new LSTORE(slot);
	}

	public Instruction ALOAD() {
		return new LALOAD();
	}

	public Instruction ASTORE() {
		return new LASTORE();
	}

	public Instruction RETURN() {
		return new LRETURN();
	}

	public Instruction IFGT(boolean tozero) {
		return null;			// should use LCMP and IFGT
	}

	public Instruction ADD() {
		return new LADD();
	}

	public Instruction SUB() {
		return new LSUB();
	}

	public Instruction MUL() {
		return new LMUL();
	}

	public Instruction DIV() {
		return new LDIV();
	}

	public Instruction REM() {
		return new LREM();
	}

	public Instruction NEG() {
		return new LNEG();
	}

	public Instruction AND() {
		return new LAND();
	}

	public Instruction OR() {
		return new LOR();
	}

	public Instruction XOR() {
		return new LXOR();
	}

	public Instruction SHL() {
		return new LSHL();
	}

	public Instruction SHR() {
		return new LSHR();
	}

	public Instruction USHR() {
		return new LUSHR();
	}

	public Instruction PUSH(ConstantPoolGen cpg, BigDecimal value) {
		PUSH push = new PUSH(cpg, value.longValue());	
		return push.getInstruction();
	}

	public Instruction NEWARRAY(int index) {
		return new NEWARRAY(Constants.T_LONG);
	}
	
	public InstructionList CAST(Type other) {
		InstructionList il = new InstructionList();
		
		if (other instanceof FloatType) {
			il.append(new L2F());
		}
		else if (other instanceof DoubleType) {
			il.append(new L2D());
		}
		else if (other instanceof LongType) {
			il.append(new NOP());
		}
		else if (other instanceof IntegralType) {		// long tested first !
			il.append(new L2I());
			il.append(Type.Int.CAST(other));
		}
		else {
			il.append(new NOP());
		}
		
		return il;
	}

}
